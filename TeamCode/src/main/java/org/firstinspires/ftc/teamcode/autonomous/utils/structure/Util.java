package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import static org.firstinspires.ftc.teamcode.util.HardwareConstants.*;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.action.packages.ActionPackage;
import org.firstinspires.ftc.teamcode.action.utils.StatementAction;
import org.firstinspires.ftc.teamcode.action.utils.ThreadedAction;

/**
 * 适配于自动程序的 {@code Robot}
 * @see Robot
 */
@SuppressWarnings("unused")
public class Util{
	private final ActionPackage thread;

	public Util(){
		thread=new ActionPackage();
		deviceInit();
	}

	public void deviceInit(){
		boxRst().armsIDLE().stopIO().scalesBack().openClip().runCached();
	}

	//PlaceOption
	public Util decant(){
		thread.add(new StatementAction(()-> place.setPosition(1)));
		return this;
	}
	public Util boxRst(){
		thread.add(new StatementAction(()-> place.setPosition(0)));
		return this;
	}

	//ClipOption
	public Util openClip(){
		thread.add(new StatementAction(()-> clip.setPosition(0)));
		return this;
	}
	public Util closeClip(){
		thread.add(new StatementAction(()-> clip.setPosition(0.5)));
		return this;
	}

	//IOTakesOption
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

	//ArmOption
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

	//ScaleOption
	public Util scalesProbe(){
		leftScale.setPosition(0.5);
		rightScale.setPosition(1);
		return this;
	}
	public Util scalesBack(){
		leftScale.setPosition(1);
		rightScale.setPosition(0.5);
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
