package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.clipOption;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.clipOptionRan;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.decant;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.flipArms;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.flipArmsRan;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.intakeSamples;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftDecantHigh;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftDecantLow;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftHighSuspend;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftIDLE;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.outtakeSamples;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.probe;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.probeRan;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.suspend;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.syncRequests;
import static org.firstinspires.ftc.teamcode.structure.ClipOption.ClipPositionTypes.close;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.decantHigh;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.decantLow;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.highSuspend;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.highSuspendPrepare;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.idle;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.action.PriorityAction;
import org.firstinspires.ftc.teamcode.action.packages.TaggedActionPackage;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.structure.ArmOption;
import org.firstinspires.ftc.teamcode.structure.ClipOption;
import org.firstinspires.ftc.teamcode.structure.DriveOption;
import org.firstinspires.ftc.teamcode.structure.IOTakesOption;
import org.firstinspires.ftc.teamcode.structure.LiftOption;
import org.firstinspires.ftc.teamcode.structure.PlaceOption;
import org.firstinspires.ftc.teamcode.structure.ScaleOption;

import java.util.Map;

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

	public Robot(){
		thread =new TaggedActionPackage();
	}

	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2){
		Robot.gamepad1 =gamepad1;
		Robot.gamepad2 =gamepad2;
	}

	public void initActions(){
		ArmOption.connect();
		ClipOption.connect();
		DriveOption.connect();
		IOTakesOption.connect();
		LiftOption.connect();
		PlaceOption.connect();
		ScaleOption.connect();

		thread.add("clip",ClipOption.cloneController());
		thread.add("intake", IOTakesOption.cloneController());
		thread.add("lift", LiftOption.cloneController());
		thread.add("place", PlaceOption.cloneController());
		thread.add("arm", ArmOption.cloneController());
		thread.add("scale", ScaleOption.cloneController());
		thread.add("drive", DriveOption.cloneAction());

		ArmOption.init();
		ClipOption.init();
		PlaceOption.init();
		ScaleOption.init();
	}

	public final void operateThroughGamepad(){
		syncRequests();
		if(clipOption && !clipOptionRan){
			ClipOption.change();
			clipOptionRan=true;
		}

		if(intakeSamples &&!outtakeSamples){
			IOTakesOption.intake();
		}else if(!intakeSamples && outtakeSamples){
			IOTakesOption.outtake();
		}else {
			IOTakesOption.idle();
		}

		if(liftIDLE){
			if(PlaceOption.PlacePositionTypes.decant == PlaceOption.recent()){
				PlaceOption.idle();
			}
			if(close == ClipOption.recent()){
				ClipOption.change();
			}
			LiftOption.sync(idle);
		}else if(liftDecantLow
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()){
			if(ArmOption.isNotSafe()){
				ArmOption.safe();
			}

			LiftOption.sync(decantLow);
		}else if(liftDecantHigh
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()){
			if(ArmOption.isNotSafe()){
				ArmOption.safe();
			}

			LiftOption.sync(decantHigh);
		} else if (liftHighSuspend
				&& ScaleOption.ScalePosition.back == ScaleOption.recent()) {
			if(ArmOption.isNotSafe()){
				ArmOption.safe();
			}

			LiftOption.sync(highSuspendPrepare);
		}

		if(decant && LiftOption.decanting()){
			PlaceOption.decant();
		}

		if(suspend && highSuspend == LiftOption.recent()){
			LiftOption.sync(highSuspend);
		}

		if(flipArms && !flipArmsRan
		   && idle == LiftOption.recent()){
			ArmOption.flip();
			flipArmsRan=true;
		}

		if(probe &&!probeRan
		   && idle == LiftOption.recent()){
			if(ScaleOption.ScalePosition.probe == ScaleOption.recent() && ArmOption.ArmPositionTypes.idle != ArmOption.recent()){
				ArmOption.idle();
			}
			ScaleOption.flip();
			probeRan=true;
		}
	}

	public static double driveBufPower=1,triggerBufFal=0.5;

	public final void driveThroughGamepad(){
		driveBufPower=1+gamepad1.right_stick_y*0.5;

		DriveOption.sync(
				gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,
				driveBufPower
		);

		if(gamepad1.left_bumper){
			DriveOption.additions(0,0,-0.1);
		}
		if(gamepad1.right_bumper){
			DriveOption.additions(0,0,0.1);
		}

		DriveOption.additions(0,0,gamepad1.right_trigger-gamepad1.left_trigger,triggerBufFal);
	}

	public void runThread(){
		thread.run();
	}

	public static final char[] printCode= "-\\|/".toCharArray();
	public static int updateTime;

	public void printThreadDebugs(){
		++updateTime;

		final String updateCode="["+printCode[updateTime%printCode.length]+"]";

		final Map<String, PriorityAction> map=thread.getActionMap();
		for (final Map.Entry<String, PriorityAction> entry : map.entrySet()) {
			final String s = entry.getKey();
			final PriorityAction a = entry.getValue();
			TelemetryClient.getInstance().changeData(s+"-action", updateCode
					+ a.getClass().getSimpleName() + ":" + a.paramsString());
		}
	}
}
