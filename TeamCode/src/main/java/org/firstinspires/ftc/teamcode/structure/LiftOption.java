package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum LiftOption {
	;
	public enum LiftPositionTypes{
		idle,decantLow,decantHigh,highSuspend,highSuspendPrepare
	}
	public static LiftPositionTypes recent=LiftPositionTypes.idle;
	public static long targetPosition;
	private static final long idlePosition=0,decantLow=1100,decantHigh=2000,highSuspend=730,highSuspendPrepare=1090;

	private static class LiftController implements Action{
		@Override
		public boolean run() {
			final long delta=targetPosition- HardwareConstants.lift.getCurrentPosition();
			if(10 > Math.abs(delta)){
				HardwareConstants.lift.setPower(0);
			}else if(50 > Math.abs(delta)){
				HardwareConstants.lift.setPower(0.3 * Math.signum(delta));
			}else{
				HardwareConstants.lift.setPower(0.6 * Math.signum(delta));
			}
			return true;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return new LiftController();
	}

	public static void sync(@NonNull final LiftPositionTypes option){
		recent=option;
		switch (option) {
			case idle:
				targetPosition=idlePosition;
				break;
			case decantLow:
				targetPosition=decantLow;
				break;
			case decantHigh:
				targetPosition=decantHigh;
				break;
			case highSuspend:
				targetPosition=highSuspend;
				break;
			case highSuspendPrepare:
				targetPosition=highSuspendPrepare;
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
