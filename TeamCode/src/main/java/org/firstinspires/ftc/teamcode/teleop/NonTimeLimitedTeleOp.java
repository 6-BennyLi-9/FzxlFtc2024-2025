package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@Deprecated
@TeleOp(name = "19419(non-TLE)", group = "1_Utils")
public class NonTimeLimitedTeleOp extends TeleOpCore {
	@Override
	public void init() {
		super.init();
		auto_terminate_when_TLE(false);
	}
}
