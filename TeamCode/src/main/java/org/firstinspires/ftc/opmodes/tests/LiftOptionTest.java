package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.Annotations;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.DcTeleLiftCtrl;

@Disabled
@Annotations.TestDoneSuccessfully
@TeleOp
public class LiftOptionTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		HardwareDatabase.sync(hardwareMap, true);
		DcTeleLiftCtrl ctrl = new DcTeleLiftCtrl(HardwareDatabase.leftLift, HardwareDatabase.rightLift);
		ctrl.setTargetPosition(0);

		waitForStart();

		while (opModeIsActive()) {
			if (gamepad1.dpad_up) {
				ctrl.setTargetPosition(ctrl.getTargetPosition() + 10);
			} else if (gamepad1.dpad_down) {
				ctrl.setTargetPosition(ctrl.getTargetPosition() - 10);
			}

			ctrl.activate();
			telemetry.addData("Lift Position", HardwareDatabase.leftLift.getCurrentPosition());
		}
	}
}
