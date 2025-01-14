package org.firstinspires.ftc.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "19419(with TLE)", group = "9_Beta")
public class TeleWithTLE extends TeleOpCore{
	@Override
	public void op_init() {
		super.op_init();
		auto_terminate_when_TLE(true);
	}
}
