package org.firstinspires.ftc.teamcode.cores;

import org.betastudio.ftc.action.*;
import org.betastudio.ftc.action.utils.*;
import org.firstinspires.ftc.teamcode.*;
import org.firstinspires.ftc.teamcode.controllers.*;

import java.util.*;

import static java.lang.Math.*;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.*;

/**
 * 适配于自动程序的 {@code RobotMng} ，修改电梯适配器参见 {@link #genLiftController(int)}
 *
 * @see RobotMng
 */
public class UtilsMng {
	private final List <Action> actions;

	/**
	 * 构造函数，初始化actions列表并调用设备初始化方法。
	 */
	public UtilsMng() {
		actions = new LinkedList <>();
		deviceInit();
	}

	/**
	 * 设备初始化方法，将旋转器设置到中间位置，并调用一系列动作重置设备状态。
	 */
	public void deviceInit() {
		actions.add(new StatementAction(() -> rotate.setPosition(0.79)));
		boxRst();
		armSafe();
		openClaw();
		scaleBack();
		closeClip();
		liftDown();
		rotateToMid();
		runCached();
	}

	/**
	 * 添加一个等待指定时间的动作。
	 * @param waitMillis 等待的毫秒数
	 */
	public void waitMs(final long waitMillis) {
		actions.add(new SleepingAction(waitMillis));
	}

	/**
	 * 添加一个动作到actions列表。
	 * @param action 要添加的动作
	 */
	public void addAction(final Action action) {
		actions.add(action);
	}

	/**
	 * 将所有电机的功率设置为0，停止电机。
	 */
	public void rstMotors() {
		leftFront.setPower(0);
		leftRear.setPower(0);
		rightFront.setPower(0);
		rightRear.setPower(0);
	}

	/**
	 * 将旋转器设置到中间位置。
	 */
	public void rotateToMid() {
		actions.add(new StatementAction(() -> rotate.setPosition(0.79)));
	}

	/**
	 * 使旋转器向右转到指定位置。
	 * @param positionVal 要增加的位置值
	 */
	public void rotateRightTurn(final double positionVal) {
		actions.add(new StatementAction(() -> rotate.setPosition(rotate.getPosition() + positionVal)));
	}

	/**
	 * 将放置机构设置到倾倒位置。
	 */
	public void boxDecant() {
		actions.add(new StatementAction(() -> place.setPosition(1)));
	}

	/**
	 * 将放置机构重置到初始位置。
	 */
	public void boxRst() {
		actions.add(new StatementAction(() -> place.setPosition(0)));
	}

	/**
	 * 打开夹具。
	 */
	public void openClip() {
		actions.add(new StatementAction(() -> clip.setPosition(0)));
	}

	/**
	 * 关闭夹具。
	 */
	public void closeClip() {
		actions.add(new StatementAction(() -> clip.setPosition(0.5)));
	}

	/**
	 * 关闭抓取器。
	 */
	public void closeClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.44)));
	}

	/**
	 * 打开抓取器。
	 */
	public void openClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.66)));
	}

	/**
	 * 显示臂。
	 */
	public void armDisplay() {
		actions.add(new AssembledAction(new StatementAction(() -> leftArm.setPosition(0.1625)), new StatementAction(() -> rightArm.setPosition(0.0825))));
	}

	/**
	 * 将臂设置为待命位置。
	 */
	public void armBack() {
		actions.add(new AssembledAction(new StatementAction(() -> leftArm.setPosition(0.87)), new StatementAction(() -> rightArm.setPosition(0.79))));
	}

	/**
	 * 将臂移动到安全位置。
	 */
	public void armSafe() {
		actions.add(new AssembledAction(new StatementAction(() -> leftArm.setPosition(0.69)), new StatementAction(() -> rightArm.setPosition(0.61))));
	}

	/**
	 * 使秤臂探出。
	 */
	public void scaleProbe() {
		actions.add(new AssembledAction(new StatementAction(() -> leftScale.setPosition(0.65)), new StatementAction(() -> rightScale.setPosition(0.35))));
	}

	/**
	 * 使秤臂收回。
	 */
	public void scaleBack() {
		actions.add(new AssembledAction(new StatementAction(() -> leftScale.setPosition(1)), new StatementAction(() -> rightScale.setPosition(0))));
	}

	/**
	 * 操作秤臂到指定位置。
	 * @param rightScalePosition 右侧秤臂的目标位置
	 */
	public void scaleOperate(double rightScalePosition) {
		rightScalePosition = min(0.35, max(rightScalePosition, 0));
		final double finalRightScalePosition = rightScalePosition;
		actions.add(new AssembledAction(new StatementAction(() -> leftScale.setPosition(1 - finalRightScalePosition)), new StatementAction(() -> rightScale.setPosition(finalRightScalePosition))));
	}

	/**
	 * 生成一个电梯控制器。
	 *
	 * @param target 目标位置
	 * @return 电梯控制器对象
	 */
	protected AbstractLiftCtrl genLiftController(final int target) {
		return new DcAutoLiftCtrl(leftLift, rightLift, target);
	}

	/**
	 * 将电梯降到底部。
	 */
	public void liftDown() {
		actions.add(genLiftController(0));
	}

	/**
	 * 将电梯升高到倾倒高位置。
	 */
	public void liftDecantHigh() {
		actions.add(genLiftController(HardwareConfigures.LIFT_DECANT_HIGH));
	}

	/**
	 * 将电梯升高到倾倒低位置。
	 */
	public void liftDecantLow() {
		actions.add(genLiftController(HardwareConfigures.LIFT_DECANT_LOW));
	}

	/**
	 * 准备将电梯升高到高悬停位置。
	 */
	public void liftSuspendHighPrepare() {
		actions.add(genLiftController(HardwareConfigures.LIFT_SUSPEND_PREPARE));
	}

	/**
	 * 将电梯升高到高悬停位置。
	 */
	public void liftSuspendHigh() {
		actions.add(genLiftController(HardwareConfigures.LIFT_SUSPEND));
	}

	/**
	 * 将电梯升高到一级悬停位置。
	 */
	public void liftSuspendLv1() {
		actions.add(genLiftController(HardwareConfigures.LIFT_SUSPEND_Lv1));
	}

	/**
	 * 运行缓存的动作。
	 */
	public void runCached() {
		Actions.runAction(pack());
		actions.clear();
	}

	/**
	 * 将缓存的动作作为线程运行。
	 */
	public void runAsThread() {
		Global.service.execute(this::runCached);
	}

	/**
	 * 会自动清除缓存动作
	 *
	 * @return 将缓存动作打包后的 {@link LinkedAction}
	 */
	public Action pack() {
		final LinkedAction res = new LinkedAction(new LinkedList <>(actions));
		actions.clear();
		return res;
	}
}
