package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ClawPositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.firstinspires.ftc.teamcode.util.implement.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.implement.TagRequested;
import org.jetbrains.annotations.Contract;

public class ClawOp implements HardwareController , InitializeRequested , TagRequested {
	private static ClawOp instance;
	public static ClawPositionTypes recent = ClawPositionTypes.unknown;
	public static ServoCtrl         clawControl;

	public static ClawOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		clawControl = new ServoCtrl(HardwareConstants.claw, 0);

		clawControl.setTag("claw");
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

	public void change() {
		switch (recent) {
			case close:
				open();
				break;
			case open:
			default:
				close();
				break;
		}
	}

	public void open() {
		recent = ClawPositionTypes.open;
		clawControl.setTargetPosition(0.65);
	}

	public void close() {
		recent = ClawPositionTypes.close;
		clawControl.setTargetPosition(0.45);
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	@Override
	public void setTag(String tag) {

	}

	@Override
	public String getTag() {
		return clawControl.getTag();
	}
}
