package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;

/**
 * LiftCtrl 是一个抽象类，实现了 Action 和 DashboardCallable 接口，用于控制机器人电梯结构。
 */
public abstract class LiftCtrl implements Action {
	protected final DcMotorEx leftLift;
	protected final DcMotorEx rightLift;
	protected       long      currentPosition;
	protected       long      targetPosition;
	protected       String    tag;

	/**
	 * 构造函数，初始化目标升降机构电机和标签。
	 *
	 * @param leftLift 目标升降机构电机
	 */
	protected LiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final  DcMotorEx rightLift) {
		this.leftLift = leftLift;
		this.rightLift = rightLift;
		tag = "lift";
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
}
