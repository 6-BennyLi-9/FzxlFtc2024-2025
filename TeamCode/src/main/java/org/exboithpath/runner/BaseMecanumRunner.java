package org.exboithpath.runner;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.exboithpath.graphics.Pose;
import org.exboithpath.loclaizer.Localizer;

public class BaseMecanumRunner implements MecanumRunner {
	protected interface RunnerUpdater {
		void run(Localizer localizer);
	}
	private Pose target,current;
	private final DcMotorEx lf,lr,rf,rr;
	private RunnerUpdater updater;

	public BaseMecanumRunner(DcMotorEx lf, DcMotorEx lr, DcMotorEx rf, DcMotorEx rr) {
		this.lf = lf;
		this.lr = lr;
		this.rf = rf;
		this.rr = rr;

		start();
	}

	public void setPowers(@NonNull Pose powers) {
		setPowers(powers.x, powers.y, powers.heading);
	}

	public void setPowers(double x, double y, double head){
		lf.setPower(y - x - head);
		lr.setPower(y + x - head);
		rf.setPower(y + x + head);
		rr.setPower(y - x + head);
	}

	@Override
	public void setPoseTarget(Pose target) {
		this.target=target;
	}

	@Override
	public void shutdown() {
		setPowers(0,0,0);
		updater= localizer ->{};
	}

	@Override
	public void start() {
		updater = localizer -> {
			syncPose(localizer);
			setPowers(current.poseTrackTo(target));
		};
	}

	@Override
	public void syncPose(@NonNull Localizer localizer) {
		current = localizer.update();
	}

	@Override
	public void update(Localizer localizer) {
		updater.run(localizer);
	}
}
