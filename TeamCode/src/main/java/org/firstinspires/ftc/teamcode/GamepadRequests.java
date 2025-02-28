package org.firstinspires.ftc.teamcode;

import static org.betastudio.ftc.util.ButtonConfig.SINGLE_WHEN_PRESSED;
import static org.betastudio.ftc.util.ButtonProcessorEx.*;
import static org.firstinspires.ftc.teamcode.Global.gamepad1;
import static org.firstinspires.ftc.teamcode.Global.gamepad2;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.util.ButtonConfig;
import org.betastudio.ftc.util.ButtonProcessorEx;

/**
 * gamepad 控制请求的数据库
 * 该类用于管理通过游戏手柄发送的控制请求，每个请求对应一个按钮处理器实例。
 * 请求的同步和打印值方法允许程序根据手柄输入的状态执行相应的动作。
 *
 * @see ButtonProcessorEx
 * @see ButtonConfig
 * @see Global
 */
public final class GamepadRequests {
	/**
	 * 输入样本
	 * 用于处理从gamepad2的A按钮输入的请求。
	 */
	public static final ButtonProcessorEx sampleIO;
	/**
	 * 电梯上筐
	 * 用于处理从gamepad2的左扳机按钮输入的请求。
	 */
	public static final ButtonProcessorEx liftDecantUpping;
	/**
	 * 电梯闲置（下）
	 * 用于处理从gamepad2的dpad向下按钮输入的请求。
	 */
	public static final ButtonProcessorEx liftIDLE;
	/**
	 * 电梯挂样本准备
	 * 用于处理从gamepad2的dpad向上按钮输入的请求。
	 */
	public static final ButtonProcessorEx liftHighSuspendPrepare;
	/**
	 * 倒筐与挂样本
	 * 用于处理从gamepad2的X按钮输入的请求。
	 */
	public static final ButtonProcessorEx decantOrSuspend;
	/**
	 * 打开/关闭样本夹
	 * 用于处理从gamepad2的B按钮输入的请求。
	 */
	public static final ButtonProcessorEx clipOption;
	/**
	 * 吸取滑轨与收取杆操作
	 * 用于处理从gamepad2的右扳机按钮输入的请求。
	 */
	public static final ButtonProcessorEx armScaleOperate;
	/**
	 * 快慢速切换
	 * 用于处理从gamepad1的A按钮输入的请求。
	 */
	public static final ButtonProcessorEx highLowSpeedConfigChange;
	/**
	 * 抬降arm
	 * 用于处理从gamepad2的Y按钮输入的请求。
	 */
	public static final ButtonProcessorEx flipArm;
	/**
	 * 切换视图模式
	 * 用于处理从gamepad1的右摇杆Y轴大于0.8的输入请求。
	 */
	public static final ButtonProcessorEx switchViewMode;

	static {
		sampleIO = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		liftDecantUpping = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		liftHighSuspendPrepare = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		liftIDLE = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		decantOrSuspend = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		clipOption = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		armScaleOperate = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		highLowSpeedConfigChange = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		flipArm = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);
		switchViewMode = new ButtonProcessorEx(SINGLE_WHEN_PRESSED);

		sampleIO.sync(false);
		liftDecantUpping.sync(false);
		liftHighSuspendPrepare.sync(false);
		liftIDLE.sync(false);
		decantOrSuspend.sync(false);
		clipOption.sync(false);
		armScaleOperate.sync(false);
		highLowSpeedConfigChange.sync(false);
		flipArm.sync(false);
		switchViewMode.sync(false);
	}

	public static void reboot(){
		sampleIO.sync(false);
		liftDecantUpping.sync(false);
		liftHighSuspendPrepare.sync(false);
		liftIDLE.sync(false);
		decantOrSuspend.sync(false);
		clipOption.sync(false);
		armScaleOperate.sync(false);
		highLowSpeedConfigChange.sync(false);
		flipArm.sync(false);
		switchViewMode.sync(false);

		sampleIO.setCallback(defaultCallback);
		liftDecantUpping.setCallback(defaultCallback);
		liftHighSuspendPrepare.setCallback(defaultCallback);
		liftIDLE.setCallback(defaultCallback);
		decantOrSuspend.setCallback(defaultCallback);
		clipOption.setCallback(defaultCallback);
		armScaleOperate.setCallback(defaultCallback);
		highLowSpeedConfigChange.setCallback(defaultCallback);
		flipArm.setCallback(defaultCallback);
		switchViewMode.setCallback(defaultCallback);
	}

	/**
	 * 同步请求
	 * 该方法用于将游戏手柄的输入状态同步到各个按钮处理器中。
	 * 它检查每个游戏手柄按钮或摇杆的状态，并相应地更新按钮处理器的状态。
	 */
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

		switchViewMode.sync(0.8 < gamepad1.right_stick_y);
	}

	/**
	 * 打印值
	 * <p>
	 * 该方法用于通过Client将各个按钮处理器的状态打印到控制台。
	 * <p>
	 * 这有助于调试和监控程序的运行状态。
	 */
	public static void printValues(@NonNull final Client client) {
		client.changeData("liftDecantUpping", liftDecantUpping);
		client.changeData("sampleIO", sampleIO);
		client.changeData("liftHighSuspendPrepare", liftHighSuspendPrepare);
		client.changeData("liftIDLE", liftIDLE);
		client.changeData("decantOrSuspend", decantOrSuspend);
		client.changeData("clipOption", clipOption);
		client.changeData("armScaleOperate", armScaleOperate);
		client.changeData("highLowSpeedConfigChange", highLowSpeedConfigChange);
		client.changeData("flipArm", flipArm);
		client.changeData("switchViewMode", switchViewMode);
	}
}
