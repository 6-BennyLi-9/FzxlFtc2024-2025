package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.lift.ClassicLiftController;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;
import org.jetbrains.annotations.Contract;

@Config
@SuppressWarnings("PublicField")
public enum LiftOption {
	;


	public enum LiftPositionTypes{
		idle,
		decantLow,
		decantHigh,
		highSuspend,
		highSuspendPrepare
	}
	private static LiftPositionTypes recent=LiftPositionTypes.idle;
	private static LiftController liftController;

	public static void connect() {
		liftController=new ClassicLiftController(HardwareConstants.lift);

		liftController.setTag("lift");
	}
	
	public static long idlePosition=0,decantLow=1080,decantHigh=2000,highSuspend=740,highSuspendPrepare=1150;

	public static LiftPositionTypes recent() {
		return recent;
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return liftController;
	}

	public static void sync(@NonNull final LiftPositionTypes option){
		recent=option;
		switch (option) {
			case idle:
				liftController.setTargetPosition(idlePosition);
				break;
			case decantLow:
				liftController.setTargetPosition(decantLow);
				break;
			case decantHigh:
				liftController.setTargetPosition(decantHigh);
				break;
			case highSuspend:
				liftController.setTargetPosition(highSuspend);
				break;
			case highSuspendPrepare:
				liftController.setTargetPosition(highSuspendPrepare);
				break;
		}
	}

	/**
	 * @return 返回 {@code recent} 是否是 {@code decant} 状态
	 */
	public static boolean decanting(){
		return LiftPositionTypes.decantHigh == recent || LiftPositionTypes.decantLow == recent;
	}
}
