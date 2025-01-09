package org.firstinspires.ftc.cores.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.cores.structure.DriveMode;
import org.firstinspires.ftc.cores.structure.DriveOp;

@Disabled
@TeleOp(name = "19419(Beta)", group = "9_Beta")
public class TeleOpTesting extends TeleOpCore {
	@Override
	public void op_init() {
		super.op_init();
		DriveOp.config = DriveMode.SimpleCalibrate;
	}

	@Override
	public void op_start() {
		super.op_start();
	}
}
