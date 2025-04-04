package org.betastudio.ftc;

public enum RunMode {
	TERMINATE, AUTONOMOUS, TELEOP, OTHERS;
	public static RunMode globalRunMode = TERMINATE;
}
