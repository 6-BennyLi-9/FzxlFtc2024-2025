package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.firstinspires.ftc.teamcode.cores.eventloop.LoopCommandAutonomous;
import org.firstinspires.ftc.teamcode.cores.eventloop.commands.ActionCommand;

@Autonomous(group = "9_Tests")
public class LoopCommandAutonomousTest extends LoopCommandAutonomous {
	@Override
	public void commandOverload(){
		commands.add(new ActionCommand(new LinkedAction(
				new StatementAction(()->{
					telemetry.addLine("r1");
					telemetry.update();
				}),
				new SleepingAction(1000),
				new StatementAction(()->{
					telemetry.addLine("r2");
					telemetry.update();
				})
		)));
	}
}
