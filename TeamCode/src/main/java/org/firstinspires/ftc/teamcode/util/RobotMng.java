package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.armScaleOperate;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.clipOption;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.decantOrSuspend;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftDecantUpping;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftHighSuspendPrepare;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftIDLE;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.sampleIO;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.syncRequests;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.action.PriorityAction;
import org.firstinspires.ftc.teamcode.action.packages.TaggedActionPackage;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.structure.ArmOp;
import org.firstinspires.ftc.teamcode.structure.ClawOp;
import org.firstinspires.ftc.teamcode.structure.ClipOp;
import org.firstinspires.ftc.teamcode.structure.DriveOp;
import org.firstinspires.ftc.teamcode.structure.LiftOp;
import org.firstinspires.ftc.teamcode.structure.PlaceOp;
import org.firstinspires.ftc.teamcode.structure.RotateOp;
import org.firstinspires.ftc.teamcode.structure.ScaleOp;

import java.util.Map;

/**
 * 申名时需要初始化{@link #registerGamepad(Gamepad, Gamepad)} ， {@link #initActions()}
 */
public class RobotMng {
	public final static double              driverTriggerBufFal = 0.5;
	public              TaggedActionPackage thread;
	public static final char[]              printCode           = "-\\|/".toCharArray();
	@NonNull
	public static       Gamepad             gamepad1, gamepad2;
	public static double driveBufPower = 1;
	public static int    updateTime;

	public static final double rotateTriggerBufFal = 0.01;

	static {
		gamepad1 = new Gamepad();
		gamepad2 = new Gamepad();
	}

	public RobotMng() {
		thread = new TaggedActionPackage();
	}

	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2) {
		RobotMng.gamepad1 = gamepad1;
		RobotMng.gamepad2 = gamepad2;
	}

	public void initActions() {
		thread.add("arm", ArmOp.initController());
		thread.add("clip", ClipOp.initController());
		thread.add("claw", ClawOp.initController());
		thread.add("lift", LiftOp.initController());
		thread.add("place", PlaceOp.initController());
		thread.add("rotate", RotateOp.initController());
		thread.add("scale", ScaleOp.initController());
		thread.add("drive", DriveOp.initController());
	}

	public final void operateThroughGamepad() {
		syncRequests();

		if (clipOption.getEnabled()) {
			ClipOp.change();
		}

		if (sampleIO.getEnabled()) {
			ClawOp.change();
/*
			sampleIO.ticker.tickAndMod(2);
			switch (sampleIO.ticker.getTicked()){
				case 0:
					ClawOp.open();
					break;
				case 1:
					ClawOp.close();
					break;
				default:
					throw new IllegalStateException("SampleOptioning Unexpected value: " + sampleIO.ticker.getTicked());
			}
*/
		}

		if (liftIDLE.getEnabled()) {
			if (PlaceOp.PlacePositionTypes.decant == PlaceOp.recent()) {
				PlaceOp.idle();
			}
			if (LiftOp.LiftPositionTypes.highSuspend == LiftOp.recent() || LiftOp.LiftPositionTypes.highSuspendPrepare == LiftOp.recent()) {
				ClipOp.open();
			}

			LiftOp.sync(LiftOp.LiftPositionTypes.idle);
		} else if (liftDecantUpping.getEnabled()) {
			if (ArmOp.isNotSafe()) {
				ArmOp.safe();
			}

			if (LiftOp.LiftPositionTypes.idle == LiftOp.recent()) {
				LiftOp.sync(LiftOp.LiftPositionTypes.decantLow);
			} else if (LiftOp.LiftPositionTypes.decantLow == LiftOp.recent()) {
				LiftOp.sync(LiftOp.LiftPositionTypes.decantHigh);
			}
		} else if (liftHighSuspendPrepare.getEnabled()) {
			if (ArmOp.isNotSafe()) {
				ArmOp.safe();
			}

			LiftOp.sync(LiftOp.LiftPositionTypes.highSuspendPrepare);
		}

		if (decantOrSuspend.getEnabled()) {
			if (LiftOp.decanting()) {
				PlaceOp.decant();
			} else if (LiftOp.LiftPositionTypes.highSuspendPrepare == LiftOp.recent()) {
				LiftOp.sync(LiftOp.LiftPositionTypes.highSuspend);
			}
		}

		if (armScaleOperate.getEnabled()) {
			armScaleOperate.ticker.tickAndMod(2);
			switch (armScaleOperate.ticker.getTicked()) {
/*
				case 0:
					ScaleOp.back();
					ArmOp.safe();
					PlaceOp.safe();
					break;
*/
				case 0:
					RotateOp.mid();
					ScaleOp.back();
					ArmOp.idle();
					PlaceOp.idle();
					break;
				case 1:
					RotateOp.mid();
					ScaleOp.operate(gamepad2.left_stick_y * 0.2 + 0.8);
					ArmOp.intake();
					ClawOp.open();
					break;
				default:
					throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.ticker.getTicked());
			}
		}
		if (armScaleOperate.ticker.getTicked() == 1) {//特殊处理
			ScaleOp.operate(gamepad2.left_stick_x * 0.2 + 0.8);
			RotateOp.turn((gamepad2.right_trigger - gamepad2.left_trigger) * rotateTriggerBufFal);
		}
	}

	public final void driveThroughGamepad() {
		driveBufPower = 1 + gamepad1.right_stick_y * 0.3;

		DriveOp.sync(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, driveBufPower);

		if (gamepad1.left_bumper) {
			DriveOp.additions(0, 0, - 0.1);
		}
		if (gamepad1.right_bumper) {
			DriveOp.additions(0, 0, 0.1);
		}

		DriveOp.additions(0, 0, gamepad1.right_trigger - gamepad1.left_trigger, driverTriggerBufFal);

		if (gamepad1.a) {
			DriveOp.targetAngleRst();
		}
	}

	public void runThread() {
		thread.run();
	}

	public void printThreadDebugs() {
		++ updateTime;

		final String updateCode = "[" + printCode[updateTime % printCode.length] + "]";

		final Map <String, PriorityAction> map = thread.getActionMap();
		for (final Map.Entry <String, PriorityAction> entry : map.entrySet()) {
			final String         s = entry.getKey();
			final PriorityAction a = entry.getValue();
			TelemetryClient.getInstance().changeData(s + "-action", updateCode + a.getClass().getSimpleName() + ":" + a.paramsString());
		}
	}
}
