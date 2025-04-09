package org.firstinspires.ftc.teamcode.cores.structure;

import static org.firstinspires.ftc.teamcode.HardwareConfigures.RATCHET_LOOSEN;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.RATCHET_TIGHT;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;

public class RatchetOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static  boolean   isTightened;
	public static  ServoCtrl ratchetControl;
	private static RatchetOp instance;

	public static RatchetOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		ratchetControl = new ServoCtrl(HardwareDatabase.ratchet, RATCHET_LOOSEN);

		ratchetControl.setTag(Labeler.gen().summon(ratchetControl));
	}

	@Override
	public Action getController() {
		return ratchetControl;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	@Override
	public void init() {
		loosen();
	}

	public void loosen() {
		ratchetControl.setTargetPosition(RATCHET_LOOSEN);
	}

	public void tighten() {
		ratchetControl.setTargetPosition(RATCHET_TIGHT);
	}

	@Override
	public String getTag() {
		return ratchetControl.getTag();
	}

	@Override
	public void setTag(final String tag) {
		ratchetControl.setTag(tag);
	}
}
