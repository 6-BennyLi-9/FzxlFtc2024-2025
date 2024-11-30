package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.structure.DriveOp;

@TeleOp(name = "19419(Beta)", group = "9_Beta")
public class TeleOpTesting extends TeleOpCore {
	@Override
	public void op_init() {
		super.op_init();
		DriveOp.config = DriveOp.DriveConfig.SimpleCalibrate;
	}

	@Override
	public void op_start() {
		super.op_start();
	}
}
