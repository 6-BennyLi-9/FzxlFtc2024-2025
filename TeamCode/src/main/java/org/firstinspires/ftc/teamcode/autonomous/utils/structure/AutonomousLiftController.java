package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

public abstract class AutonomousLiftController extends LiftController {
	protected final long targetPosition;
	protected final DcMotorEx targetLift;

	protected AutonomousLiftController(@NonNull final DcMotorEx target,final long targetPosition){
		super(target);
		targetLift=target;
		tag=target.getDeviceName();
		this.targetPosition=targetPosition;
	}


	@Override
	public final boolean run() {
		currentPosition= targetLift	.getCurrentPosition();
		errorPosition=targetPosition- currentPosition;

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
