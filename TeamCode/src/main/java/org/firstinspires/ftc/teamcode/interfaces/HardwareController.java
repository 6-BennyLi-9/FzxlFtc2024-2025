package org.firstinspires.ftc.teamcode.interfaces;

import org.betastudio.ftc.action.Action;

public interface HardwareController {
	void connect();
	Action getController();
	void writeToInstance();
}
