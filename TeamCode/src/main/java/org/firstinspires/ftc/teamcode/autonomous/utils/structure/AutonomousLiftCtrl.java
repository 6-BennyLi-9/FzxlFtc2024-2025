package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

public abstract class AutonomousLiftCtrl extends LiftCtrl {
	protected final long targetPosition;
	protected final DcMotorEx targetLift;

	protected AutonomousLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition){
		super(target);
		targetLift=target;
		tag=target.getDeviceName();
		this.targetPosition=targetPosition;
	}


	@Override
	public boolean run() {
		currentPosition= targetLift	.getCurrentPosition();

		modify();
		targetLift.setPower(getCalibrateVal());

		if(! HardwareConstants.liftTouch.isPressed()){
			targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}

		if(getCalibrationDone()){
			targetLift.setPower(0);
		}
		return !getCalibrationDone();
	}

	public abstract boolean getCalibrationDone();

	@Override
	public long getTargetPosition() {
		return this.targetPosition;
	}
}
