package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.InitializeRequested;
import org.betastudio.ftc.interfaces.InstanceRequired;
import org.betastudio.ftc.interfaces.Taggable;
import org.firstinspires.ftc.cores.structure.positions.ClawPositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public class ClawOp implements HardwareController, InitializeRequested, Taggable , InstanceRequired<ClawOp> {
	public static  ClawPositions recent = ClawPositions.OPEN;
	public static  ServoCtrl     clawControl;
	private static ClawOp        instance;

	@Override
	public ClawOp getInstance() {
		return instance;
	}

	@Override
	public void setInstance(ClawOp instance) {
		ClawOp.instance = instance;
	}

	public static ClawOp getOp() {
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
		recent = ClawPositions.OPEN;
		clawControl.setTargetPosition(0.65);
	}

	public void close() {
		recent = ClawPositions.CLOSE;
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
	public String getTag() {
		return clawControl.getTag();
	}

	@Override
	public void setTag(String tag) {
		clawControl.setTag(tag);
	}
}
