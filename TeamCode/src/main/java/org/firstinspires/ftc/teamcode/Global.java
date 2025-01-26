package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.util.ThreadManager;
import org.firstinspires.ftc.teamcode.events.SystemMonitor;

public final class Global {
	public  static ThreadManager threadManager;
	public  static Gamepad       gamepad1, gamepad2;
	public  static RunMode       runMode;
	public  static OpMode        currentOpmode;
	public  static Client        client;
	private static boolean       auto_create_monitor = true;

	public static void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2) {
		Global.gamepad1 = gamepad1;
		Global.gamepad2 = gamepad2;
	}

	public static void prepareCoreThreadPool() {
		if (null != threadManager) {
			if (! threadManager.isEmpty()) {
				threadManager.interruptAll();
			}
		}
		threadManager = new ThreadManager();

		if (auto_create_monitor) {
			createMonitor();
		}
	}

	public static void createMonitor() {
		threadManager.add("sys-monitor", new SystemMonitor());
	}

	public static boolean auto_create_monitor() {
		return auto_create_monitor;
	}

	public static void auto_create_monitor(final boolean configure) {
		auto_create_monitor = configure;
	}
}
