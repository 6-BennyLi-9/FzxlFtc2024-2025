package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.betastudio.ftc.action.Action;

import java.util.Locale;

public class ServoCtrl implements Action {
	public final Servo  controlTarget;
	private      double targetPosition;
	private      String tag;

	public ServoCtrl(@NonNull final Servo target) {
		this(target, 0.5);
	}

	public ServoCtrl(@NonNull final Servo target, final double defaultPosition) {
		targetPosition = defaultPosition;
		controlTarget = target;
		tag = target.getDeviceName();
	}

	@Override
	public boolean run() {
		controlTarget.setPosition(targetPosition);
		return true;
	}

	@Override
	public String paramsString() {
		return String.format(Locale.SIMPLIFIED_CHINESE,"%s:%.3f", tag, targetPosition);
	}

	public void setTargetPosition(final double targetPosition) {
		this.targetPosition = targetPosition;
	}

	/**
	 * 不能一步到位，需要重复调试
	 *
	 * @param targetPosition 目标点位
	 * @param tolerance      最大更改量
	 */
	public void setTargetPositionTolerance(final double targetPosition, final double tolerance) {
		if (Math.abs(targetPosition - this.targetPosition) <= tolerance) {
			this.targetPosition = targetPosition;
		} else {
			changeTargetPositionBy(Math.signum(targetPosition - this.targetPosition) * tolerance);
		}
	}

	/**
	 * 不能一步到位，需要重复调试
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 */
	public void setTargetPositionSmooth(final double targetPosition, final double smoothVal) {
		setTargetPositionSmooth(targetPosition, smoothVal, 0);
	}

	/**
	 * 不能一步到位，需要重复调试
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 * @param minControlVal  最小调整数
	 */
	public void setTargetPositionSmooth(final double targetPosition, final double smoothVal, final double minControlVal) {
		if (Math.abs(targetPosition - this.targetPosition) <= minControlVal) {
			this.targetPosition = targetPosition;
		} else {
			changeTargetPositionBy(Math.max((targetPosition - this.targetPosition) * smoothVal, minControlVal));
		}
	}

	public void changeTargetPositionBy(final double targetPosition) {
		this.targetPosition += targetPosition;
	}

	/**
	 * 不能一步到位，需要重复调试
	 *
	 * @param targetPosition 目标点位
	 * @param tolerance      最大更改量
	 */
	public void changeTargetPositionTolerance(final double targetPosition, final double tolerance) {
		setTargetPositionTolerance(this.targetPosition + tolerance, tolerance);
	}

	/**
	 * 不能一步到位，需要重复调试
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 */
	public void changeTargetPositionSmooth(final double targetPosition, final double smoothVal) {
		changeTargetPositionSmooth(targetPosition, smoothVal, 0);
	}

	/**
	 * 不能一步到位，需要重复调试
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 * @param minControlVal  最小调整数
	 */
	public void changeTargetPositionSmooth(double targetPosition, final double smoothVal, final double minControlVal) {
		targetPosition += this.targetPosition;
		if (Math.abs(targetPosition - this.targetPosition) <= minControlVal) {
			this.targetPosition = targetPosition;
		} else {
			changeTargetPositionBy(Math.max((targetPosition - this.targetPosition) * smoothVal, minControlVal));
		}
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}
	public String getTag(){
		return this.tag;
	}
}
