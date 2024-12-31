package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareDatabase;

/**
 * 使用 {@link DcMotorEx} 为基础的控制器
 */
@Config
public class DcLiftCtrl extends LiftCtrl {
	public static double  bufPow                     = 1;
	public static int     tolerance                  = 10;
	private       boolean using_touch_calibrate      = true;
	private       boolean using_touch_reset_encoders = true;

	public DcLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public boolean run() {
		currentPosition=targetLift.getCurrentPosition();

		if (0 == getTargetPosition() && using_touch_calibrate) {
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			targetLift.setPower(! HardwareDatabase.liftTouch.isPressed() ? 0 : - 1);
			if (! HardwareDatabase.liftTouch.isPressed()) {
				targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			}
			return true;
		}

		if(0 == getTargetPosition() && using_touch_reset_encoders && ! HardwareDatabase.liftTouch.isPressed()){
			targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			targetLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			targetLift.setPower(0);
			return true;
		}

		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setTargetPositionTolerance(tolerance);
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow);

		return true;
	}

	@Override
	public void modify() {
	}

	@Override
	public double getCalibrateVal() {
		return 0;
	}

	public void using_touch_calibrate(final boolean using_touch_calibrate) {
		this.using_touch_calibrate = using_touch_calibrate;
	}
	public boolean using_touch_calibrate() {
		return using_touch_calibrate;
	}


	public void using_touch_reset_encoders(final boolean using_touch_reset_encoders) {
		this.using_touch_reset_encoders = using_touch_reset_encoders;
	}
	public boolean using_touch_reset_encoders() {
		return using_touch_reset_encoders;
	}
}
