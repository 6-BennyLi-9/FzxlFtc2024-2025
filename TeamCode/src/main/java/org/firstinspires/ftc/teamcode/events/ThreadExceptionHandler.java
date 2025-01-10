package org.firstinspires.ftc.teamcode.events;

import android.util.Log;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;
import org.firstinspires.ftc.cores.eventloop.IntegralOpMode;
import org.firstinspires.ftc.cores.eventloop.TerminateReason;
import org.firstinspires.ftc.teamcode.Global;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
		Log.e("Error","OpMode Terminated By Exception",e);
		if (Global.currentOpmode instanceof IntegralAutonomous) {
			((IntegralOpMode) Global.currentOpmode).sendTerminateSignal(TerminateReason.UncaughtException, (Exception) e);
		}else{
			Global.currentOpmode.terminateOpModeNow();
		}

		new TaskCloseMonitor(t).start();
	}
}
