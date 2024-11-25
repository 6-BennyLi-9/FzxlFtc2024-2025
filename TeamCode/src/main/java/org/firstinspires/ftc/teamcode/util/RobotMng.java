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
import org.firstinspires.ftc.teamcode.structure.ArmOp;
import org.firstinspires.ftc.teamcode.structure.ClipOp;
import org.firstinspires.ftc.teamcode.structure.DriveOp;
import org.firstinspires.ftc.teamcode.structure.TakeOp;
import org.firstinspires.ftc.teamcode.structure.LiftOp;
import org.firstinspires.ftc.teamcode.structure.PlaceOp;
import org.firstinspires.ftc.teamcode.structure.ScaleOp;

import java.util.Map;

/**
 * 申名时需要初始化{@link #registerGamepad(Gamepad, Gamepad)}
 */
public class RobotMng {
	@NonNull public static Gamepad gamepad1,gamepad2;
	public TaggedActionPackage thread;

	static {
		gamepad1=new Gamepad();
		gamepad2=new Gamepad();
	}

	public RobotMng(){
		thread =new TaggedActionPackage();
	}

	public void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2){
		RobotMng.gamepad1 =gamepad1;
		RobotMng.gamepad2 =gamepad2;
	}

	public void initActions(){
		ArmOp.connect();
		ClipOp.connect();
		DriveOp.connect();
		TakeOp.connect();
		LiftOp.connect();
		PlaceOp.connect();
		ScaleOp.connect();

		thread.add("clip", ClipOp.cloneController());
		thread.add("intake", TakeOp.cloneController());
		thread.add("lift", LiftOp.cloneController());
		thread.add("place", PlaceOp.cloneController());
		thread.add("arm", ArmOp.cloneController());
		thread.add("scale", ScaleOp.cloneController());
		thread.add("drive", DriveOp.cloneAction());

		ArmOp.init();
		ClipOp.init();
		PlaceOp.init();
		ScaleOp.init();
	}

	public final void operateThroughGamepad(){
		syncRequests();

		if(clipOption.getEnabled()){
			ClipOp.change();
		}

		if(sampleIO.getEnabled()){
			sampleIO.ticker.tickAndMod(3);
			switch (sampleIO.ticker.getTicked()){
				case 0:
					TakeOp.idle();
					break;
				case 1:
					TakeOp.outtake();
					break;
				case 2:
					TakeOp.intake();
					break;
				default:
					throw new IllegalStateException("SampleOptioning Unexpected value: " + sampleIO.ticker.getTicked());
			}
		}

		if(liftIDLE.getEnabled()){
			if(PlaceOp.PlacePositionTypes.decant == PlaceOp.recent()){
				PlaceOp.idle();
			}
			if(LiftOp.LiftPositionTypes.highSuspend == LiftOp.recent()){
				ClipOp.open();
			}

			LiftOp.sync(LiftOp.LiftPositionTypes.idle);
		}else if(liftDecantUpping.getEnabled()){
			if(ArmOp.isNotSafe()){
				ArmOp.safe();
			}

			if(LiftOp.LiftPositionTypes.idle == LiftOp.recent()){
				LiftOp.sync(LiftOp.LiftPositionTypes.decantLow);
			}else if(LiftOp.LiftPositionTypes.decantLow == LiftOp.recent()){
				LiftOp.sync(LiftOp.LiftPositionTypes.decantHigh);
			}
		}else if(liftHighSuspendPrepare.getEnabled()){
			if(ArmOp.isNotSafe()){
				ArmOp.safe();
			}

			LiftOp.sync(LiftOp.LiftPositionTypes.highSuspendPrepare);
		}

		if(decantOrSuspend.getEnabled()){
			if(LiftOp.decanting()){
				PlaceOp.decant();
			} else if (LiftOp.LiftPositionTypes.highSuspendPrepare == LiftOp.recent()) {
				LiftOp.sync(LiftOp.LiftPositionTypes.highSuspend);
			}
		}

		if(armScaleOperate.getEnabled()){
			armScaleOperate.ticker.tickAndMod(3);
		}
		switch (armScaleOperate.ticker.getTicked()){
			case 0:
				ScaleOp.back();
				ArmOp.safe();
				break;
			case 1:
				ScaleOp.operate(gamepad2.left_stick_x*0.2+0.8);
				ArmOp.intake();
				break;
			case 2:
				ScaleOp.back();
				ArmOp.idle();
				break;
			default:
				throw new IllegalStateException("Scaling Unexpected value: " + armScaleOperate.ticker.getTicked());
		}
	}

	public static double driveBufPower=1,triggerBufFal=0.5;

	public final void driveThroughGamepad(){
		driveBufPower=1+gamepad1.right_stick_y*0.5;

		DriveOp.sync(
				gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,
				driveBufPower
		);

		if(gamepad1.left_bumper){
			DriveOp.additions(0,0,-0.1);
		}
		if(gamepad1.right_bumper){
			DriveOp.additions(0,0,0.1);
		}

		DriveOp.additions(0,0,gamepad1.right_trigger-gamepad1.left_trigger,triggerBufFal);

		if(gamepad1.a){
			DriveOp.targetAngleRst();
		}
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
