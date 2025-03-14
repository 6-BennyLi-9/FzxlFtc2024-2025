package org.firstinspires.ftc.opmodes.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.packages.ActionPackage;
import org.betastudio.ftc.action.packages.LinkedActionPackage;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
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
		ActionPackage actionPackage = new LinkedActionPackage();
		actionPackage.add(new StatementAction(() -> client.putLine("r1")));
		actionPackage.add(new SleepingAction(1000));
		actionPackage.add(new StatementAction(() -> client.putLine("r2")));

		while (! isStopRequested()){
			actionPackage.activate();
			client.update();
		}
	}
}
