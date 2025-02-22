package org.exboithpath.manager;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.betastudio.ftc.util.entry.ThreadEx;

import java.util.concurrent.Executor;

public class DaemonDriveService extends BaseMecanumDriveService implements ThreadEx {
	private boolean callDaemonStop;

	public DaemonDriveService(HardwareMap hardwareMap, Executor executor) {
		super(hardwareMap);
		executor.execute(()->{
			Thread.currentThread().setDaemon(true);
			while (!callDaemonStop){
				update();
				Thread.yield();
			}
		});
	}

	@Override
	public void closeTask() {
		callDaemonStop=true;
	}
}
