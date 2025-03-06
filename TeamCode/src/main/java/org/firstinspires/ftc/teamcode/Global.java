package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.betastudio.ftc.RunMode;
import org.betastudio.ftc.thread.TaskMng;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.teamcode.events.SystemMonitor;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Global {
	public static  Gamepad gamepad1, gamepad2;
	public static final TaskMng service = new TaskMng(defaultThreadExecutor());
	public static       RunMode runMode;
	public static  OpMode  currentOpmode;
	public static  Client  client;
	private static boolean auto_create_monitor;

	public static void registerGamepad(final Gamepad gamepad1, final Gamepad gamepad2) {
		Global.gamepad1 = gamepad1;
		Global.gamepad2 = gamepad2;
	}

	public static void prepareCoreThreadPool() {
		FtcLogTunnel.MAIN.report("Shutdown tasks of " + service.reboot(defaultThreadExecutor()).size());

		if (auto_create_monitor) {
			createMonitor();
		}
	}

	public static void createMonitor() {
		service.execute(new SystemMonitor());
		FtcLogTunnel.MAIN.report("System monitor created");
	}

	public static boolean auto_create_monitor() {
		return auto_create_monitor;
	}

	public static void auto_create_monitor(final boolean configure) {
		auto_create_monitor = configure;
	}

	@NonNull
	@Contract(" -> new")
	public static ThreadPoolExecutor defaultThreadExecutor(){
		return new ThreadPoolExecutor(16, 32, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue <>(1024), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
	}
}
