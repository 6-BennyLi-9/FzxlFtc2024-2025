package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.util.ThreadManager;

public final class Global {
	public static ThreadManager coreThreads;
	public static Gamepad       gamepad1, gamepad2;
	public static RunMode       currentMode;

	public static void registerGamepad(Gamepad gamepad1,Gamepad gamepad2){
		Global.gamepad1=gamepad1;
		Global.gamepad2=gamepad2;
	}
	public static void prepareCoreThreadPool(){
		if (coreThreads != null) {
			if (! coreThreads.isEmpty()) {
				coreThreads.interruptAll();
			}
		}
		coreThreads=new ThreadManager();
	}

	public static void createMonitor(){
		coreThreads.add("sys-monitor",
			new Thread(()->{
				while (currentMode!=RunMode.Interrupted){
					sleep(5000);
				}
				coreThreads.interruptAll();
			})
		);
	}

	public static void sleep(long mills){
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
