package org.firstinspires.ftc.teamcore.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcore.structure.positions.ClawPositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class ClawOp implements HardwareController , InitializeRequested , TagRequested {
	public static ClawPositions recent = ClawPositions.open;
	public static ServoCtrl     clawControl;
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
		recent = ClawPositions.open;
		clawControl.setTargetPosition(0.65);
	}

	public void close() {
		recent = ClawPositions.close;
		clawControl.setTargetPosition(0.44);
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
