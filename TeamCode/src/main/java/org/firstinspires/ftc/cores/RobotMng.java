package org.firstinspires.ftc.cores;

import static org.firstinspires.ftc.teamcode.GamepadRequests.armScaleOperate;
import static org.firstinspires.ftc.teamcode.GamepadRequests.clipOption;
import static org.firstinspires.ftc.teamcode.GamepadRequests.decantOrSuspend;
import static org.firstinspires.ftc.teamcode.GamepadRequests.flipArm;
import static org.firstinspires.ftc.teamcode.GamepadRequests.highLowSpeedConfigChange;
import static org.firstinspires.ftc.teamcode.GamepadRequests.liftDecantUpping;
import static org.firstinspires.ftc.teamcode.GamepadRequests.liftHighSuspendPrepare;
import static org.firstinspires.ftc.teamcode.GamepadRequests.liftIDLE;
import static org.firstinspires.ftc.teamcode.GamepadRequests.sampleIO;
import static org.firstinspires.ftc.teamcode.GamepadRequests.switchViewMode;
import static org.firstinspires.ftc.teamcode.GamepadRequests.syncRequests;
import static org.firstinspires.ftc.teamcode.Global.gamepad1;
import static org.firstinspires.ftc.teamcode.Global.gamepad2;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.packages.TaggedActionPackage;
import org.betastudio.ftc.client.Client;
import org.betastudio.ftc.client.TelemetryClient;
import org.betastudio.ftc.client.ViewMode;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.InitializeRequested;
import org.betastudio.ftc.interfaces.Taggable;
import org.betastudio.ftc.interfaces.Updatable;
import org.firstinspires.ftc.cores.structure.ArmOp;
import org.firstinspires.ftc.cores.structure.ClawOp;
import org.firstinspires.ftc.cores.structure.ClipOp;
import org.firstinspires.ftc.cores.structure.DriveOp;
import org.firstinspires.ftc.cores.structure.LiftOp;
import org.firstinspires.ftc.cores.structure.PlaceOp;
import org.firstinspires.ftc.cores.structure.RotateOp;
import org.firstinspires.ftc.cores.structure.ScaleOp;
import org.firstinspires.ftc.cores.structure.positions.LiftMode;
import org.firstinspires.ftc.cores.structure.positions.ScalePositions;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.HardwareDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * 申名时需要初始化 {@link #initControllers()} , {@link #fetchClient()}
 */
public class RobotMng implements Updatable {
	public static final double                           driverTriggerBufFal = 0.5;
	public static final char[]                           printCode           = "-\\|/".toCharArray();
	public static final double                           rotateTriggerBufFal = 0.01;
	public static       double                           driveBufPower       = 1;
	public              Map <String, HardwareController> controllers         = new HashMap <>();
	public              TaggedActionPackage              thread              = new TaggedActionPackage();
	public              int                              updateTime;
	private             Client                           client;

	public RobotMng() {
		controllers.put("arm", new ArmOp());
		controllers.put("clip", new ClipOp());
		controllers.put("claw", new ClawOp());
		controllers.put("lift", new LiftOp());
		controllers.put("place", new PlaceOp());
		controllers.put("rotate", new RotateOp());
		controllers.put("scale", new ScaleOp());
		controllers.put("drive", new DriveOp());
	}

	public void fetchClient() {
		fetchClient(TelemetryClient.getInstance());
	}

	public void fetchClient(@NonNull Client client) {
		this.client = client;
	}

	public void initControllers() {
		for (Map.Entry <String, HardwareController> entry : controllers.entrySet()) {
			String             k = entry.getKey();
			HardwareController v = entry.getValue();

			v.connect();
			v.writeToInstance();

			if (v instanceof InitializeRequested) {
				((InitializeRequested) v).init();
			}
			if (v instanceof Taggable) {
				((Taggable) v).setTag(k + ":ctrl");
			}

			thread.add(k, v.getController());
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

			driveBufPower = 1;
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
			driveBufPower = 0.5;
		} else if (liftHighSuspendPrepare.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			LiftOp.getInstance().sync(LiftMode.highSuspendPrepare);
		}

		if (decantOrSuspend.getEnabled()) {
			if (LiftMode.highSuspendPrepare == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.highSuspend);
			} else {
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

		if (flipArm.getEnabled()) {
			if (ScalePositions.probe == ScaleOp.recent) {
				ArmOp.getInstance().flipIO();
			}
		}

		if (switchViewMode.getEnabled()) {
			if (client.getCurrentViewMode() == ViewMode.BASIC_TELEMETRY) {
				client.configViewMode(ViewMode.THREAD_MANAGER);
			} else {
				client.configViewMode(ViewMode.BASIC_TELEMETRY);
			}
			client.speak("The telemetry's ViewMode has recently switched to " + client.getCurrentViewMode().name());
		}
	}

	public final void driveThroughGamepad() {
		if (highLowSpeedConfigChange.getEnabled()) {
			if (1 == driveBufPower) {
				driveBufPower = 0.5;
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

	@Override
	public void update() {
		thread.run();
	}

	public void printActions() {
		++ updateTime;

		final String updateCode = "[" + printCode[updateTime % printCode.length] + "]";

		final Map <String, PriorityAction> map = thread.getActionMap();
		for (final Map.Entry <String, PriorityAction> entry : map.entrySet()) {
			final String         s = entry.getKey();
			final PriorityAction a = entry.getValue();
			client.changeData(s, updateCode + a.paramsString());
		}
	}

	public void printIMUVariables() {
		BNO055IMU   imu         = HardwareDatabase.imu;
		Orientation orientation = imu.getAngularOrientation();
		client.changeData("∠1", orientation.firstAngle).changeData("∠2", orientation.secondAngle).changeData("∠3", orientation.thirdAngle);

		Acceleration acceleration = imu.getLinearAcceleration();
		client.changeData("VelΔx", acceleration.xAccel).changeData("VelΔy", acceleration.yAccel).changeData("VelΔz", acceleration.zAccel);
	}
}
