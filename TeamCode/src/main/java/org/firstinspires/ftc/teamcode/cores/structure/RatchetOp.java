package org.firstinspires.ftc.teamcode.cores.structure;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.HardwareConfigures;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;

public class RatchetOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static  boolean   isTightened = false;
	public static  ServoCtrl ratchetControl;
	private static RatchetOp instance;

	public static RatchetOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		ratchetControl = new ServoCtrl(HardwareDatabase.ratchet, HardwareConfigures.RATCHET_LOOSEN);

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
		ratchetControl.setTargetPosition(HardwareConfigures.RATCHET_LOOSEN);
	}

	public void tighten() {
		ratchetControl.setTargetPosition(HardwareConfigures.RATCHET_TIGHT);
	}

	@Override
	public String getTag() {
		return ratchetControl.getTag();
	}

	@Override
	public void setTag(String tag) {
		ratchetControl.setTag(tag);
	}
}
