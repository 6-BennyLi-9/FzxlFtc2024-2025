package org.betastudio.ftc.events;

import static org.firstinspires.ftc.teamcode.Global.coreThreads;
import static org.firstinspires.ftc.teamcode.Global.currentMode;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.RunMode;

public final class SystemMonitor extends Thread implements ThreadAdditions {
	private boolean taskClosed;

	@Override
	public void run() {
		while (currentMode != RunMode.Terminated && !taskClosed){
			Local.sleep(5000);
		}
		//正常退出
		if(!taskClosed){
			coreThreads.interruptAll();
		}
	}

	@Override
	public void closeTask() {
		taskClosed=true;
	}
}
