package org.firstinspires.ftc.opmodes.tests;

import static org.betastudio.ftc.Annotations.TestShelved;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.button.ButtonConfig;
import org.betastudio.ftc.button.ButtonProcessorEx;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

/**
 * @noinspection deprecation
 */
@Disabled
@TestShelved
@TeleOp(group = "9_Tests")
public class ButtonProcessorTest extends LinearOpMode {
	public ButtonProcessorEx processor;

	@Override
	public void runOpMode() throws InterruptedException {
		processor = new ButtonProcessorEx(ButtonConfig.SINGLE_WHEN_PRESSED, () -> {
			telemetry.speak("reached run");
			FtcLogTunnel.MAIN.report("reached run");
		});

		while (! isStopRequested()) {
			processor.sync(gamepad1.a);
		}
	}
}
