package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.structure.controllers.lift.DcLiftCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.LiftPositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.jetbrains.annotations.Contract;

@Config
@SuppressWarnings("PublicField")
public class LiftOp implements HardwareController {
	private static LiftOp instance;
	public static LiftPositionTypes recent = LiftPositionTypes.idle;
	public static LiftCtrl          liftCtrl;

	public static LiftOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		liftCtrl = new DcLiftCtrl(HardwareConstants.lift);

		liftCtrl.setTag("lift");
	}

	public long idlePosition, decantLow = 1080, decantHigh = 2000, highSuspend = 740, highSuspendPrepare = 1250, suspendLv1 = 770;

	@NonNull
	@Contract(" -> new")
	public Action getController() {
		return liftCtrl;
	}

	public void sync(@NonNull final LiftPositionTypes option) {
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
	public boolean decanting() {
		return LiftPositionTypes.decantHigh == recent || LiftPositionTypes.decantLow == recent;
	}

	@NonNull
	public Action initController() {
		connect();
		return getController();
	}
}
