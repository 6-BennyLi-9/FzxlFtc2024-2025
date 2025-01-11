package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.controllers.lift.DcLiftCtrl;
import org.firstinspires.ftc.cores.structure.positions.LiftMode;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.Taggable;
import org.jetbrains.annotations.Contract;

@Config
@SuppressWarnings("PublicField")
public class LiftOp implements HardwareController , Taggable {
	public static  LiftMode recent = LiftMode.idle;
	public static  LiftCtrl liftCtrl;
	private static LiftOp   instance;

	public static LiftOp getInstance(){
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
		instance=this;
	}

	@Override
	public void connect() {
		liftCtrl = new DcLiftCtrl(HardwareDatabase.lift);

		liftCtrl.setTag("lift");
	}

	public static long idlePosition, decantLow = 1080, decantHigh = 2000, highSuspend = 740, highSuspendPrepare = 1250, suspendLv1 = 770;

	public void sync(@NonNull final LiftMode option) {
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
			case suspendLv1:
				liftCtrl.setTargetPosition(suspendLv1);
				break;
			default:
				throw new IllegalStateException("Unexpected enum state:"+option.name());
		}
	}

	/**
	 * @return 返回 {@code recent} 是否是 {@code decant} 状态
	 */
	public boolean decanting() {
		return LiftMode.decantHigh == recent || LiftMode.decantLow == recent;
	}

	@NonNull
	public Action initController() {
		connect();
		return getController();
	}

	@Override
	public void setTag(String tag) {
		liftCtrl.setTag(tag);
	}

	@Override
	public String getTag() {
		return liftCtrl.getTag();
	}
}
