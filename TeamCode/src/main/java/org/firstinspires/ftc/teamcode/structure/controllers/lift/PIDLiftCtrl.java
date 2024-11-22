package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pid.PidProcessor;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;

/**
 * 基于 {@code PID}
 */
@Config
public class PIDLiftCtrl extends LiftCtrl {
	private final PidProcessor processor;
	public static double vP,vI,vD,max_I;

	static {
		vP=1;
		vI=0;
		vD=0;
		max_I=1;
	}

	public PIDLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
		processor=new PidProcessor(vP,vI,vD,max_I);
	}

	@Override
	public void modify() {
		processor.modify(getErrorPosition());
	}

	@Override
	public double getCalibrateVal() {
		return processor.getCalibrateVal();
	}

	@Override
	public String paramsString() {
		return super.paramsString()+"//"+processor;
	}
}
