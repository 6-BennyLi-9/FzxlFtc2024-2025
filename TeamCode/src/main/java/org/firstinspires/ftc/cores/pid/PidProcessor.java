package org.firstinspires.ftc.cores.pid;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Timer;

public class PidProcessor {
	public final double vP, vI, vD;
	public final double maxVI;
	public final Timer  timer;

	private double sP, sI, sD, calibrateVal;

	private double lstErr;

	public PidProcessor(final double vP, final double vI, final double vD, final double maxVI) {
		this.maxVI = maxVI;
		this.vP = vP;
		this.vI = vI;
		this.vD = vD;
		timer = new Timer();
	}

	private boolean initialized;

	public void modify(final double err) {
		if (! initialized) {
			timer.restart();
			initialized = true;
		}
		timer.stop();
		sP = err * vP;

		sI += err * vI * timer.getDeltaTime();
		sI = Math.max(Math.min(sI, maxVI), - maxVI);

		sD = (err - lstErr) * vD / timer.getDeltaTime();
		lstErr = err;

		calibrateVal = sP + sI + sD;

		timer.restart();
	}

	public double getCalibrateVal() {
		return calibrateVal;
	}

	@NonNull
	@Override
	public String toString() {
		return "par:" + vP + "," + vI + "," + vD + ":" + sP + "," + sI + "," + sD + "->" + calibrateVal;
	}
}
