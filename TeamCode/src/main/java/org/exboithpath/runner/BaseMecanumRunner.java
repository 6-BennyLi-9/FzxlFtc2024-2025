package org.exboithpath.runner;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.exboithpath.graphics.Pose;
import org.exboithpath.loclaizer.Localizer;

public class BaseMecanumRunner implements MecanumRunner {
	private Pose target,current;
	private final DcMotorEx lf,lr,rf,rr;
	private Runnable updater;

	public BaseMecanumRunner(DcMotorEx lf, DcMotorEx lr, DcMotorEx rf, DcMotorEx rr) {
		this.lf = lf;
		this.lr = lr;
		this.rf = rf;
		this.rr = rr;

		updater = ()->{

		};
	}

	public void setPowers(double x,double y,double head){
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
	}

	@Override
	public void syncPose(Localizer localizer) {

	}

	@Override
	public void update() {

	}

	@Override
	public boolean isUpdateRequested() {
		return false;
	}
}
