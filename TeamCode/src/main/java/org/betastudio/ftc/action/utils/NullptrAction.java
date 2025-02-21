package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.action.Action;

public class NullptrAction implements Action {
	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public String paramsString() {
		return "null";
	}
}
