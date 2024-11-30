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
		utils.integralIntakes().displayArms().waitMs(5000).integralIntakesEnding().runCached();
		flagging_op_complete();
	}
}
