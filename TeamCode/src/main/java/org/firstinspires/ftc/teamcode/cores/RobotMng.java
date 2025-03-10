package org.firstinspires.ftc.teamcode.cores;

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
import static org.firstinspires.ftc.teamcode.Global.gamepad1;
import static org.firstinspires.ftc.teamcode.Global.gamepad2;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.packages.TaggedActionPackage;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.util.message.DriveBufMsg;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrlMode;
import org.firstinspires.ftc.teamcode.cores.structure.ArmOp;
import org.firstinspires.ftc.teamcode.cores.structure.ClawOp;
import org.firstinspires.ftc.teamcode.cores.structure.ClipOp;
import org.firstinspires.ftc.teamcode.cores.structure.DriveOp;
import org.firstinspires.ftc.teamcode.cores.structure.LiftOp;
import org.firstinspires.ftc.teamcode.cores.structure.PlaceOp;
import org.firstinspires.ftc.teamcode.cores.structure.RotateOp;
import org.firstinspires.ftc.teamcode.cores.structure.ScaleOp;
import org.firstinspires.ftc.teamcode.cores.structure.positions.LiftMode;
import org.firstinspires.ftc.teamcode.cores.structure.positions.ScalePositions;

import java.util.HashMap;
import java.util.Map;

/**
 * 机器人管理类，实现了 {@link Interfaces.Updatable} 接口。在使用此类时，需要先初始化硬件控制器 {@link #initControllers()}，
 * 然后获取客户端 {@link #fetchClient()}。
 */
@Config
public class RobotMng implements Interfaces.Updatable {
	/**
	 * 打印代码的字符数组，用于在 telemetry 中显示状态更新
	 */
	public static final String                                      printCode           = "-\\|/";
	/**
	 * 驱动杆缓冲阈值
	 */
	public static final double                                      driverTriggerBufFal = 0.2;
	/**
	 * 旋转触发缓冲失败的阈值
	 */
	public static final double                                      rotateTriggerBufFal = 0.01;
	/**
	 * 硬件控制器的映射表
	 */
	public final        Map <String, Interfaces.HardwareController> controllers         = new HashMap <>();
	/**
	 * 标记的 Action 包，用于管理不同硬件控制器的动作
	 */
	public final        TaggedActionPackage                         thread              = new TaggedActionPackage();
	/**
	 * 更新时间，用于计算 telemetry 的更新状态
	 */
	public              int                                         updateTime;
	/**
	 * 客户端对象，用于与控制台通信
	 */
	private             Client                                      client;

	/**
	 * 构造函数，在创建 RobotMng 对象时初始化各个硬件控制器并将其放入控制器映射表中
	 */
	public RobotMng() {
		controllers.put("arm", new ArmOp());
		controllers.put("clip", new ClipOp());
		controllers.put("claw", new ClawOp());
		controllers.put("rightLift", new LiftOp());
		controllers.put("place", new PlaceOp());
		controllers.put("rotate", new RotateOp());
		controllers.put("scale", new ScaleOp());
		controllers.put("drive", new DriveOp());
	}

	/**
	 * 获取默认的 telemetry 客户端
	 */
	public void fetchClient() {
		fetchClient(Global.client);
	}

	/**
	 * 设置客户端对象
	 *
	 * @param client 需要设定的客户端对象，不能为空
	 */
	public void fetchClient(@NonNull final Client client) {
		this.client = client;
	}

	/**
	 * 初始化所有的硬件控制器，连接硬件，写入实例，并根据需要执行初始化、设置标签操作
	 */
	public void initControllers() {
		for (final Map.Entry <String, Interfaces.HardwareController> entry : controllers.entrySet()) {
			final String                        k = entry.getKey();
			final Interfaces.HardwareController v = entry.getValue();

			v.connect();
			v.writeToInstance();

			if (v instanceof Interfaces.InitializeRequested) {
				((Interfaces.InitializeRequested) v).init();
			}
			if (v instanceof Interfaces.TagOptionsRequired) {
				((Interfaces.TagOptionsRequired) v).setTag(k + ":ctrl");
			}

			thread.add(k, v.getController());
		}
	}

	/**
	 * 根据游戏手柄的操作来控制机器人，包括剪切、采样、提升、放置等动作。
	 * 此方法会同步游戏手柄请求，然后根据不同的按钮和开关执行相应的操作。
	 */
	public final void operateThroughGamepad() {
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
			if (LiftMode.HIGH_SUSPEND == LiftOp.recent || LiftMode.HIGH_SUSPEND_PREPARE == LiftOp.recent) {
				ClipOp.getInstance().open();
			}

			ChassisCtrl.mode = ChassisCtrlMode.FASTER_CONTROL;
			LiftOp.getInstance().sync(LiftMode.IDLE);
		} else if (liftDecantUpping.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			if (LiftMode.IDLE == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.DECANT_LOW);
			} else if (LiftMode.DECANT_LOW == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.DECANT_HIGH);
			}

			ChassisCtrl.mode = ChassisCtrlMode.NONE_SPECIFIED;
			PlaceOp.getInstance().prepare();
		} else if (liftHighSuspendPrepare.getEnabled()) {
			if (ArmOp.getInstance().isNotSafe()) {
				ArmOp.getInstance().safe();
			}

			LiftOp.getInstance().sync(LiftMode.HIGH_SUSPEND_PREPARE);
		}

		if (decantOrSuspend.getEnabled()) {
			if (LiftMode.HIGH_SUSPEND_PREPARE == LiftOp.recent) {
				LiftOp.getInstance().sync(LiftMode.HIGH_SUSPEND);
			} else {
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
					Global.service.execute(() -> {
						Local.sleep(500);
						ClawOp.getInstance().open();
					});
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
				ScaleOp.getInstance().operate(- gamepad2.left_stick_y * 0.15 + 0.2);
				break;
			default:
				throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.ticker.getTicked());
		}

		if (flipArm.getEnabled()) {
			if (ScalePositions.PROBE == ScaleOp.recent) {
				ArmOp.getInstance().flipIO();
			}
		}

		if (switchViewMode.getEnabled()) {
			client.switchViewMode();
			client.speak("The telemetry's ClientViewMode has recently switched to " + client.getCurrentViewMode());
			FtcLogTunnel.MAIN.report("ClientViewMode switched to " + client.getCurrentViewMode());
		}
	}

	/**
	 * 根据游戏手柄的操作来控制机器人的驱动，包括速度切换、转向和复位操作。
	 * 该方法会根据不同的游戏手柄输入调整驱动模式。
	 */
	public final void driveThroughGamepad() {
		if (highLowSpeedConfigChange.getEnabled()) {
			switch (ChassisCtrl.mode) {
				case FASTER_CONTROL:
					ChassisCtrl.mode = ChassisCtrlMode.SLOWER_CONTROL;
					break;
				case NONE_SPECIFIED:
				case SLOWER_CONTROL:
				default:
					ChassisCtrl.mode = ChassisCtrlMode.FASTER_CONTROL;
					break;
			}
		}

		DriveOp.getInstance().sync(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

		if (gamepad1.left_bumper) {
			DriveOp.getInstance().turn(- 0.1);
		}
		if (gamepad1.right_bumper) {
			DriveOp.getInstance().turn(0.1);
		}

		DriveOp.getInstance().turn(gamepad1.right_trigger - gamepad1.left_trigger, new DriveBufMsg(driverTriggerBufFal));

		if (gamepad1.a) {
			DriveOp.getInstance().targetAngleRst();
		}
	}

	/**
	 * 更新方法，执行标记动作包中的所有动作。
	 * 在机器人的每一周期中调用，用于驱动所有硬件控制器的操作。
	 */
	@Override
	public void update() {
		thread.activate();
	}

	public void printActions() {
		++ updateTime;

		final String updateCode = "[" + printCode.charAt(updateTime % printCode.length()) + "]";
//		final String lastUpdateCode = "[" + printCode.charAt((updateTime - 1) % printCode.length()) + "]";

		final Map <String, PriorityAction> map = thread.getActionMap();
		for (final Map.Entry <String, PriorityAction> entry : map.entrySet()) {
			final String         s = entry.getKey();
			final PriorityAction a = entry.getValue();
			client.changeData(s + "\t", updateCode + a.paramsString());
		}
	}

	public void printIMUVariables() {
		final BNO055IMU   imu         = HardwareDatabase.imu;
		final Orientation orientation = imu.getAngularOrientation();
		client.changeData("∠1", orientation.firstAngle);
		client.changeData("∠2", orientation.secondAngle);
		client.changeData("∠3", orientation.thirdAngle);

		final Acceleration acceleration = imu.getLinearAcceleration();
		client.changeData("VelΔx", acceleration.xAccel);
		client.changeData("VelΔy", acceleration.yAccel);
		client.changeData("VelΔz", acceleration.zAccel);
	}
}

