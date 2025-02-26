package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.util.message.DriveBufMsg;
import org.betastudio.ftc.util.message.DriveMsg;
import org.betastudio.ftc.util.message.TelemetryMsg;

import java.util.Locale;

@Config
public strictfp class ChassisCtrl implements Action, Interfaces.DashboardCallable, Interfaces.MessagesProcessRequired <DriveMsg> {
	public static final double kS = 1;
	public static final double kF = - 1;
	public static final double maxControlPower = 1.3;
	public static final double smoothConfig    = 0.9;
	public static final double vS              = 0.6;
	public static       ChassisCtrlMode mode         = ChassisCtrlMode.FASTER_CONTROL;
	public final  DcMotorEx       leftFront, leftRear, rightFront, rightRear;
	private double pX, pY, pTurn, vX, vY, vTurn;
	private String tag;

	public ChassisCtrl(final DcMotorEx leftFront, final DcMotorEx leftRear, final DcMotorEx rightFront, final DcMotorEx rightRear) {
		this.leftFront = leftFront;
		this.leftRear = leftRear;
		this.rightFront = rightFront;
		this.rightRear = rightRear;
	}

	/**
	 * 处理二次函数方程，将输入值映射到 [-1, 1] 范围内。
	 *
	 * @param val x
	 * @param k   a
	 * @return 处理后的函数值
	 */
	private static double resolveFunc(final double val, final double k) {
		double absVal = Math.abs(val);
		double result = k * absVal * absVal + (1 - k) * absVal;//y=ax^2+(1-a)x
		if (Math.signum(result) != Math.signum(val)) {//处理符号
			result = - result;
		}
		return result;
	}

	public static void setDcMotorPowerSmooth(@NonNull final DcMotorEx motor, final double power, final double smoothConfig) {
		final double delta = power - motor.getPower();
		motor.setPower(motor.getPower() + delta * smoothConfig);
	}

	@Override
	public boolean activate() {
		//defaults:
		vX = pX;
		vY = pY;
		vTurn = pTurn;
		switch (mode) {
			case FASTER_CONTROL:
				vX = resolveFunc(pX, kF); // 使用快速控制模式调整 pX
				vY = resolveFunc(pY, kF); // 使用快速控制模式调整 pY
				vTurn = resolveFunc(pTurn, kF); // 使用快速控制模式调整 pTurn
				break;
			case SLOWER_CONTROL:
				vX = resolveFunc(pX, kS) * vS; // 使用慢速控制模式调整 pX
				vY = resolveFunc(pY, kS) * vS; // 使用慢速控制模式调整 pY
				vTurn = resolveFunc(pTurn, kS * vS); // 使用慢速控制模式调整 pTurn
				break;
			case NONE_SPECIFIED:
			default:
				break; // 没有指定模式时不做任何调整
		}

		double pLF = vY - vX - vTurn;
		double pLR = vY + vX - vTurn;
		double pRF = vY + vX + vTurn;
		double pRR = vY - vX + vTurn;

		if (Math.abs(pLF) > maxControlPower || Math.abs(pLR) > maxControlPower || Math.abs(pRF) > maxControlPower || Math.abs(pRR) > maxControlPower) {
			final double buf = Math.max(Math.max(Math.abs(pLF), Math.abs(pLR)), Math.max(Math.abs(pRF), Math.abs(pRR))) / maxControlPower;
			pLF /= buf;
			pLR /= buf;
			pRF /= buf;
			pRR /= buf;
		}

		setDcMotorPowerSmooth(leftFront, pLF, smoothConfig);
		setDcMotorPowerSmooth(leftRear, pLR, smoothConfig);
		setDcMotorPowerSmooth(rightFront, pRF, smoothConfig);
		setDcMotorPowerSmooth(rightRear, pRR, smoothConfig);

		return true; // 总是返回 true，表示运行成功
	}

	/**
	 * 返回控制器的参数字符串，包含当前的 x, y 和 heading 值。
	 *
	 * @return 参数字符串
	 */
	@Override
	public String paramsString() {
		return String.format(Locale.SIMPLIFIED_CHINESE, "%s:{x:%.3f,y%.4f,heading:%.3f}", tag, vX, vY, vTurn);
	}

	/**
	 * 设置电机功率，使用默认缓冲值 1。
	 *
	 * @param x    前进速度
	 * @param y    侧向速度
	 * @param turn 旋转速度
	 */
	public void setPowers(final double x, final double y, final double turn) {
		setPowers(x, y, turn, new DriveBufMsg(1)); // 调用重载方法，缓冲值为 1
	}

	/**
	 * 设置电机功率，使用指定的缓冲值。
	 *
	 * @param x      前进速度
	 * @param y      侧向速度
	 * @param turn   旋转速度
	 * @param bufVal 缓冲值
	 */
	public void setPowers(final double x, final double y, final double turn, @NonNull final DriveBufMsg bufVal) {
		pX = x * bufVal.valX; // 设置 pX 为 x 乘以缓冲值
		pY = y * bufVal.valY; // 设置 pY 为 y 乘以缓冲值
		pTurn = turn * bufVal.valTurn; // 设置 pTurn 为 turn 乘以缓冲值
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

	@Override
	public void sendMsg(@NonNull final DriveMsg message) {
		setPowers(message.valX, message.valY, message.valTurn);
	}

	@Override
	public DriveMsg callMsg() {
		return new DriveMsg(pX, pY, pTurn);
	}

	@Override
	public void process(@NonNull final TelemetryMsg messageOverride) {
		messageOverride.add(new TelemetryItem("vX", vX));
		messageOverride.add(new TelemetryItem("vY", vY));
		messageOverride.add(new TelemetryItem("vTurn", vTurn));
	}
}
