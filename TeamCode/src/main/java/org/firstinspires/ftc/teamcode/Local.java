package org.firstinspires.ftc.teamcode;

public class Local {
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {}
	}
}
