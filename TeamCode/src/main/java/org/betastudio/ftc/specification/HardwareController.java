package org.betastudio.ftc.specification;

import org.betastudio.ftc.action.Action;

public interface HardwareController {
	void connect();

	Action getController();

	void writeToInstance();
}
