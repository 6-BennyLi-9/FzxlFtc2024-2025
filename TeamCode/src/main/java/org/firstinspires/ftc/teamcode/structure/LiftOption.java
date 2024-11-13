package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

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
	private static long targetPosition;
	private static final long idlePosition=0,decantLow=1100,decantHigh=2000,highSuspend=730,highSuspendPrepare=1090;

	public static LiftPositionTypes recent() {
		return recent;
	}

	private static class LiftController implements Action{
		private long currentPosition;

		@Override
		public boolean run() {
			currentPosition=HardwareConstants.lift.getCurrentPosition();
			final long delta=targetPosition- currentPosition;

			if(targetPosition==0){
				HardwareConstants.lift.setPower(currentPosition <= 10 ? 0 : - 0.5);
				return true;
			}

			if(40 > Math.abs(delta)){
				HardwareConstants.lift.setPower(0);
			}else if(120 > Math.abs(delta)){
				HardwareConstants.lift.setPower(0.3 * Math.signum(delta));
			}else{
				HardwareConstants.lift.setPower(0.7 * Math.signum(delta));
			}
			return true;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return currentPosition+"->"+targetPosition;
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
