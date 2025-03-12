package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.YieldAction;
import org.firstinspires.ftc.teamcode.cores.UtilsMng;
import org.firstinspires.ftc.teamcode.cores.eventloop.LoopCommandAutonomous;

@Autonomous(group = "9_Tests")
public class LoopCommandAutonomousTest extends LoopCommandAutonomous {
	@Override
	public void commandOverload(){
		UtilsMng utils = new UtilsMng();

		commands.add(new StatementAction(()-> client.putLine("r1")));
		commands.add(new YieldAction(1000));
		commands.add(new StatementAction(()-> client.putLine("r2")));
		commands.add(new YieldAction(1000));
		commands.add(utils.liftDecantHigh().pack());
	}
}
