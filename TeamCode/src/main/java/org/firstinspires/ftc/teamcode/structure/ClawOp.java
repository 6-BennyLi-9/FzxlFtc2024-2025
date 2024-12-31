package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ClawPositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareDatabase;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class ClawOp implements HardwareController , InitializeRequested , TagRequested {
	public static  ClawPositionTypes recent = ClawPositionTypes.unknown;
	public static  ServoCtrl         clawControl;
	private static ClawOp            instance;

	public static ClawOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		clawControl = new ServoCtrl(HardwareDatabase.claw, 0);

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

	@Override
	public void writeToInstance() {
		instance=this;
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
