package org.firstinspires.ftc.teamcode.controllers;

import static java.lang.Math.max;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;

/**
 * AbstractLiftCtrl 是一个抽象类，实现了 Action 和 DashboardCallable 接口，用于控制机器人电梯结构。
 */
public abstract class AbstractLiftCtrl implements Action {
	protected final DcMotorEx leftLift;
	protected final DcMotorEx rightLift;
	protected       int       currentPosition;
	protected       int       targetPosition;
	protected       String    tag;

	/**
	 * 构造函数，初始化目标升降机构电机和标签。
	 *
	 * @param leftLift 目标升降机构电机
	 */
	protected AbstractLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final DcMotorEx rightLift) {
		this.leftLift = leftLift;
		this.rightLift = rightLift;
		tag = "lift";
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
	public int getTargetPosition() {
		return targetPosition; // 返回目标位置
	}

	/**
	 * 设置目标位置(>=0)。
	 *
	 * @param targetPosition 目标位置
	 */
	public void setTargetPosition(final int targetPosition) {
		this.targetPosition = max(0, targetPosition); // 设置目标位置
	}
}
