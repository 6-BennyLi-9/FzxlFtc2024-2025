package org.betastudio.ftc.util.entry;

import org.betastudio.ftc.action.Action;

public interface HardwareController {
	void connect();

	Action getController();

	void writeToInstance();
}
