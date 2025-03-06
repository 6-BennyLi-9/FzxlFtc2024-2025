package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.*;
import org.betastudio.ftc.ui.log.*;
import org.betastudio.ftc.util.*;

/** @noinspection deprecation */
public class ButtonProcessorTest extends LinearOpMode{
	public ButtonProcessorEx processor;

	@Override
	public void runOpMode() throws InterruptedException{
		processor = new ButtonProcessorEx(ButtonConfig.SINGLE_WHEN_PRESSED, () -> {
			telemetry.speak("reached run");
			FtcLogTunnel.MAIN.report("reached run");
		});

		while(!isStopRequested()){
			processor.sync(gamepad1.a);
		}
	}
}
