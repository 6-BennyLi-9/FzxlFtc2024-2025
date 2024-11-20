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
		utils.liftDecantLow().runCached();
		sleep(5000);
		utils.liftDecantHigh().runCached();
		sleep(5000);
		utils.liftSuspendHighPrepare().runCached();
		sleep(5000);
		utils.liftSuspendHigh().runCached();
		sleep(5000);
	}
}
