package org.firstinspires.ftc.opmodes.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;

@Autonomous(group = "9_Tests")
@Config
public final class TestAutonomous extends IntegralAutonomous {
	@Override
	public void initialize() {
		//		utils = new UtilsMng();
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		//		utils.integralIntakes().displayArms().waitMs(5000).integralIntakesEnding().runCached();
		//		utils.scaleOperate(0.85);
		angleCalibration(90);
		angleCalibration(- 90);
		angleCalibration(0);
		flagging_op_complete();
	}
}
