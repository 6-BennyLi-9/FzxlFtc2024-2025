package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.structure.controllers.lift.DcLiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

@Config
@SuppressWarnings("PublicField")
public enum LiftOp {
	;


	public enum LiftPositionTypes {
		idle, decantLow, decantHigh, highSuspend, highSuspendPrepare
	}

	public static LiftPositionTypes recent = LiftPositionTypes.idle;
	public static LiftCtrl          liftCtrl;

	public static void connect() {
		liftCtrl = new DcLiftCtrl(HardwareConstants.lift);

		liftCtrl.setTag("lift");
	}

	public static long idlePosition, decantLow = 1080, decantHigh = 2000, highSuspend = 740, highSuspendPrepare = 1250;

	public static LiftPositionTypes recent() {
		return recent;
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return liftCtrl;
	}

	public static void sync(@NonNull final LiftPositionTypes option) {
		recent = option;
		switch (option) {
			case idle:
				liftCtrl.setTargetPosition(idlePosition);
				break;
			case decantLow:
				liftCtrl.setTargetPosition(decantLow);
				break;
			case decantHigh:
				liftCtrl.setTargetPosition(decantHigh);
				break;
			case highSuspend:
				liftCtrl.setTargetPosition(highSuspend);
				break;
			case highSuspendPrepare:
				liftCtrl.setTargetPosition(highSuspendPrepare);
				break;
		}
	}

	/**
	 * @return 返回 {@code recent} 是否是 {@code decant} 状态
	 */
	public static boolean decanting() {
		return LiftPositionTypes.decantHigh == recent || LiftPositionTypes.decantLow == recent;
	}

	@NonNull
	public static Action initController() {
		connect();
		return getController();
	}
}
