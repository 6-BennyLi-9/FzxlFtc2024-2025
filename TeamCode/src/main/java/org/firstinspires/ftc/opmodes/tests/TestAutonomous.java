package org.firstinspires.ftc.opmodes.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.cores.eventloop.IntegralAutonomous;

@Autonomous(group = "9_Tests")
@Config
public final class TestAutonomous extends IntegralAutonomous {
	@Override
	public void initialize() {
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		utils.liftSuspendHighPrepare();
		utils.runAsThread();

		Local.sleep(Long.MAX_VALUE);
	}
}
