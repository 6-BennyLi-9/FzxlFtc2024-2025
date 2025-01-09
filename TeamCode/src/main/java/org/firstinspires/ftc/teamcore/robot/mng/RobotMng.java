package org.firstinspires.ftc.teamcore.robot.mng;

import static org.firstinspires.ftc.teamcode.Global.gamepad1;
import static org.firstinspires.ftc.teamcode.Global.gamepad2;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.armScaleOperate;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.clipOption;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.decantOrSuspend;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.flipArm;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.highLowSpeedConfigChange;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.liftDecantUpping;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.liftHighSuspendPrepare;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.liftIDLE;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.sampleIO;
import static org.firstinspires.ftc.teamcode.util.GamepadRequests.syncRequests;

import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.packages.TaggedActionPackage;
import org.betastudio.ftc.client.TelemetryClient;
import org.firstinspires.ftc.teamcore.structure.ArmOp;
import org.firstinspires.ftc.teamcore.structure.ClawOp;
import org.firstinspires.ftc.teamcore.structure.ClipOp;
import org.firstinspires.ftc.teamcore.structure.DriveOp;
import org.firstinspires.ftc.teamcore.structure.LiftOp;
import org.firstinspires.ftc.teamcore.structure.PlaceOp;
import org.firstinspires.ftc.teamcore.structure.RotateOp;
import org.firstinspires.ftc.teamcore.structure.ScaleOp;
import org.firstinspires.ftc.teamcore.structure.positions.LiftMode;
import org.firstinspires.ftc.teamcore.structure.positions.ScalePositionTypes;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;

import java.util.HashMap;
import java.util.Map;

/**
 * 申名时需要初始化 {@link #initControllers()}
 */
public class RobotMng {
	public static final double  driverTriggerBufFal = 0.5;
	public static final char[]  printCode           = "-\\|/".toCharArray();
	public static final double  rotateTriggerBufFal = 0.01;

	public Map < String , HardwareController > controllers=new HashMap <>();

	public TaggedActionPackage thread        = new TaggedActionPackage();
	public static double       driveBufPower = 1;
	public int                 updateTime;

	public RobotMng() {
		controllers.put("arm", new ArmOp());
		controllers.put("clip", new ClipOp());
		controllers.put("claw", new  ClawOp());
		controllers.put("lift", new  LiftOp());
		controllers.put("place", new  PlaceOp());
		controllers.put("rotate", new  RotateOp());
		controllers.put("scale", new  ScaleOp());
		controllers.put("drive", new  DriveOp());
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
			if (LiftMode.highSuspend == LiftOp.recent || LiftMode.highSuspendPrepare == LiftOp.recent) {
				ClipOp.getInstance().open();
			}

			driveBufPower=1;
			LiftOp.getInstance().sync(LiftMode.idle);
		} else if (liftDecantUpping.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			if (LiftMode.idle == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.decantLow);
			} else if (LiftMode.decantLow == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.decantHigh);
			}

			PlaceOp.getInstance().prepare();
			driveBufPower=0.4;
		} else if (liftHighSuspendPrepare.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			LiftOp.getInstance().sync(LiftMode.highSuspendPrepare);
			driveBufPower=0.4;
		}

		if (decantOrSuspend.getEnabled()) {
			if (LiftMode.highSuspendPrepare == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.highSuspend);
			} else{
				ArmOp.getInstance().safe();
				PlaceOp.getInstance().flip();
			}
		}

		if (armScaleOperate.getEnabled()) {
			armScaleOperate.smartCounter.tickAndMod(2);

			//初始化
			switch (armScaleOperate.smartCounter.getTicked()) {
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
					throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.smartCounter.getTicked());
			}
		}
		switch (armScaleOperate.smartCounter.getTicked()) {
			case 0:
				ScaleOp.getInstance().back();
				break;
			case 1:
				RotateOp.getInstance().turn((gamepad2.left_trigger - gamepad2.right_trigger) * rotateTriggerBufFal);
				ScaleOp.getInstance().operate(- gamepad2.left_stick_y * 0.2 + 0.8);
				break;
			default:
				throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.smartCounter.getTicked());
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
