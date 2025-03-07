package org.exboithpath.runner;

import static org.exboithpath.runner.RunnerStatus.CALIBRATING;
import static org.exboithpath.runner.RunnerStatus.IDLE;
import static org.exboithpath.runner.RunnerStatus.RUNNING;
import static org.exboithpath.runner.RunnerStatus.STARTING;
import static org.exboithpath.runner.RunnerStatus.STOPPING;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.exboithpath.graphics.Pose;
import org.exboithpath.loclaizer.Localizer;

public class ExpansionMecanumRunner extends BaseMecanumRunner {
	public static final double       bufferOnRunning      = 0.1;
	public static final double       bufferOnCalibrating  = 0.05;
	public static final double       allowUpperErrInch    = 0.5;
	public static final double       allowUpperErrHeading = Math.toRadians(0.2);
	public static final double       allowLowerErrInch    = 0.1;
	public static final double       allowLowerErrHeading = Math.toRadians(1.0e-3);
	public              double       buffer;
	private             boolean      isRunning            = true;
	private             RunnerStatus status;

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
		isRunning = false;
	}

	@Override
	public void start() {
		isRunning = true;
	}

	@Override
	public void update(Localizer localizer) {
		if (isRunning) {
			syncPose(localizer);
			Pose pose = current.poseTrackTo(target);

			Pose onRunning = pose;

			switch (status) {
				case STARTING:
					status = RUNNING;
				case RUNNING:
					buffer = bufferOnRunning;
					if (pose.dis() <= allowUpperErrInch && Math.abs(pose.heading) <= allowUpperErrHeading) {
						status = CALIBRATING;
					} else {
						break;
					}
				case CALIBRATING:
					buffer = bufferOnCalibrating;
					if (pose.dis() <= allowLowerErrInch && Math.abs(pose.heading) <= allowLowerErrHeading) {
						status = STOPPING;
					} else if (pose.dis() <= allowUpperErrInch && Math.abs(pose.heading) <= allowUpperErrHeading) {
						status = CALIBRATING;
						break;
					} else {
						status = RUNNING;
						buffer = bufferOnRunning;
						break;
					}
				case STOPPING:
					onRunning = new Pose(0, 0, 0);
					status = IDLE;
				case IDLE:
					if (! (pose.dis() <= allowLowerErrInch) || ! (Math.abs(pose.heading) <= allowLowerErrHeading)) {
						status = STARTING;
					}
					break;
			}

			setPowers(onRunning);
		}
	}
}
