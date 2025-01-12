package org.firstinspires.ftc.teamcode.events;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.RunMode;

import java.util.concurrent.Callable;

public final class AutonomousMonitor extends Thread implements ThreadAdditions {
	private final Callable <Boolean> activeCaller;
	private       boolean            taskInterrupted;

	public AutonomousMonitor(Callable <Boolean> activeCaller) {
		this.activeCaller = activeCaller;
	}

	@Override
	public void run() {
		Local.waitForVal(() -> (taskInterrupted || ! activeCaller.call()), true);
		Global.runMode = RunMode.TERMINATE;
	}

	@Override
	public void closeTask() {
		taskInterrupted = true;
	}
}
