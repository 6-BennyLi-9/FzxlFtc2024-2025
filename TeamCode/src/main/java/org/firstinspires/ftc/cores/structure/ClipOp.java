package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.firstinspires.ftc.cores.structure.positions.ClipPositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.InitializeRequested;
import org.betastudio.ftc.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class ClipOp implements HardwareController , InitializeRequested , TagRequested {
	public static ClipPositions recent = ClipPositions.open;
	public static ServoCtrl     clipControl;
	private static ClipOp            instance;

	public static ClipOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		clipControl = new ServoCtrl(HardwareDatabase.clip, 0);

		clipControl.setTag("clip");
	}

	@Override
	public void init() {
		open();
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return clipControl;
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
		recent = ClipPositions.open;
		clipControl.setTargetPosition(0);
	}

	public void close() {
		recent = ClipPositions.close;
		clipControl.setTargetPosition(0.5);
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
		clipControl.setTag(tag);
	}

	@Override
	public String getTag() {
		return clipControl.getTag();
	}
}
