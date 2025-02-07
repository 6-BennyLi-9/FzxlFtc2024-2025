package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.betastudio.ftc.RunMode;
import org.betastudio.ftc.thread.FtcExecutorService;
import org.betastudio.ftc.thread.FtcThreadPool;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.teamcode.events.SystemMonitor;

public final class Global {
	public static  Gamepad            gamepad1, gamepad2;
	public static FtcExecutorService threadService       = new FtcThreadPool();
	public static RunMode            runMode;
	public static OpMode             currentOpmode;
	public static  Client             client;
	private static boolean            auto_create_monitor = true;

	public static void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2) {
		Global.gamepad1 = gamepad1;
		Global.gamepad2 = gamepad2;
	}

	public static void prepareCoreThreadPool() {
		FtcLogTunnel.MAIN.report("Shutdown tasks of " + threadService.shutdownNow().size());

		if (auto_create_monitor) {
			createMonitor();
		}
	}

	public static void createMonitor() {
		threadService.execute(new SystemMonitor());
		FtcLogTunnel.MAIN.report("System monitor created");
	}

	public static boolean auto_create_monitor() {
		return auto_create_monitor;
	}

	public static void auto_create_monitor(final boolean configure) {
		auto_create_monitor = configure;
	}
}
