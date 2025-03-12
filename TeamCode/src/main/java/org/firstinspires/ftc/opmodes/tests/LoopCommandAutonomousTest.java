package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.YieldAction;
import org.firstinspires.ftc.teamcode.cores.eventloop.LoopCommandAutonomous;

@Autonomous(group = "9_Tests")
public class LoopCommandAutonomousTest extends LoopCommandAutonomous {
	@Override
	public void commandOverload(){
		commands.add(new StatementAction(()-> {
			telemetry.addLine("r1");
			telemetry.update();
		}));
		commands.add(new YieldAction(1000));
		commands.add(new StatementAction(()-> {
			telemetry.addLine("r2");
			telemetry.update();
		}));
	}
}
