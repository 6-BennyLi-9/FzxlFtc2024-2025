package org.firstinspires.ftc.opmodes.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.eventloop.integral.IntegralAutonomous;

@Autonomous(group = "9_Tests")
@Config
public final class TestAutonomous extends IntegralAutonomous {
	@Override
	public void initialize() {
		utils.armSafe();
		utils.runCached();
	}

	@Override
	public void linear() {

	}
}
