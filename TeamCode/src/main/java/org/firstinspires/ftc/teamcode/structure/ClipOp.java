package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ClipPositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class ClipOp implements HardwareController , InitializeRequested , TagRequested {
	private static ClipOp instance;
	public static ClipPositionTypes recent = ClipPositionTypes.unknown;
	public static ServoCtrl         clipControl;

	public static ClipOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		clipControl = new ServoCtrl(HardwareConstants.clip, 0);

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
		recent = ClipPositionTypes.open;
		clipControl.setTargetPosition(0);
	}

	public void close() {
		recent = ClipPositionTypes.close;
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
