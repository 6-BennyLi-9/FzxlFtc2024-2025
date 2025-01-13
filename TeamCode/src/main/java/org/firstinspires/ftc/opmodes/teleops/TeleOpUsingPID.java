package org.firstinspires.ftc.opmodes.teleops;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.cores.structure.AngleCalibrateMode;
import org.firstinspires.ftc.cores.structure.DriveOp;

@Disabled
@TeleOp(name = "19419(using PID)", group = "9_Beta")
public class TeleOpUsingPID extends TeleOpCore {
	@Override
	public void op_init() {
		super.init();
		DriveOp.config = AngleCalibrateMode.PID;
	}
}
