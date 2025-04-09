package org.firstinspires.ftc.teamcode.cores;

import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_IDLE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_INTAKE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_LEFT_ADDITION;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_SAFE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.CLAW_CLOSE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.CLAW_OPEN;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.CLIP_CLOSE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.CLIP_OPEN;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.LIFT_DECANT_HIGH;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.LIFT_DECANT_LOW;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.LIFT_SUSPEND;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.LIFT_SUSPEND_Lv1;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.LIFT_SUSPEND_PREPARE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.PLACE_DECANT;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.PLACE_IDLE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ROTATE_DEFAULT;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.SCALE_BACH;
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
import org.betastudio.ftc.action.builder.LinkedActionBuilder;
import org.betastudio.ftc.action.utils.AssembledAction;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.controllers.AbstractLiftCtrl;
import org.firstinspires.ftc.teamcode.controllers.DcAutoLiftCtrl;

/**
 * 适配于自动程序的 {@code RobotMng} ，修改电梯适配器参见 {@link #genLiftController(int)}
 *
 * @see RobotMng
 */
public class UtilsMng {
	private final LinkedActionBuilder builder;

	/**
	 * 构造函数，初始化actions列表并调用设备初始化方法。
	 */
	public UtilsMng() {
		builder = new LinkedActionBuilder();
		deviceInit();
	}

	/**
	 * 设备初始化方法，将旋转器设置到中间位置，并调用一系列动作重置设备状态。
	 */
	public void deviceInit() {
		builder.append(new StatementAction(() -> rotate.setPosition(0.79)));
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
	 *
	 * @param waitMillis 等待的毫秒数
	 */
	public void waitMs(final long waitMillis) {
		builder.append(new SleepingAction(waitMillis));
	}

	/**
	 * 添加一个动作到actions列表。
	 *
	 * @param action 要添加的动作
	 */
	public void addAction(final Action action) {
		builder.append(action);
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
		builder.append(new StatementAction(() -> rotate.setPosition(ROTATE_DEFAULT)));
	}

	/**
	 * 使旋转器向右转到指定位置。
	 *
	 * @param positionVal 要增加的位置值
	 */
	public void rotateRightTurn(final double positionVal) {
		builder.append(new StatementAction(() -> rotate.setPosition(rotate.getPosition() + positionVal)));
	}

	/**
	 * 将放置机构设置到倾倒位置。
	 */
	public void boxDecant() {
		builder.append(new StatementAction(() -> place.setPosition(PLACE_DECANT)));
	}

	/**
	 * 将放置机构重置到初始位置。
	 */
	public void boxRst() {
		builder.append(new StatementAction(() -> place.setPosition(PLACE_IDLE)));
	}

	/**
	 * 打开夹具。
	 */
	public void openClip() {
		builder.append(new StatementAction(() -> clip.setPosition(CLIP_OPEN)));
	}

	/**
	 * 关闭夹具。
	 */
	public void closeClip() {
		builder.append(new StatementAction(() -> clip.setPosition(CLIP_CLOSE)));
	}

	/**
	 * 关闭抓取器。
	 */
	public void closeClaw() {
		builder.append(new StatementAction(() -> claw.setPosition(CLAW_CLOSE)));
	}

	/**
	 * 打开抓取器。
	 */
	public void openClaw() {
		builder.append(new StatementAction(() -> claw.setPosition(CLAW_OPEN)));
	}

	/**
	 * 显示臂。
	 */
	public void armDisplay() {
		builder.append(new AssembledAction(new StatementAction(() -> leftArm.setPosition(ARM_INTAKE + ARM_LEFT_ADDITION)), new StatementAction(() -> rightArm.setPosition(ARM_INTAKE))));
	}

	/**
	 * 将臂设置为待命位置。
	 */
	public void armBack() {
		builder.append(new AssembledAction(new StatementAction(() -> leftArm.setPosition(ARM_IDLE + ARM_LEFT_ADDITION)), new StatementAction(() -> rightArm.setPosition(ARM_IDLE))));
	}

	/**
	 * 将臂移动到安全位置。
	 */
	public void armSafe() {
		builder.append(new AssembledAction(new StatementAction(() -> leftArm.setPosition(ARM_SAFE + ARM_LEFT_ADDITION)), new StatementAction(() -> rightArm.setPosition(ARM_SAFE))));
	}

	/**
	 * 使秤臂收回。
	 */
	public void scaleBack() {
		builder.append(new AssembledAction(new StatementAction(() -> leftScale.setPosition(1 - SCALE_BACH)), new StatementAction(() -> rightScale.setPosition(SCALE_BACH))));
	}

	/**
	 * 操作秤臂到指定位置。
	 *
	 * @param rightScalePosition 右侧秤臂的目标位置
	 */
	public void scaleOperate(double rightScalePosition) {
		rightScalePosition = min(0.35, max(rightScalePosition, 0));
		final double finalRightScalePosition = rightScalePosition;
		builder.append(new AssembledAction(new StatementAction(() -> leftScale.setPosition(1 - finalRightScalePosition)), new StatementAction(() -> rightScale.setPosition(finalRightScalePosition))));
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
		builder.append(genLiftController(0));
	}

	/**
	 * 将电梯升高到倾倒高位置。
	 */
	public void liftDecantHigh() {
		builder.append(genLiftController(LIFT_DECANT_HIGH));
	}

	/**
	 * 将电梯升高到倾倒低位置。
	 */
	public void liftDecantLow() {
		builder.append(genLiftController(LIFT_DECANT_LOW));
	}

	/**
	 * 准备将电梯升高到高悬停位置。
	 */
	public void liftSuspendHighPrepare() {
		builder.append(genLiftController(LIFT_SUSPEND_PREPARE));
	}

	/**
	 * 将电梯升高到高悬停位置。
	 */
	public void liftSuspendHigh() {
		builder.append(genLiftController(LIFT_SUSPEND));
	}

	/**
	 * 将电梯升高到一级悬停位置。
	 */
	public void liftSuspendLv1() {
		builder.append(genLiftController(LIFT_SUSPEND_Lv1));
	}

	/**
	 * 运行缓存的动作。
	 */
	public void runCached() {
		Actions.runAction(pack());
		builder.clear();
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
		final Action res = builder.store();
		builder.clear();
		return res;
	}
}
