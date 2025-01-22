package org.betastudio.ftc.util;

import org.betastudio.ftc.action.Action;

public interface HardwareController {
	void connect();

	Action getController();

	void writeToInstance();
}
