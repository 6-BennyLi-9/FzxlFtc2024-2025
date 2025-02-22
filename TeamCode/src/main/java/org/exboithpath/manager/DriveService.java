package org.exboithpath.manager;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.betastudio.ftc.util.entry.RawUpdatable;
import org.exboithpath.loclaizer.Localizer;
import org.exboithpath.loclaizer.MirrorRoadrunnerLocalizer;
import org.exboithpath.runner.BaseMecanumRunner;
import org.exboithpath.runner.MecanumRunner;

public class DriveService implements RawUpdatable {
	private final MecanumRunner runner;
	private final Localizer localizer;
	private final DcMotorEx lf,lr,rf,rr;

	public DriveService(HardwareMap hardwareMap) {
		this(new MirrorRoadrunnerLocalizer(hardwareMap), hardwareMap);
	}

	public DriveService(Localizer localizer, @NonNull HardwareMap hardwareMap) {
		this(
				localizer,
				hardwareMap.get(DcMotorEx.class,"leftFront"),
				hardwareMap.get(DcMotorEx.class,"leftRear"),
				hardwareMap.get(DcMotorEx.class,"rightFront"),
				hardwareMap.get(DcMotorEx.class,"rightRear")
		);
	}

	public DriveService(Localizer localizer, DcMotorEx lf, DcMotorEx lr, DcMotorEx rf, DcMotorEx rr) {
		this.localizer = localizer;
		this.lf = lf;
		this.lr = lr;
		this.rf = rf;
		this.rr = rr;

		this.lf.setDirection(DcMotorSimple.Direction.FORWARD);  //F
		this.lr.setDirection(DcMotorSimple.Direction.FORWARD);  //F
		this.rf.setDirection(DcMotorSimple.Direction.REVERSE);  //R
		this.rr.setDirection(DcMotorSimple.Direction.REVERSE);  //R

		this.runner = new BaseMecanumRunner(lf, lr, rf, rr);
	}

	@Override
	public void update() {
		runner.update(localizer);
	}
}
