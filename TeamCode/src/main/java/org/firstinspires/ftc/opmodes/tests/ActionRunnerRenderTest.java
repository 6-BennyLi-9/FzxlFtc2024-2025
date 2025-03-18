package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.render.ClientRender;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;

@Autonomous(group = "9_Tests")
public class ActionRunnerRenderTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		Client client = new BaseMapClient(telemetry);
		Actions.runAction(new ThreadedAction(
				new SleepingAction(1024),
				new StatementAction(client::update)
		), new ClientRender(client));

		client.update();
		waitForStart();
	}
}
