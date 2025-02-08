package org.firstinspires.ftc.opmodes.teleops;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cores.structure.DriveMode;
import org.firstinspires.ftc.teamcode.cores.structure.DriveOp;

@Disabled
@TeleOp(name = "19419(using PID)", group = "9_Beta")
public class TeleOpUsingPID extends TeleOpCore {
	@Override
	public void op_init() {
		init();
		DriveOp.config = DriveMode.PID;
	}
}
