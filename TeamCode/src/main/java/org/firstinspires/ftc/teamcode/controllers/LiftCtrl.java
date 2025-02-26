package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.teamcode.HardwareDatabase;

/**
 * LiftCtrl 是一个抽象类，实现了 Action 和 DashboardCallable 接口，用于控制机器人电梯结构。
 */
public abstract class LiftCtrl implements Action, Interfaces.DashboardCallable {
	protected final DcMotorEx targetLift; // 目标升降机构电机
	protected       long      currentPosition;
	protected       long      targetPosition;
	protected       String    tag; // 控制器的标签
	private         boolean   infinityRun = true; // 是否无限运行

	/**
	 * 构造函数，初始化目标升降机构电机和标签。
	 *
	 * @param target 目标升降机构电机
	 */
	protected LiftCtrl(@NonNull final DcMotorEx target) {
		targetLift = target; // 设置目标电机
		tag = target.getDeviceName(); // 设置标签为电机的设备名称
	}

	/**
	 * 运行升降机构控制逻辑。
	 *
	 * @return 如果无限运行，总是返回 true；否则返回校准是否完成的反值
	 */
	@Override
	public boolean activate() {
		currentPosition = targetLift.getCurrentPosition(); // 获取当前电机位置

		modify(); // 调用修改方法
		targetLift.setPower(getCalibrateVal()); // 设置电机功率

		if (! HardwareDatabase.liftTouch.isPressed()) {
			targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // 停止并重置编码器
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // 无编码器模式运行
		}

		if (infinityRun) return true; // 如果无限运行，返回 true
		return ! getCalibrateDone(); // 否则返回校准是否完成的反值
	}

	/**
	 * 抽象类执行具体操作的方法。
	 * <p>
	 * 如果要修改大纲，重写{@link #activate()}
	 */
	public abstract void modify();

	/**
	 * 获取校准值的方法。
	 *
	 * @return 校准值
	 */
	public abstract double getCalibrateVal();

	/**
	 * @return 校准是否完成
	 */
	public boolean getCalibrateDone() {
		return true; // 默认认为校准已完成
	}

	/**
	 * 返回控制器的参数字符串，包含当前和目标位置。
	 *
	 * @return 参数字符串
	 */
	@Override
	public String paramsString() {
		return tag + ":" + currentPosition + "->" + targetPosition; // 返回参数字符串
	}

	/**
	 * 获取控制器的标签。
	 *
	 * @return 标签字符串
	 */
	public String getTag() {
		return this.tag; // 返回标签
	}

	/**
	 * 设置控制器的标签。
	 *
	 * @param tag 标签字符串
	 */
	public void setTag(final String tag) {
		this.tag = tag; // 设置标签
	}

	/**
	 * 获取目标位置。
	 *
	 * @return 目标位置
	 */
	public long getTargetPosition() {
		return targetPosition; // 返回目标位置
	}

	/**
	 * 设置目标位置。
	 *
	 * @param targetPosition 目标位置
	 */
	public void setTargetPosition(final long targetPosition) {
		this.targetPosition = targetPosition; // 设置目标位置
	}

	/**
	 * 获取当前位置。
	 *
	 * @return 当前位置
	 */
	public long getCurrentPosition() {
		return currentPosition; // 返回当前位置
	}

	/**
	 * 获取误差位置（目标位置与当前位置的差值）。
	 *
	 * @return 误差位置
	 */
	public long getErrorPosition() {
		return targetPosition - currentPosition; // 返回误差位置
	}

	/**
	 * 禁用无限运行模式。
	 */
	@Deprecated
	protected void disableInfinityRun() {
		this.infinityRun = false; // 设置无限运行模式为 false
	}

	@Override
	public void process(@NonNull final TelemetryMsg messageOverride) {
		messageOverride.add(new TelemetryItem("lift-current", currentPosition)); // 发送当前电机位置
		messageOverride.add(new TelemetryItem("lift-target", targetPosition)); // 发送目标电机位置
	}
}
