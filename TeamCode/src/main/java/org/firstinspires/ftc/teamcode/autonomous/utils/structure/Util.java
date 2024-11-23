package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import static org.firstinspires.ftc.teamcode.util.HardwareConstants.clip;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.intake;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.leftArm;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.leftScale;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.lift;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.place;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.rightArm;
import static org.firstinspires.ftc.teamcode.util.HardwareConstants.rightScale;

import org.firstinspires.ftc.teamcode.action.packages.ActionPackage;
import org.firstinspires.ftc.teamcode.action.utils.StatementAction;
import org.firstinspires.ftc.teamcode.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.LiftOp;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.Robot;

/**
 * 适配于自动程序的 {@code Robot} ，修改电梯适配器参见 {@link #liftControllerGenerator(long)}}
 * @see Robot
 */
@SuppressWarnings({"unused","UnusedReturnValue"})
public class Util{
	private final ActionPackage thread;

	public Util(){
		thread=new ActionPackage();
		deviceInit();
	}

	public void deviceInit(){
		boxRst().armsIDLE().stopIO().scalesBack().openClip().runCached();
	}

	//PlaceOp
	public Util decant(){
		thread.add(new StatementAction(()-> place.setPosition(1)));
		return this;
	}
	public Util boxRst(){
		thread.add(new StatementAction(()-> place.setPosition(0)));
		return this;
	}

	//ClipOp
	public Util openClip(){
		thread.add(new StatementAction(()-> clip.setPosition(0)));
		return this;
	}
	public Util closeClip(){
		thread.add(new StatementAction(()-> clip.setPosition(0.5)));
		return this;
	}

	//TakeOp
	public Util intake(){
		thread.add(new StatementAction(()-> intake.setPosition(1)));
		return this;
	}
	public Util outtake(){
		thread.add(new StatementAction(()-> intake.setPosition(0)));
		return this;
	}
	public Util stopIO(){
		thread.add(new StatementAction(()-> intake.setPosition(0.5)));
		return this;
	}

	//ArmOp
	public Util displayArms(){
		thread.add(new ThreadedAction(
				new StatementAction(()-> leftArm.setPosition(0.3)),
				new StatementAction(()-> rightArm.setPosition(0.3))
		));
		return this;
	}
	public Util armsIDLE(){
		thread.add(new ThreadedAction(
				new StatementAction(()-> leftArm.setPosition(0.86)),
				new StatementAction(()-> rightArm.setPosition(0.86))
		));
		return this;
	}
	public Util armsToSafePosition(){
		thread.add(new ThreadedAction(
				new StatementAction(()-> leftArm.setPosition(0.75)),
				new StatementAction(()-> rightArm.setPosition(0.75))
		));
		return this;
	}

	//ScaleOp
	public Util scalesProbe(){
		thread.add(new ThreadedAction(
				new StatementAction(()-> leftScale.setPosition(0.5)),
				new StatementAction(()-> rightScale.setPosition(1))
		));
		return this;
	}
	public Util scalesBack(){
		thread.add(new ThreadedAction(
				new StatementAction(()-> leftScale.setPosition(1)),
				new StatementAction(()-> rightScale.setPosition(0.5))
		));
		return this;
	}

	//lift
	protected LiftCtrl liftControllerGenerator(final long target){
		return new ClassicLiftCtrl(lift,target);
	}

	public Util liftDown(){
		thread.add(liftControllerGenerator(0));
		return this;
	}
	public Util liftDecantHigh(){
		thread.add(liftControllerGenerator(LiftOp.decantHigh));
		return this;
	}
	public Util liftDecantLow(){
		thread.add(liftControllerGenerator(LiftOp.decantLow));
		return this;
	}
	public Util liftSuspendHighPrepare(){
		thread.add(liftControllerGenerator(LiftOp.highSuspendPrepare));
		return this;
	}
	public Util liftSuspendHigh(){
		thread.add(liftControllerGenerator(LiftOp.highSuspend));
		return this;
	}

	//integral
	public Util integralIntakes(){
		return displayArms().intake();
	}
	public Util integralIntakesEnding(){
		return boxRst().armsIDLE().intake();
	}
	public Util integralLiftUpPrepare(){
		return armsToSafePosition();
	}
	public Util integralLiftDownPrepare(){
		return armsIDLE().boxRst();
	}

	public void runCached(){
		thread.runTillEnd();
	}
}
