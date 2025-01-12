package org.firstinspires.ftc.teamcode.events;

import static org.firstinspires.ftc.teamcode.Global.currentOpmode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;
import org.firstinspires.ftc.cores.eventloop.IntegralOpMode;
import org.firstinspires.ftc.cores.eventloop.TerminateReason;

public class IntegralThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
		if (e instanceof OpModeManagerImpl.ForceStopException) {
			if (currentOpmode instanceof ThreadAdditions) {
				((ThreadAdditions) currentOpmode).closeTask();
			} else {
				currentOpmode.terminateOpModeNow();
			}
		}else {
			Log.e("Error", "OpMode Terminated By Exception", e);
			if (currentOpmode instanceof IntegralAutonomous) {
				((IntegralOpMode) currentOpmode).sendTerminateSignal(TerminateReason.UNCAUGHT_EXCEPTION, (Exception) e);
			} else {
				currentOpmode.terminateOpModeNow();
			}

			new TaskCloseMonitor(t).start();
		}
	}
}
