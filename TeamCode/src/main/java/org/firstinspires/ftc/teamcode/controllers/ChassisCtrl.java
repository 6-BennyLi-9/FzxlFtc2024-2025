package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.DashboardCallable;
import org.firstinspires.ftc.teamcode.message.DriveBufMessage;

import java.util.Locale;

@Config
public class ChassisCtrl implements Action, DashboardCallable {
	public static double kS = 0.4, kF = - 0.4;
	public static ChassisCtrlMode mode = ChassisCtrlMode.FASTER_CONTROL;
	public final  DcMotorEx       leftFront, leftRear, rightFront, rightRear;
	private double pX, pY, pTurn;
	private String tag;

	public ChassisCtrl(final DcMotorEx leftFront, final DcMotorEx leftRear, final DcMotorEx rightFront, final DcMotorEx rightRear) {
		this.leftFront = leftFront;
		this.leftRear = leftRear;
		this.rightFront = rightFront;
		this.rightRear = rightRear;
	}

	@Override
	public boolean run() {
		switch (mode) {
			case FASTER_CONTROL:
				pX = resolveFunc(pX, kF); // 使用快速控制模式调整 pX
				pY = resolveFunc(pY, kF); // 使用快速控制模式调整 pY
				pTurn = resolveFunc(pTurn, kF); // 使用快速控制模式调整 pTurn
				break;
			case SLOWER_CONTROL:
				pX = resolveFunc(pX, kS); // 使用慢速控制模式调整 pX
				pY = resolveFunc(pY, kS); // 使用慢速控制模式调整 pY
				pTurn = resolveFunc(pTurn, kS); // 使用慢速控制模式调整 pTurn
				break;
			case NONE_SPECIFIED:
			default:
				break; // 没有指定模式时不做任何调整
		}

		leftFront.setPower(pY - pX - pTurn); // 设置左前电机功率
		leftRear.setPower(pY + pX - pTurn); // 设置左后电机功率
		rightFront.setPower(pY + pX + pTurn); // 设置右前电机功率
		rightRear.setPower(pY - pX + pTurn); // 设置右后电机功率
		return true; // 总是返回 true，表示运行成功
	}

	/**
	 * 返回控制器的参数字符串，包含当前的 x, y 和 heading 值。
	 *
	 * @return 参数字符串
	 */
	@Override
	public String paramsString() {
		return String.format(Locale.SIMPLIFIED_CHINESE, "%s:{x:%.3f,y%.4f,heading:%.3f}", tag, pX, pY, pTurn);
	}

	/**
	 * 设置电机功率，使用默认缓冲值 1。
	 *
	 * @param x     前进速度
	 * @param y     侧向速度
	 * @param turn  旋转速度
	 */
	public void setPowers(final double x, final double y, final double turn) {
		setPowers(x, y, turn, new DriveBufMessage(1)); // 调用重载方法，缓冲值为 1
	}

	/**
	 * 设置电机功率，使用指定的缓冲值。
	 *
	 * @param x      前进速度
	 * @param y      侧向速度
	 * @param turn   旋转速度
	 * @param bufVal 缓冲值
	 */
	public void setPowers(final double x, final double y, final double turn, @NonNull final DriveBufMessage bufVal) {
		pX = x * bufVal.bufX; // 设置 pX 为 x 乘以缓冲值
		pY = y * bufVal.bufY; // 设置 pY 为 y 乘以缓冲值
		pTurn = turn * bufVal.bufTurn; // 设置 pTurn 为 turn 乘以缓冲值
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
	public void send(@NonNull TelemetryPacket packet) {
		packet.put("drive-x", pX);
		packet.put("drive-y", pY);
		packet.put("drive-turn", pTurn);
	}

	/**
	 * 处理二次函数方程，将输入值映射到 [-1, 1] 范围内。
	 * @param val x
	 * @param k   a
	 * @return    处理后的函数值
	 */
	private double resolveFunc(double val, double k) {
		double result = k * val * val + (1 - k) * val;//y=ax^2+(1-a)x
		if (Math.signum(result) != Math.signum(val)) {//处理符号
			result = - result;
		}
		return result;
	}
}
