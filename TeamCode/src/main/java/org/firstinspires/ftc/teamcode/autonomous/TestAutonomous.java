package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(group = "9_Tests")
public final class TestAutonomous extends IntegralAutonomous {
	@Override
	public void initialize() {
//		utils = new UtilMng();
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftDecantLow().waitMs(1000).liftDecantHigh().waitMs(1000).liftDown().runCached();
		utils.integralIntakes().scaleOperate(0.82).waitMs(1000).integralIntakesEnding().runCached();

		flagging_op_complete();
	}
}
