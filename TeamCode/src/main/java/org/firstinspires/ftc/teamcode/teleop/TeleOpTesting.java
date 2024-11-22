package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.structure.DriveOption;

@TeleOp(name = "19419(Beta)",group = "9_Beta")
public class TeleOpTesting extends TeleOpCore{
	@Override
	public void init() {
		super.init();
		DriveOption.config= DriveOption.DriveConfig.SimpleCalibrate;
	}

	@Override
	public void start() {
		super.start();
	}
}
