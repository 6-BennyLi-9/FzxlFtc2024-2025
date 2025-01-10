package org.firstinspires.ftc.teamcode.events;

import static org.firstinspires.ftc.teamcode.Global.runMode;
import static org.firstinspires.ftc.teamcode.Global.threadManager;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.RunMode;

public final class SystemMonitor extends Thread implements ThreadAdditions {
	private boolean taskClosed;

	@Override
	public void run() {
		while (runMode != RunMode.terminated && !taskClosed){
			Local.sleep(5000);
		}
		//正常退出
		if(!taskClosed){
			threadManager.interruptAll();
		}
	}

	@Override
	public void closeTask() {
		taskClosed=true;
	}
}
