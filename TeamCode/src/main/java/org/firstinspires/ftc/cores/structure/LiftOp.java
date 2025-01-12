package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.Taggable;
import org.firstinspires.ftc.cores.structure.positions.LiftMode;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.LiftControllers;
import org.firstinspires.ftc.teamcode.controllers.LiftCtrl;
import org.jetbrains.annotations.Contract;

@Config
@SuppressWarnings("PublicField")
public class LiftOp implements HardwareController, Taggable {
	public static  LiftMode recent = LiftMode.IDLE;
	public static  LiftCtrl liftCtrl;
	public static long idlePosition, decantLow = 1080, decantHigh = 2000, highSuspend = 740, highSuspendPrepare = 1250, suspendLv1 = 770;
	private static LiftOp   instance;

	public static LiftOp getInstance() {
		return instance;
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return liftCtrl;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	@Override
	public void connect() {
		liftCtrl = new LiftControllers.DcLiftCtrl(HardwareDatabase.lift);

		liftCtrl.setTag("lift");
	}

	public void sync(@NonNull final LiftMode option) {
		recent = option;
		switch (option) {
			case IDLE:
				liftCtrl.setTargetPosition(idlePosition);
				break;
			case DECANT_LOW:
				liftCtrl.setTargetPosition(decantLow);
				break;
			case DECANT_HIGH:
				liftCtrl.setTargetPosition(decantHigh);
				break;
			case HIGH_SUSPEND:
				liftCtrl.setTargetPosition(highSuspend);
				break;
			case HIGH_SUSPEND_PREPARE:
				liftCtrl.setTargetPosition(highSuspendPrepare);
				break;
			case SUSPEND_LV1:
				liftCtrl.setTargetPosition(suspendLv1);
				break;
			default:
				throw new IllegalStateException("Unexpected enum state:" + option.name());
		}
	}

	/**
	 * @return 返回 {@code recent} 是否是 {@code decant} 状态
	 */
	public boolean decanting() {
		return LiftMode.DECANT_HIGH == recent || LiftMode.DECANT_LOW == recent;
	}

	@NonNull
	public Action initController() {
		connect();
		return getController();
	}

	@Override
	public String getTag() {
		return liftCtrl.getTag();
	}

	@Override
	public void setTag(String tag) {
		liftCtrl.setTag(tag);
	}
}
