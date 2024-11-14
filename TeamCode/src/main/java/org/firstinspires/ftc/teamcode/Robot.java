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
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.decantHigh;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.decantLow;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.highSuspend;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.highSuspendPrepare;
import static org.firstinspires.ftc.teamcode.structure.LiftOption.LiftPositionTypes.idle;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.actions.PriorityAction;
import org.firstinspires.ftc.teamcode.actions.packages.TaggedActionPackage;
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

	public Robot(@NonNull final HardwareMap hardwareMap){
		HardwareConstants.sync(hardwareMap);
		thread =new TaggedActionPackage();
	}

	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2){
		Robot.gamepad1 =gamepad1;
		Robot.gamepad2 =gamepad2;
	}

	public void initActions(){
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

	public void operateThroughGamepad(){
		syncRequests();
		if(clipOption){
			ClipOption.change();
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

		if(flipArms&& idle == LiftOption.recent()){
			ArmOption.flip();
		}

		if(probe && idle == LiftOption.recent()){
			if(ScaleOption.ScalePosition.probe == ScaleOption.recent() && ArmOption.ArmPositionTypes.idle != ArmOption.recent()){
				ArmOption.idle();
			}
			ScaleOption.flip();
		}
	}

	public double driveBufPower=1;

	public void driveThroughGamepad(){
		driveBufPower=1+gamepad1.right_stick_y*0.5;

		DriveOption.sync(
				gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,
				driveBufPower
		);
	}

	public void runThread(){
		thread.run();
	}

	public static final char[] printCode= "-\\|/".toCharArray();
	public static int updateTime;

	public void printThreadDebugs(){
		++updateTime;
		final Map<String, PriorityAction> map=thread.getActionMap();
		for (final Map.Entry<String, PriorityAction> entry : map.entrySet()) {
			final String s = entry.getKey();
			final PriorityAction a = entry.getValue();
			TelemetryClient.getInstance().changeData(s+"-action", "["+printCode[updateTime%printCode.length]+"]"
					+a.getClass().getSimpleName()+":"+a.paramsString());
		}
	}
}
