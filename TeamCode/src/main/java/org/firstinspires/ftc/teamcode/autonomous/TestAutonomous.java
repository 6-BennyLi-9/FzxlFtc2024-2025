package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;

@Autonomous(group = "tests")
public class TestAutonomous extends IntegralLinearOp {
	@Override
	public void initialize() {
		utils.armsToSafePosition().runCached();
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
