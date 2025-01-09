package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.Global.gamepad1;
import static org.firstinspires.ftc.teamcode.Global.gamepad2;
import static org.firstinspires.ftc.teamcode.util.ButtonProcessor.ButtonConfig.SingleWhenPressed;

import org.betastudio.ftc.client.Client;
import org.betastudio.ftc.client.TelemetryClient;

/**
 * gamepad 控制请求的数据库
 *
 * @see ButtonProcessor
 */
public final class GamepadRequests {
	/**
	 * 输入样本
	 */
	public static final ButtonProcessor sampleIO;
	/**
	 * 电梯上筐
	 */
	public static final ButtonProcessor liftDecantUpping;
	/**
	 * 电梯闲置（下）
	 */
	public static final ButtonProcessor liftIDLE;
	/**
	 * 电梯挂样本准备
	 */
	public static final ButtonProcessor liftHighSuspendPrepare;
	/**
	 * 倒筐与挂样本
	 */
	public static final ButtonProcessor decantOrSuspend;
	/**
	 * 打开/关闭样本夹
	 */
	public static final ButtonProcessor clipOption;
	/**
	 * 吸取滑轨与收取杆操作
	 */
	public static final ButtonProcessor armScaleOperate;
	/**
	 * 快慢速切换
	 */
	public static final ButtonProcessor highLowSpeedConfigChange;
	/**抬降arm*/
	public static final ButtonProcessor flipArm;
	public static final ButtonProcessor switchViewMode;

	static {
		sampleIO = new ButtonProcessor(SingleWhenPressed);
		liftDecantUpping = new ButtonProcessor(SingleWhenPressed);
		liftHighSuspendPrepare = new ButtonProcessor(SingleWhenPressed);
		liftIDLE = new ButtonProcessor(SingleWhenPressed);
		decantOrSuspend = new ButtonProcessor(SingleWhenPressed);
		clipOption = new ButtonProcessor(SingleWhenPressed);
		armScaleOperate = new ButtonProcessor(SingleWhenPressed);
		highLowSpeedConfigChange = new ButtonProcessor(SingleWhenPressed);
		flipArm=new ButtonProcessor(SingleWhenPressed);
		switchViewMode=new ButtonProcessor(SingleWhenPressed);
	}

	public static void syncRequests() {
		sampleIO.sync(gamepad2.a);
		liftDecantUpping.sync(gamepad2.left_bumper);
		liftHighSuspendPrepare.sync(gamepad2.dpad_up);
		liftIDLE.sync(gamepad2.dpad_down);
		decantOrSuspend.sync(gamepad2.x);
		clipOption.sync(gamepad2.b);
		armScaleOperate.sync(gamepad2.right_bumper);
		flipArm.sync(gamepad2.y);

		highLowSpeedConfigChange.sync(gamepad1.a);

		switchViewMode.sync(gamepad1.right_stick_y>0.8);
	}

	public static void printValues() {
		final Client instance = TelemetryClient.getInstance();
		instance.changeData("liftDecantUpping", liftDecantUpping);
		instance.changeData("sampleIO", sampleIO);
		instance.changeData("liftHighSuspendPrepare", liftHighSuspendPrepare);
		instance.changeData("liftIDLE", liftIDLE);
		instance.changeData("decantOrSuspend", decantOrSuspend);
		instance.changeData("clipOption", clipOption);
		instance.changeData("armScaleOperate", armScaleOperate);
		instance.changeData("highLowSpeedConfigChange", highLowSpeedConfigChange);
		instance.changeData("flipArm", flipArm);
	}
}
