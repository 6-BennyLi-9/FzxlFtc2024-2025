package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.cores.structure.positions.ClipPositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public class ClipOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static  ClipPositions recent = ClipPositions.OPEN;
	public static  ServoCtrl     clipControl;
	private static ClipOp        instance;

	public static ClipOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		clipControl = new ServoCtrl(HardwareDatabase.clip, 0);

		clipControl.setTag(Labeler.gen().summon(clipControl));
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
		recent = ClipPositions.OPEN;
		clipControl.setTargetPosition(0);
	}

	public void close() {
		recent = ClipPositions.CLOSE;
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
	public String getTag() {
		return clipControl.getTag();
	}

	@Override
	public void setTag(final String tag) {
		clipControl.setTag(tag);
	}
}
