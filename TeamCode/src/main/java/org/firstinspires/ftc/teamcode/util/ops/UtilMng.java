package org.firstinspires.ftc.teamcode.util.ops;

import static org.firstinspires.ftc.teamcode.util.HardwareConstants.claw;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.clip;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.leftArm;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.leftScale;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.lift;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.place;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.rightArm;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.rightScale;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.rotate;

import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.action.Actions;
import org.firstinspires.ftc.teamcode.action.utils.LinkedAction;
import org.firstinspires.ftc.teamcode.action.utils.StatementAction;
import org.firstinspires.ftc.teamcode.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.autonomous.structure.DcAutoLiftCtrl;
import org.firstinspires.ftc.teamcode.structure.LiftOp;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.RobotMng;

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
		actions.add(new StatementAction(()->rotate.setPosition(0.79)));
		boxRst().armsToSafePosition().openClaw().scalesBack().closeClip().liftDown().runCached();
	}

	public UtilMng waitMs(long waitMillis) {
		actions.add(new StatementAction(() -> {
			try {
				Thread.sleep(waitMillis);
			} catch (InterruptedException ignore) {}
		}));
		return this;
	}

	//rotate
	public UtilMng rotateToMid(){
		actions.add(new StatementAction(()->rotate.setPosition(0.79)));
		return this;
	}
	public UtilMng rotateRightTurn(double positionVal){
		actions.add(new StatementAction(()->rotate.setPosition(rotate.getPosition()+positionVal)));
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
		actions.add(new StatementAction(() -> claw.setPosition(0.44)));
		return this;
	}
	public UtilMng openClaw() {
		actions.add(new StatementAction(() -> claw.setPosition(0.65)));
		return this;
	}

	//ArmOp
	public UtilMng displayArms() {
		actions.add(new ThreadedAction(new StatementAction(() -> leftArm.setPosition(0.19)), new StatementAction(() -> rightArm.setPosition(0.11))));
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
	public UtilMng scaleOperate(double rightScalePosition){
		rightScalePosition = Math.min(Math.max(rightScalePosition, 0.58), 0.92);
		double finalRightScalePosition = rightScalePosition;
		actions.add(new ThreadedAction(new StatementAction(() -> leftScale.setPosition(1.5 - finalRightScalePosition)),
				new StatementAction(() -> rightScale.setPosition(finalRightScalePosition))));
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
	public UtilMng liftSuspendLv1(){
		actions.add(liftControllerGenerator(LiftOp.suspendLv1));
		return this;
	}

	//integral
	public UtilMng integralIntakes() {
		return displayArms().openClaw().rotateToMid();
	}
	public UtilMng integralIntakesEnding() {
		return boxRst().closeClaw().waitMs(50).armsIDLE().scalesBack().rotateToMid();
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
		saveCachedAsThread().start();
	}
	public Thread saveCachedAsThread() {
		return new Thread(this::runCached);
	}
}
