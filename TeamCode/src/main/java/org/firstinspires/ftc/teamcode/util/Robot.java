package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.armScaleOperate;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.clipOption;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.decantOrSuspend;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftDecantUpping;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftHighSuspendPrepare;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.liftIDLE;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.sampleIO;
import static org.firstinspires.ftc.teamcode.util.GamepadRequestMemories.syncRequests;

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

		if(clipOption.getEnabled()){
			ClipOption.change();
		}

		if(sampleIO.getEnabled()){
			sampleIO.ticker.tickAndMod(3);
			switch (sampleIO.ticker.getTicked()){
				case 0:
					IOTakesOption.idle();
					break;
				case 1:
					IOTakesOption.outtake();
					break;
				case 2:
					IOTakesOption.intake();
					break;
				default:
					throw new IllegalStateException("SampleOptioning Unexpected value: " + sampleIO.ticker.getTicked());
			}
		}

		if(liftIDLE.getEnabled()){
			if(ClipOption.ClipPositionTypes.open == ClipOption.recent()){
				ClipOption.open();
			}
			if(PlaceOption.PlacePositionTypes.decant == PlaceOption.recent()){
				PlaceOption.idle();
			}
			LiftOption.sync(LiftOption.LiftPositionTypes.idle);
		}else if(liftDecantUpping.getEnabled()){
			if(ArmOption.isNotSafe()){
				ArmOption.safe();
			}

			if(LiftOption.LiftPositionTypes.idle == LiftOption.recent()){
				LiftOption.sync(LiftOption.LiftPositionTypes.decantLow);
			}else if(LiftOption.LiftPositionTypes.decantLow == LiftOption.recent()){
				LiftOption.sync(LiftOption.LiftPositionTypes.decantHigh);
			}
		}else if(liftHighSuspendPrepare.getEnabled()){
			if(ArmOption.isNotSafe()){
				ArmOption.safe();
			}

			LiftOption.sync(LiftOption.LiftPositionTypes.highSuspendPrepare);
		}

		if(decantOrSuspend.getEnabled()){
			if(LiftOption.decanting()){
				PlaceOption.decant();
			} else if (LiftOption.LiftPositionTypes.highSuspendPrepare == LiftOption.recent()) {
				LiftOption.sync(LiftOption.LiftPositionTypes.highSuspend);
			}
		}

		if(armScaleOperate.getEnabled()){
			armScaleOperate.ticker.tickAndMod(4);
			switch (armScaleOperate.ticker.getTicked()){
				case 0:
					ScaleOption.back();
					ArmOption.safe();
					break;
				case 1:
					ScaleOption.probe();
					ArmOption.intake();
					break;
				case 2:
					ScaleOption.back();
					ArmOption.intake();
					break;
				case 3:
					ScaleOption.back();
					ArmOption.idle();
					break;
				default:
					throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.ticker.getTicked());
			}
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
