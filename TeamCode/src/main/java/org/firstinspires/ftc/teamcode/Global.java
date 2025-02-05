package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.teamcode.events.SystemMonitor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Global {
	public static ExecutorService threadService;
	public static Gamepad       gamepad1, gamepad2;
	public static  RunMode runMode;
	public static  OpMode  currentOpmode;
	public static  Client  client;
	private static boolean auto_create_monitor = true;

	static {
		threadService = new ThreadPoolExecutor(16, 32, 5000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue <>(1024), Executors.defaultThreadFactory(), (r, executor) -> {
			r.run();
			FtcLogTunnel.MAIN.report("Release rejected runnable: "+r);
		});
	}

	public static void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2) {
		Global.gamepad1 = gamepad1;
		Global.gamepad2 = gamepad2;
	}

	public static void prepareCoreThreadPool() {
		FtcLogTunnel.MAIN.report("Shutdown tasks of "+threadService.shutdownNow().size());

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
