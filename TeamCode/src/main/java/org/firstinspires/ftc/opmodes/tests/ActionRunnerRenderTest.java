package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.Annotations;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.render.ClientRender;
import org.betastudio.ftc.action.utils.AssembledAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;

@Annotations.TestDoneSuccessfully
@Disabled
@Autonomous(group = "9_Tests")
public class ActionRunnerRenderTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		final Client client = new BaseMapClient(telemetry);
		Actions.runAction(new AssembledAction(new SleepingAction(1024), new StatementAction(client::update)), new ClientRender(client));

		client.update();
		waitForStart();
	}
}
