package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.Annotations;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.ObjectiveClient;

@Autonomous
@Disabled
@Annotations.TestShelved
public class ObjectiveClientTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		final ObjectiveClient client = new ObjectiveClient(telemetry);
		client.setUpdateConfig(UpdateConfig.AUTOMATIC);

		client.putData("key1","val1");
	}
}
