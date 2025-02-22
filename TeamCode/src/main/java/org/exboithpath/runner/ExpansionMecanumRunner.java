package org.exboithpath.runner;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.exboithpath.loclaizer.Localizer;

public class ExpansionMecanumRunner extends BaseMecanumRunner {
	public static final double bufferOnRunning = 0.1;
	public static final double bufferOnCalibrating = 0.05;
	public double buffer;

	public ExpansionMecanumRunner(DcMotorEx lf, DcMotorEx lr, DcMotorEx rf, DcMotorEx rr) {
		super(lf, lr, rf, rr);
	}

	@Override
	public void setPowers(double x, double y, double head) {
		super.setPowers(x * buffer, y * buffer, head * buffer);
	}

	@Override
	public RunnerStatus getStatus() {
		return super.getStatus();
	}

	@Override
	public void shutdown() {
		setPowers(0, 0, 0);

	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void update(Localizer localizer) {
		super.update(localizer);
	}
}
