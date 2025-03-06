package org.firstinspires.ftc.teamcode.cores;

import static org.firstinspires.ftc.teamcode.HardwareDatabase.claw;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.clip;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftArm;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftFront;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftLift;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftRear;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftScale;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.place;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightArm;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightFront;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightLift;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightRear;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightScale;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rotate;
import static java.lang.Math.max;
import static java.lang.Math.min;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.controllers.DcAutoLiftCtrl;
import org.firstinspires.ftc.teamcode.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.cores.structure.LiftOp;

import java.util.LinkedList;
import java.util.List;

/**
 * 适配于自动程序的 {@code RobotMng} ，修改电梯适配器参见 {@link #liftControllerGenerator(long)}
 *
 * @see RobotMng
 */
@SuppressWarnings("UnusedReturnValue")
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
		boxRst().armsToSafePosition().openClaw().scalesBack().closeClip().liftDown().runCached();
	}

	/**
	 * 添加一个等待指定时间的动作。
	 *
	 * @param waitMillis 等待的毫秒数
	 * @return 当前对象
	 */
	public UtilsMng waitMs(final long waitMillis) {
		actions.add(new SleepingAction(waitMillis));
		return this;
	}

	/**
	 * 添加一个动作到actions列表。
	 *
	 * @param action 要添加的动作
	 * @return 当前对象
	 */
	public UtilsMng addAction(final Action action) {
		actions.add(action);
		return this;
	}

	/**
	 * 添加一个StatementAction动作。
	 *
	 * @param r Runnable对象，代表要执行的语句
	 * @return 当前对象
	 */
	public UtilsMng addStatement(final Runnable r) {
		actions.add(new StatementAction(r));
		return this;
	}

	/**
	 * 添加一个等待线程结束的动作。
	 *
	 * @param t 要等待的线程
	 * @return 当前对象
	 */
	public UtilsMng joinThread(final Thread t) {
		actions.add(new StatementAction(() -> Local.waitForVal(t::isInterrupted, true)));
		return this;
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
	 *
	 * @return 当前对象
	 */
	public UtilsMng rotateToMid() {
		actions.add(new StatementAction(() -> rotate.setPosition(0.79)));
		return this;
	}

	/**
	 * 使旋转器向右转到指定位置。
	 *
	 * @param positionVal 要增加的位置值
	 * @return 当前对象
	 */
	public UtilsMng rotateRightTurn(final double positionVal) {
		actions.add(new StatementAction(() -> rotate.setPosition(rotate.getPosition() + positionVal)));
		return this;
	}

	/**
	 * 将放置机构设置到倾倒位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng decant() {
		actions.add(new StatementAction(() -> place.setPosition(1)));
		return this;
	}

	/**
	 * 将放置机构重置到初始位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng boxRst() {
		actions.add(new StatementAction(() -> place.setPosition(0)));
		return this;
	}

	/**
	 * 打开夹具。
	 *
	 * @return 当前对象
	 */
	public UtilsMng openClip() {
		actions.add(new StatementAction(() -> clip.setPosition(0)));
		return this;
	}

	/**
	 * 关闭夹具。
	 *
	 * @return 当前对象
	 */
	public UtilsMng closeClip() {
		actions.add(new StatementAction(() -> clip.setPosition(0.5)));
		return this;
	}

	/**
	 * 关闭抓取器。
	 *
	 * @return 当前对象
	 */
	public UtilsMng closeClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.44)));
		return this;
	}

	/**
	 * 打开抓取器。
	 *
	 * @return 当前对象
	 */
	public UtilsMng openClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.66)));
		return this;
	}

	/**
	 * 显示臂。
	 *
	 * @return 当前对象
	 */
	public UtilsMng displayArms() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.1625)), new StatementAction(() -> rightArm.setPosition(0.0825))));
		return this;
	}

	/**
	 * 将臂设置为待命位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng armsIDLE() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.87)), new StatementAction(() -> rightArm.setPosition(0.79))));
		return this;
	}

	/**
	 * 将臂移动到安全位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng armsToSafePosition() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.69)), new StatementAction(() -> rightArm.setPosition(0.61))));
		return this;
	}

	/**
	 * 使秤臂探出。
	 *
	 * @return 当前对象
	 */
	public UtilsMng scalesProbe() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(0.65)), new StatementAction(() -> rightScale.setPosition(0.35))));
		return this;
	}

	/**
	 * 使秤臂收回。
	 *
	 * @return 当前对象
	 */
	public UtilsMng scalesBack() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(1)), new StatementAction(() -> rightScale.setPosition(0))));
		return this;
	}

	/**
	 * 操作秤臂到指定位置。
	 *
	 * @param rightScalePosition 右侧秤臂的目标位置
	 * @return 当前对象
	 */
	public UtilsMng scaleOperate(double rightScalePosition) {
		rightScalePosition = min(0.35, max(rightScalePosition, 0));
		final double finalRightScalePosition = rightScalePosition;
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(1 - finalRightScalePosition)), new StatementAction(() -> rightScale.setPosition(finalRightScalePosition))));
		return this;
	}

	/**
	 * 生成一个电梯控制器。
	 *
	 * @param target 目标位置
	 * @return 电梯控制器对象
	 */
	protected LiftCtrl liftControllerGenerator(final long target) {
		return new DcAutoLiftCtrl(leftLift, rightLift, target);
	}

	/**
	 * 将电梯降到底部。
	 *
	 * @return 当前对象
	 */
	public UtilsMng liftDown() {
		actions.add(liftControllerGenerator(0));
		return this;
	}

	/**
	 * 将电梯升高到倾倒高位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng liftDecantHigh() {
		actions.add(liftControllerGenerator(LiftOp.decantHigh));
		return this;
	}

	/**
	 * 将电梯升高到倾倒低位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng liftDecantLow() {
		actions.add(liftControllerGenerator(LiftOp.decantLow));
		return this;
	}

	/**
	 * 准备将电梯升高到高悬停位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng liftSuspendHighPrepare() {
		actions.add(liftControllerGenerator(LiftOp.highSuspendPrepare));
		return this;
	}

	/**
	 * 将电梯升高到高悬停位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng liftSuspendHigh() {
		actions.add(liftControllerGenerator(LiftOp.highSuspend));
		return this;
	}

	/**
	 * 将电梯升高到一级悬停位置。
	 *
	 * @return 当前对象
	 */
	public UtilsMng liftSuspendLv1() {
		actions.add(liftControllerGenerator(LiftOp.suspendLv1));
		return this;
	}

	/**
	 * 集成动作：初始化抓取机构。
	 *
	 * @return 当前对象
	 */
	public UtilsMng integralIntakes() {
		return openClaw().rotateToMid();
	}

	/**
	 * 集成动作：抓取机构结束动作。
	 *
	 * @return 当前对象
	 */
	public UtilsMng integralIntakesEnding() {
		return boxRst().closeClaw().waitMs(250).armsIDLE().scalesBack().rotateToMid();
	}

	/**
	 * 集成动作：准备将电梯升高。
	 *
	 * @return 当前对象
	 */
	public UtilsMng integralLiftUpPrepare() {
		return armsToSafePosition();
	}

	/**
	 * 集成动作：准备将电梯降低。
	 *
	 * @return 当前对象
	 */
	public UtilsMng integralLiftDownPrepare() {
		return boxRst().armsToSafePosition();
	}

	/**
	 * 运行缓存的动作。
	 */
	public void runCached() {
		Actions.runAction(new LinkedAction(actions));
		actions.clear();
	}

	/**
	 * 将缓存的动作作为线程运行。
	 */
	public void runAsThread() {
		Global.service.execute(saveCachedAsThread());
	}

	/**
	 * 保存缓存的动作到线程。
	 *
	 * @return 包含缓存动作的新线程
	 */
	public Runnable saveCachedAsThread() {
		return this::runCached;
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
