package org.firstinspires.ftc.cores;

import static org.firstinspires.ftc.teamcode.HardwareDatabase.claw;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.clip;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftArm;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftFront;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftRear;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.leftScale;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.lift;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.place;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightArm;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightFront;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightRear;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rightScale;
import static org.firstinspires.ftc.teamcode.HardwareDatabase.rotate;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.cores.autonomous.structure.DcAutoLiftCtrl;
import org.firstinspires.ftc.cores.structure.LiftOp;
import org.firstinspires.ftc.teamcode.controllers.LiftCtrl;

import java.util.LinkedList;

/**
 * 适配于自动程序的 {@code RobotMng} ，修改电梯适配器参见 {@link #liftControllerGenerator(long)}}
 *
 * @see RobotMng
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class UtilMng {
	private final LinkedList <Action> actions;

	public UtilMng() {
		actions = new LinkedList <>();
		deviceInit();
	}

	public void deviceInit() {
		actions.add(new StatementAction(() -> rotate.setPosition(0.79)));
		boxRst().armsToSafePosition().openClaw().scalesBack().closeClip().liftDown().runCached();
	}

	public UtilMng waitMs(final long waitMillis) {
		actions.add(new SleepingAction(waitMillis));
		return this;
	}

	public UtilMng addAction(final Action action) {
		actions.add(action);
		return this;
	}

	public void rstMotors(){
		leftFront.setPower(0);
		leftRear.setPower(0);
		rightFront.setPower(0);
		rightRear.setPower(0);
	}

	//rotate
	public UtilMng rotateToMid() {
		actions.add(new StatementAction(() -> rotate.setPosition(0.79)));
		return this;
	}

	public UtilMng rotateRightTurn(final double positionVal) {
		actions.add(new StatementAction(() -> rotate.setPosition(rotate.getPosition() + positionVal)));
		return this;
	}

	//PlaceOp
	public UtilMng decant() {
		actions.add(new StatementAction(() -> place.setPosition(1)));
		return this;
	}

	public UtilMng boxRst() {
		actions.add(new StatementAction(() -> place.setPosition(0)));
		return this;
	}

	//ClipOp
	public UtilMng openClip() {
		actions.add(new StatementAction(() -> clip.setPosition(0)));
		return this;
	}

	public UtilMng closeClip() {
		actions.add(new StatementAction(() -> clip.setPosition(0.5)));
		return this;
	}

	//ClawOp
	public UtilMng closeClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.45)));
		return this;
	}

	public UtilMng openClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.66)));
		return this;
	}

	//ArmOp
	public UtilMng displayArms() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.1625)), new StatementAction(() -> rightArm.setPosition(0.0825))));
		return this;
	}

	public UtilMng armsIDLE() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.87)), new StatementAction(() -> rightArm.setPosition(0.79))));
		return this;
	}

	public UtilMng armsToSafePosition() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.69)), new StatementAction(() -> rightArm.setPosition(0.61))));
		return this;
	}

	//ScaleOp
	public UtilMng scalesProbe() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(0.5)), new StatementAction(() -> rightScale.setPosition(1))));
		return this;
	}

	public UtilMng scalesBack() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(1)), new StatementAction(() -> rightScale.setPosition(0.5))));
		return this;
	}

	public UtilMng scaleOperate(double rightScalePosition) {
		rightScalePosition = Math.max(rightScalePosition, 0.58);
		final double finalRightScalePosition = rightScalePosition;
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(1.5 - finalRightScalePosition)), new StatementAction(() -> rightScale.setPosition(finalRightScalePosition))));
		return this;
	}

	//lift
	protected LiftCtrl liftControllerGenerator(final long target) {
		return new DcAutoLiftCtrl(lift, target);
	}

	public UtilMng liftDown() {
		actions.add(liftControllerGenerator(0));
		return this;
	}

	public UtilMng liftDecantHigh() {
		actions.add(liftControllerGenerator(LiftOp.decantHigh));
		return this;
	}

	public UtilMng liftDecantLow() {
		actions.add(liftControllerGenerator(LiftOp.decantLow));
		return this;
	}

	public UtilMng liftSuspendHighPrepare() {
		actions.add(liftControllerGenerator(LiftOp.highSuspendPrepare));
		return this;
	}

	public UtilMng liftSuspendHigh() {
		actions.add(liftControllerGenerator(LiftOp.highSuspend));
		return this;
	}

	public UtilMng liftSuspendLv1() {
		actions.add(liftControllerGenerator(LiftOp.suspendLv1));
		return this;
	}

	//integral
	public UtilMng integralIntakes() {
		return openClaw().rotateToMid();
	}

	public UtilMng integralIntakesEnding() {
		return boxRst().closeClaw().waitMs(250).armsIDLE().scalesBack().rotateToMid();
	}

	public UtilMng integralLiftUpPrepare() {
		return armsToSafePosition();
	}

	public UtilMng integralLiftDownPrepare() {
		return boxRst().armsToSafePosition();
	}

	public void runCached() {
		Actions.runAction(new LinkedAction(actions));
		actions.clear();
	}

	public void runAsThread() {
		Global.threadManager.add(saveCachedAsThread());
	}

	public Thread saveCachedAsThread() {
		return new Thread(this::runCached);
	}
}
