package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.telemetry.IntegralSelectElement;
import org.betastudio.ftc.util.ButtonProcessor;
import org.betastudio.ftc.util.message.IntegralSelectionPackage;
import org.firstinspires.ftc.teamcode.ButtonConfig;

@Autonomous(group = "9_Tests")
public class SelectionTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		final Client                   client = new BaseMapClient(telemetry);
		final IntegralSelectionPackage msg    = new IntegralSelectionPackage();
		final ButtonProcessor          b_a    = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor          b_b    = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor          b_x    = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);

		msg.add(new IntegralSelectElement("item1", () -> client.speak("item1 selected")));
		msg.add(new IntegralSelectElement("item2", () -> client.speak("item2 selected")));
		msg.add(new IntegralSelectElement("item3", () -> client.speak("item3 selected")));
		msg.add(new IntegralSelectElement("item4", () -> client.speak("item4 selected")));
		msg.add(new IntegralSelectElement("item5", () -> client.speak("item5 selected")));
		msg.add(new IntegralSelectElement("item6", () -> client.speak("item6 selected")));

		client.send(msg.convertToTelemetryMsg());
		client.update();
		waitForStart();
		while (opModeIsActive()) {
			b_a.sync(gamepad1.a);
			b_b.sync(gamepad1.b);
			b_x.sync(gamepad1.x);

			if (b_a.getEnabled()) {
				msg.select_prev();
			} else if (b_b.getEnabled()) {
				msg.select_next();
			}

			if (b_x.getEnabled()) {
				msg.submit();
			}

			client.send(msg.convertToTelemetryMsg());
			client.update();
		}
	}
}
