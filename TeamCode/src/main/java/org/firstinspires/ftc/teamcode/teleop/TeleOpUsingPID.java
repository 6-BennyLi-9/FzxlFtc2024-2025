package org.firstinspires.ftc.teamcode.teleop;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.structure.DriveOp;

@TeleOp(name = "19419(using PID)", group = "9_Beta")
public class TeleOpUsingPID extends TeleOpCore {
	@Override
	public void op_init() {
		super.init();
		DriveOp.config = DriveOp.DriveConfig.PID;
	}
}
