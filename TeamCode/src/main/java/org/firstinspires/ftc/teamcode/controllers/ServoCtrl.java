package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.DashboardCallable;
import org.betastudio.ftc.telemetry.TelemetryItem;
import org.firstinspires.ftc.teamcode.message.TelemetryMessage;

import java.util.Locale;

/**
 * 通用的舵机控制类
 */
public class ServoCtrl implements Action, DashboardCallable {
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
		return String.format(Locale.SIMPLIFIED_CHINESE, "%s:%.3f", tag, targetPosition);
	}

	public void setTargetPosition(final double targetPosition) {
		this.targetPosition = targetPosition;
	}

	/**
	 * 不能一步到位，需要重复调用
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
	 * 不能一步到位，需要重复调用
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 */
	public void setTargetPositionSmooth(final double targetPosition, final double smoothVal) {
		setTargetPositionSmooth(targetPosition, smoothVal, 0);
	}

	/**
	 * 不能一步到位，需要重复调用
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 * @param tolerance      最小调整数
	 */
	public void setTargetPositionSmooth(final double targetPosition, final double smoothVal, final double tolerance) {
		if (Math.abs(targetPosition - this.targetPosition) <= tolerance) {
			this.targetPosition = targetPosition;
		} else {
			changeTargetPositionBy(Math.max(Math.abs(targetPosition - this.targetPosition) * smoothVal, tolerance) * Math.signum(targetPosition - this.targetPosition));
		}
	}

	public void changeTargetPositionBy(final double targetPosition) {
		this.targetPosition += targetPosition;
	}

	/**
	 * 不能一步到位，需要重复调用
	 *
	 * @param targetPosition 目标点位
	 * @param tolerance      最大更改量
	 */
	public void changeTargetPositionTolerance(final double targetPosition, final double tolerance) {
		setTargetPositionTolerance(this.targetPosition + tolerance, tolerance);
	}

	/**
	 * 不能一步到位，需要重复调用
	 *
	 * @param targetPosition 目标点位
	 * @param smoothVal      关于调控量的因数
	 */
	public void changeTargetPositionSmooth(final double targetPosition, final double smoothVal) {
		changeTargetPositionSmooth(targetPosition, smoothVal, 0);
	}

	/**
	 * 不能一步到位，需要重复调用
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

	public String getTag() {
		return this.tag;
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}

	@Override
	public void process(@NonNull final TelemetryMessage messageOverride) {
		messageOverride.add(new TelemetryItem(tag+"-target",targetPosition));
	}
}
