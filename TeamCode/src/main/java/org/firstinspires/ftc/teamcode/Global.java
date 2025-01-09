package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.betastudio.ftc.events.SystemMonitor;

public final class Global {
	public  static ThreadManager coreThreads;
	public  static Gamepad       gamepad1, gamepad2;
	public  static RunMode       currentMode;
	private static boolean       auto_create_monitor = true;

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

		if (auto_create_monitor){
			createMonitor();
		}
	}

	public static void createMonitor(){
		coreThreads.add("sys-monitor", new SystemMonitor());
	}

	public boolean auto_create_monitor(){
		return auto_create_monitor;
	}
	public void auto_create_monitor(boolean configure){
		auto_create_monitor=configure;
	}
}
