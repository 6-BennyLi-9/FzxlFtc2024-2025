package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.telemetry.SelectionTeleElement;
import org.betastudio.ftc.util.ButtonProcessor;
import org.betastudio.ftc.util.message.IntegralSelectionMsg;
import org.firstinspires.ftc.teamcode.ButtonConfig;

public class SelectionTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		final Client               client =new BaseMapClient(telemetry);
		final IntegralSelectionMsg msg    =new IntegralSelectionMsg();
		final ButtonProcessor b_a =new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor b_b =new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor b_x =new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);

		msg.add(new SelectionTeleElement("item1"));
		msg.add(new SelectionTeleElement("item2"));
		msg.add(new SelectionTeleElement("item3"));
		msg.add(new SelectionTeleElement("item4"));
		msg.add(new SelectionTeleElement("item5"));
		client.send(msg.convertToTelemetryMsg());
		client.update();
		waitForStart();
		while (opModeIsActive()){
			b_a.sync(gamepad1.a);
			b_b.sync(gamepad1.b);
			b_x.sync(gamepad1.x);

			if (b_a.getEnabled()){
				msg.select_prev();
			}else if (b_b.getEnabled()){
				msg.select_next();
			}

			if(b_x.getEnabled()){
				msg.submit();
			}

			client.send(msg.convertToTelemetryMsg());
			client.update();
		}
	}
}
