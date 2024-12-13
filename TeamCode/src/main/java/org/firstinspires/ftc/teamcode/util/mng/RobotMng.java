package org.firstinspires.ftc.teamcode.util.mng;

import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.armScaleOperate;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.clipOption;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.decantOrSuspend;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.flipArm;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.highLowSpeedConfigChange;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftDecantUpping;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftHighSuspendPrepare;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftIDLE;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.sampleIO;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.syncRequests;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.packages.TaggedActionPackage;
import org.betastudio.ftc.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.structure.ArmOp;
import org.firstinspires.ftc.teamcode.structure.ClawOp;
import org.firstinspires.ftc.teamcode.structure.ClipOp;
import org.firstinspires.ftc.teamcode.structure.DriveOp;
import org.firstinspires.ftc.teamcode.structure.LiftOp;
import org.firstinspires.ftc.teamcode.structure.PlaceOp;
import org.firstinspires.ftc.teamcode.structure.RotateOp;
import org.firstinspires.ftc.teamcode.structure.ScaleOp;
import org.firstinspires.ftc.teamcode.structure.positions.LiftPositionTypes;
import org.firstinspires.ftc.teamcode.structure.positions.ScalePositionTypes;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;

import java.util.HashMap;
import java.util.Map;

/**
 * 申名时需要初始化{@link #registerGamepad(Gamepad, Gamepad)} , {@link #initControllers()}
 */
public class RobotMng {
	public static final double  driverTriggerBufFal = 0.5;
	public static final char[]  printCode           = "-\\|/".toCharArray();
	public static final double  rotateTriggerBufFal = 0.01;
	@NonNull
	public static       Gamepad gamepad1, gamepad2;

	public Map < String , HardwareController > controllers=new HashMap <>();

	static {
		gamepad1 = new Gamepad();
		gamepad2 = new Gamepad();
	}

	public TaggedActionPackage thread        = new TaggedActionPackage();
	public double              driveBufPower = 1;
	public int                 updateTime;

	public RobotMng() {
		controllers.put("arm", new ArmOp());
		controllers.put("clip", new ClawOp());
		controllers.put("claw", new  ClawOp());
		controllers.put("lift", new  LiftOp());
		controllers.put("place", new  PlaceOp());
		controllers.put("rotate", new  RotateOp());
		controllers.put("scale", new  ScaleOp());
		controllers.put("drive", new  DriveOp());
	}

	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2) {
		RobotMng.gamepad1 = gamepad1;
		RobotMng.gamepad2 = gamepad2;
	}

	public void initControllers() {
		for (Map.Entry <String, HardwareController> entry : controllers.entrySet()) {
			String             k = entry.getKey();
			HardwareController v = entry.getValue();

			v.connect();
			v.writeToInstance();

			if(v instanceof InitializeRequested){
				((InitializeRequested) v).init();
			}
			if(v instanceof TagRequested){
				((TagRequested) v).setTag(k+":ctrl");
			}

			thread.add(k,v.getController());
		}
	}

	public final void operateThroughGamepad() {
		syncRequests();

		if (clipOption.getEnabled()) {
			ClipOp.getInstance().change();
		}

		if (sampleIO.getEnabled()) {
			ClawOp.getInstance().change();
		}

		if (liftIDLE.getEnabled()) {
			if (PlaceOp.getInstance().decanting()) {
				PlaceOp.getInstance().idle();
			}
			if (LiftPositionTypes.highSuspend == LiftOp.recent || LiftPositionTypes.highSuspendPrepare == LiftOp.recent) {
				ClipOp.getInstance().open();
			}

			LiftOp.getInstance().sync(LiftPositionTypes.idle);
		} else if (liftDecantUpping.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			if (LiftPositionTypes.idle == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftPositionTypes.decantLow);
			} else if (LiftPositionTypes.decantLow == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftPositionTypes.decantHigh);
			}

			PlaceOp.getInstance().prepare();
		} else if (liftHighSuspendPrepare.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			LiftOp.getInstance().sync(LiftPositionTypes.highSuspendPrepare);
		}

		if (decantOrSuspend.getEnabled()) {
			if (LiftPositionTypes.highSuspendPrepare == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftPositionTypes.highSuspend);
			} else{
				ArmOp.getInstance().safe();
				PlaceOp.getInstance().flip();
			}
		}

		if (armScaleOperate.getEnabled()) {
			armScaleOperate.ticker.tickAndMod(2);

			//初始化
			switch (armScaleOperate.ticker.getTicked()) {
				case 0:
					RotateOp.getInstance().mid();
					PlaceOp.getInstance().idle();
					ArmOp.getInstance().idle();
					break;
				case 1:
					ClawOp.getInstance().open();
					ArmOp.getInstance().intake();
					break;
				default:
					throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.ticker.getTicked());
			}
		}
		switch (armScaleOperate.ticker.getTicked()) {
			case 0:
				ScaleOp.getInstance().back();
				break;
			case 1:
				RotateOp.getInstance().turn((gamepad2.left_trigger - gamepad2.right_trigger) * rotateTriggerBufFal);
				ScaleOp.getInstance().operate(- gamepad2.left_stick_y * 0.2 + 0.8);
				break;
			default:
				throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.ticker.getTicked());
		}

		if(flipArm.getEnabled()){
			if(ScalePositionTypes.probe == ScaleOp.recent){
				ArmOp.getInstance().flipIO();
			}
		}
	}

	public final void driveThroughGamepad() {
		if (highLowSpeedConfigChange.getEnabled()) {
			if (1 == driveBufPower) {
				driveBufPower = 0.4;
			} else {
				driveBufPower = 1;
			}
		}

		DriveOp.getInstance().sync(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, driveBufPower);

		if (gamepad1.left_bumper) {
			DriveOp.getInstance().additions(0, 0, - 0.2);
		}
		if (gamepad1.right_bumper) {
			DriveOp.getInstance().additions(0, 0, 0.2);
		}

		DriveOp.getInstance().additions(0, 0, gamepad1.right_trigger - gamepad1.left_trigger, driverTriggerBufFal);

		if (gamepad1.a) {
			DriveOp.getInstance().targetAngleRst();
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
			TelemetryClient.getInstance().changeData(s + updateCode,  a.paramsString());
		}
	}
}
