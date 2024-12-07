package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.mng.RobotMng.gamepad1;
import static org.firstinspires.ftc.teamcode.util.mng.RobotMng.gamepad2;
import static org.firstinspires.ftc.teamcode.util.UtilButtonControlSystem.ButtonConfig.SingleWhenPressed;

import org.betastudio.ftc.client.TelemetryClient;

/**
 * gamepad 控制请求的数据库
 *
 * @see UtilButtonControlSystem
 */
public enum GamepadRequestMemories {
	;
	/**
	 * 输入样本
	 */
	public static final UtilButtonControlSystem sampleIO;
	/**
	 * 电梯上筐
	 */
	public static final UtilButtonControlSystem liftDecantUpping;
	/**
	 * 电梯闲置（下）
	 */
	public static final UtilButtonControlSystem liftIDLE;
	/**
	 * 电梯挂样本准备
	 */
	public static final UtilButtonControlSystem liftHighSuspendPrepare;
	/**
	 * 倒筐与挂样本
	 */
	public static final UtilButtonControlSystem decantOrSuspend;
	/**
	 * 打开/关闭样本夹
	 */
	public static final UtilButtonControlSystem clipOption;
	/**
	 * 吸取滑轨与收取杆操作
	 */
	public static final UtilButtonControlSystem armScaleOperate;
	/**
	 * 快慢速切换
	 */
	public static final UtilButtonControlSystem highLowSpeedConfigChange;

	static {
		sampleIO = new UtilButtonControlSystem(SingleWhenPressed);
		liftDecantUpping = new UtilButtonControlSystem(SingleWhenPressed);
		liftHighSuspendPrepare = new UtilButtonControlSystem(SingleWhenPressed);
		liftIDLE = new UtilButtonControlSystem(SingleWhenPressed);
		decantOrSuspend = new UtilButtonControlSystem(SingleWhenPressed);
		clipOption = new UtilButtonControlSystem(SingleWhenPressed);
		armScaleOperate = new UtilButtonControlSystem(SingleWhenPressed);
		highLowSpeedConfigChange = new UtilButtonControlSystem(SingleWhenPressed);
	}

	public static void syncRequests() {
		sampleIO.sync(gamepad2.a);
		liftDecantUpping.sync(gamepad2.left_bumper);
		liftHighSuspendPrepare.sync(gamepad2.dpad_up);
		liftIDLE.sync(gamepad2.dpad_down);
		decantOrSuspend.sync(gamepad2.x);
		clipOption.sync(gamepad2.b);
		armScaleOperate.sync(gamepad2.right_bumper);

		highLowSpeedConfigChange.sync(gamepad1.a);
	}

	public static void printValues() {
		final TelemetryClient instance = TelemetryClient.getInstance();
		instance.changeData("liftDecantUpping", liftDecantUpping);
		instance.changeData("sampleIO", sampleIO);
		instance.changeData("liftHighSuspendPrepare", liftHighSuspendPrepare);
		instance.changeData("liftIDLE", liftIDLE);
		instance.changeData("decantOrSuspend", decantOrSuspend);
		instance.changeData("clipOption", clipOption);
		instance.changeData("armScaleOperate", armScaleOperate);
		instance.changeData("highLowSpeedConfigChange", highLowSpeedConfigChange);
	}
}
