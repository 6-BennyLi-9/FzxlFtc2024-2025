package org.exboithpath.manager;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.betastudio.ftc.util.entry.ThreadEx;

import java.util.concurrent.Executor;

public class DaemonDriveService extends BaseMecanumDriveService implements ThreadEx {
	private boolean callDaemonStop;

	public DaemonDriveService(HardwareMap hardwareMap, @NonNull Executor executor) {
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
