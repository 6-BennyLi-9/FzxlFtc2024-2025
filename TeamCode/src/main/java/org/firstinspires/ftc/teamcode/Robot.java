package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.clipOption;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.decant;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.flipArms;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.intakeSamples;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.liftDecantHigh;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.liftDecantLow;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.liftHighSuspend;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.liftIDLE;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.outtakeSamples;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.probe;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.suspend;
import static org.firstinspires.ftc.teamcode.GamepadRequestMemories.syncRequests;
import static org.firstinspires.ftc.teamcode.structure.ClipOption.ClipPositionTypes.close;
import static org.firstinspires.ftc.teamcode.structure.IOTakesOption.IOTakesPositionTypes.intake;
import static org.firstinspires.ftc.teamcode.structure.IOTakesOption.IOTakesPositionTypes.outtake;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.decantHigh;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.decantLow;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.highSuspend;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.highSuspendPrepare;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.idle;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.actions.packages.TaggedActionPackage;
import org.firstinspires.ftc.teamcode.structure.ArmOption;
import org.firstinspires.ftc.teamcode.structure.ClipOption;
import org.firstinspires.ftc.teamcode.structure.DriveOption;
import org.firstinspires.ftc.teamcode.structure.IOTakesOption;
import org.firstinspires.ftc.teamcode.structure.LiftOption;
import org.firstinspires.ftc.teamcode.structure.PlaceOption;
import org.firstinspires.ftc.teamcode.structure.ScaleOption;

/**
 * 申名时需要初始化{@link #registerGamepad(Gamepad, Gamepad)}
 */
public class Robot {
	@NonNull public static Gamepad gamepad1,gamepad2;
	public TaggedActionPackage thread;

	static {
		gamepad1=new Gamepad();
		gamepad2=new Gamepad();
	}

	public Robot(@NonNull final HardwareMap hardwareMap){
		HardwareConstants.sync(hardwareMap);
		thread =new TaggedActionPackage();
	}

	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2){
		Robot.gamepad1 =gamepad1;
		Robot.gamepad2 =gamepad2;
	}

	public void initActions(){
		thread.add("clip",ClipOption.init());
		thread.add("intake", IOTakesOption.IOtakes(IOTakesOption.IOTakesPositionTypes.idle));
		thread.add("lift", LiftOption.cloneController());
		thread.add("place", PlaceOption.init());
		thread.add("arm", ArmOption.init());
		thread.add("scale", ScaleOption.init());
	}

	public void operateThroughGamepad(){
		syncRequests();
		if(clipOption){
			thread.replace("clip", ClipOption.change());
		}

		if(intakeSamples &&!outtakeSamples){
			thread.replace("intake", IOTakesOption.IOtakes(intake));
		}else if(!intakeSamples && outtakeSamples){
			thread.replace("intake", IOTakesOption.IOtakes(outtake));
		}else {
			thread.replace("intake", IOTakesOption.IOtakes(IOTakesOption.IOTakesPositionTypes.idle));
		}

		if(liftIDLE
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()){
			if(PlaceOption.PlacePositionTypes.decant == PlaceOption.recent()){
				thread.replace("place",PlaceOption.idle());
			}
			if(close == ClipOption.recent()){
				thread.replace("clip",ClipOption.change());
			}
			LiftOption.sync(idle);
		}else if(liftDecantLow		&&	(idle == LiftOption.recent() ||LiftOption.decanting())
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()){
			if(ArmOption.isNotSafe()){
				thread.replace("arm",ArmOption.safe());
			}

			LiftOption.sync(decantLow);
		}else if(liftDecantHigh		&&	(idle == LiftOption.recent() ||LiftOption.decanting())
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()){
			if(ArmOption.isNotSafe()){
				thread.replace("arm",ArmOption.safe());
			}

			LiftOption.sync(decantHigh);
		} else if (liftHighSuspend 	&&	 idle == LiftOption.recent()
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()) {
			if(ArmOption.isNotSafe()){
				thread.replace("arm",ArmOption.safe());
			}

			LiftOption.sync(highSuspendPrepare);
		}

		if(decant && LiftOption.decanting()){
			thread.replace("place",PlaceOption.decant());
		}

		if(suspend && highSuspend == LiftOption.recent()){
			LiftOption.sync(highSuspend);
		}

		if(flipArms&& idle == LiftOption.recent()){
			thread.replace("arm",ArmOption.flip());
		}

		if(probe && idle == LiftOption.recent()){
			if(ScaleOption.ScalePosition.probe == ScaleOption.recent() && ArmOption.ArmPositionTypes.idle != ArmOption.recent()){
				thread.replace("arm",ArmOption.idle());
			}
			thread.replace("scale",ScaleOption.flip());
		}
	}

	public double driveBufPower=1;

	public void driveThroughGamepad(){
		driveBufPower=1+gamepad1.right_stick_y*0.5;
		thread.replace("drive", DriveOption.build(
				gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,
				driveBufPower
		));
	}

	public void runThread(){
		thread.run();
	}
}
