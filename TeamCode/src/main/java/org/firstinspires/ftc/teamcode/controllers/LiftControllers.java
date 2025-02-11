package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.HardwareDatabase;

/**
 * 电梯的控制器
 * <p>
 * 这个类定义了电梯的控制方式，包括电梯的各种控制方式，包括电梯的PID控制，电梯的线性函数控制，电梯的二次函数控制，电梯的电容控制等等。
 * <p>
 * 任何参数调试，可以修改 {@code public static ...} 的相关内容。
 */
public class LiftControllers {
	/**
	 * 经典的电梯控制，人走码还在
	 */
	@Config
	@Disabled
	public static class ClassicLiftCtrl extends LiftCtrl {
		public static long   staticAllowError;
		public static long   lowerErrorRange;
		public static double zeroPoseCalibrationPow, lowerCalibrationPow, higherCalibrationPow;

		static {
			staticAllowError = 35;
			lowerErrorRange = 60;

			zeroPoseCalibrationPow = 1;
			lowerCalibrationPow = 0.35;
			higherCalibrationPow = 1;
		}

		private double calibrateVal;

		public ClassicLiftCtrl(@NonNull final DcMotorEx target) {
			super(target);
		}

		@Override
		public void modify() {
			if (0 == getTargetPosition()) {
				calibrateVal = ! HardwareDatabase.liftTouch.isPressed() ? 0 : - zeroPoseCalibrationPow;
				return;
			}

			if (staticAllowError > Math.abs(getErrorPosition())) {
				calibrateVal = 0;
			} else if (lowerErrorRange > Math.abs(getErrorPosition())) {
				calibrateVal = lowerCalibrationPow * Math.signum(getErrorPosition());
			} else {
				calibrateVal = higherCalibrationPow * Math.signum(getErrorPosition());
			}
		}

		@Override
		public double getCalibrateVal() {
			return calibrateVal;
		}
	}

	/**
	 * 使用 {@link DcMotorEx} 为基础的控制器
	 */
	@Config
	public static class DcLiftCtrl extends LiftCtrl {
		public static double  bufPow                     = 1;
		public static int     tolerance                  = 10;
		private       boolean using_touch_calibrate      = true;
		private       boolean using_touch_reset_encoders = true;

		public DcLiftCtrl(@NonNull final DcMotorEx target) {
			super(target);
		}

		@Override
		public boolean activate() {
			currentPosition = targetLift.getCurrentPosition();

			//特殊处理目标值为0的情况
			if (0 == getTargetPosition() && using_touch_calibrate) {
				targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
				targetLift.setPower(! HardwareDatabase.liftTouch.isPressed() ? 0 : - 1);
				if (! HardwareDatabase.liftTouch.isPressed()) {
					targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				}
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

	/**
	 * 基于一次函数 {@code LinearFunction}
	 */
	@Config
	@Disabled
	public static class LinFuncLiftCtrl extends LiftCtrl {
		public static double vA;

		static {
			vA = 0.05;
		}

		private double calibrateVal;

		public LinFuncLiftCtrl(@NonNull final DcMotorEx target) {
			super(target);
		}

		@Override
		public void modify() {
			if (0 == getTargetPosition()) {
				if (HardwareDatabase.liftTouch.isPressed()) {//没到
					calibrateVal = - 1;
				} else {
					calibrateVal = 0;
				}
				return;
			}
			calibrateVal = getErrorPosition() * vA;
		}

		@Override
		public double getCalibrateVal() {
			return calibrateVal;
		}
	}

	/**
	 * 基于二次函数 {@code QuadrantFunction}
	 */
	@Config
	@Disabled
	public static class QuadFuncLiftCtrl extends LiftCtrl {
		public static double vA;

		static {
			vA = 0.02;
		}

		private double calibrateVal;

		public QuadFuncLiftCtrl(@NonNull final DcMotorEx target) {
			super(target);
		}

		@Override
		public void modify() {
			calibrateVal = getErrorPosition() * vA * (1 + getErrorPosition());
		}

		@Override
		public double getCalibrateVal() {
			return calibrateVal;
		}
	}
}
