package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.client.ObjectionClient;
import org.firstinspires.ftc.opmodes.teleops.TeleOpCore;

@TeleOp(group = "9_Tests")
@Deprecated
@Disabled
public class ObjectionClientTest extends TeleOpCore {
	/** @noinspection deprecation*/
	@Deprecated
	@Override
	public void op_init() {
		super.op_init();
		client=new ObjectionClient(telemetry);
		robot.fetchClient(client);
	}
}
