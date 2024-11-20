package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOpMode;

@Autonomous(group = "tests")
public class TestAutonomous extends IntegralLinearOpMode {
	@Override
	public void initialize() {
	}

	@Override
	public void linear() {
		utils.armsIDLE().runCached();
		sleep(5000);
		utils.boxRst().displayArms().runCached();
		sleep(5000);
	}
}
