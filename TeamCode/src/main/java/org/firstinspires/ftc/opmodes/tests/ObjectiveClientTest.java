package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.ObjectiveClient;

public class ObjectiveClientTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		ObjectiveClient client = new ObjectiveClient(telemetry);
		client.setUpdateConfig(UpdateConfig.AUTOMATIC);

		client.putData("key1","val1");
	}
}
