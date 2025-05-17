package org.firstinspires.ftc.teamcode.structure;

import static org.betastudio.ftc.Interfaces.HardwareController;
import static org.betastudio.ftc.Interfaces.InitializeRequested;
import static org.betastudio.ftc.Interfaces.TagOptionsRequired;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.CLAW_CLOSE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.CLAW_OPEN;
import static org.firstinspires.ftc.teamcode.structure.HardwareSituation.ClawPositions;
import static org.firstinspires.ftc.teamcode.structure.HardwareSituation.ClawPositions.CLOSE;
import static org.firstinspires.ftc.teamcode.structure.HardwareSituation.ClawPositions.OPEN;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.Hardwares;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public class ClawOp implements HardwareController, InitializeRequested, TagOptionsRequired {
	public static  ClawPositions recent = OPEN;
	public static  ServoCtrl     clawControl;
	private static ClawOp        instance;

	public static ClawOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		clawControl = new ServoCtrl(Hardwares.claw, 0);

		clawControl.setTag(Labeler.summon(clawControl));
	}

	@Override
	public void init() {
		open();
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return clawControl;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	public void change() {
		switch (recent) {
			case CLOSE:
				open();
				break;
			case OPEN:
			default:
				close();
				break;
		}
	}

	public void open() {
		recent = OPEN;
		clawControl.setTargetPosition(CLAW_OPEN);
	}

	public void close() {
		recent = CLOSE;
		clawControl.setTargetPosition(CLAW_CLOSE);
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	@Override
	public String getTag() {
		return clawControl.getTag();
	}

	@Override
	public void setTag(final String tag) {
		clawControl.setTag(tag);
	}
}
