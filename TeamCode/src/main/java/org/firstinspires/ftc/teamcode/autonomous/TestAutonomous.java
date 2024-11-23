package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;
import org.firstinspires.ftc.teamcode.autonomous.utils.Util;

@Autonomous(group = "9_tests")
public class TestAutonomous extends IntegralLinearOp {
	@Override
	public void initialize() {
		utils=new Util();
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		utils.liftDecantLow().waitMs(1000).liftDecantHigh().runCached();
		sleep(1000);
		utils.liftSuspendHighPrepare().runCached();
		sleep(1000);
		utils.liftSuspendHigh().runCached();
		sleep(1000);
		utils.liftDown().runCached();
		sleep(1000);
	}
}
