package org.betastudio.ftc.events;

import android.util.Log;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Global;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
		Log.e("Error","OpMode Terminated By Exception",e);
		Global.currentOpmode.terminateOpModeNow();
	}
}
