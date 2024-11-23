package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.lift.DcLiftCtrl;

public class DcAutoLiftCtrl extends DcLiftCtrl {
	public static double bufPow=1;
	public static int tolerance=10;

	public DcAutoLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target);
		setTargetPosition(targetPosition);
	}

	@Override
	public boolean run() {
		super.run();
		return false;
	}
}
