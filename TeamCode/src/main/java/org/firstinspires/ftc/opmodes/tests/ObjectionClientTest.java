package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.client.ObjectionClient;
import org.firstinspires.ftc.opmodes.teleops.TeleOpCore;

@TeleOp
@Deprecated
@Disabled
public class ObjectionClientTest extends TeleOpCore {
	@Override
	public void op_init() {
		super.op_init();
		client=new ObjectionClient(telemetry);
		robot.fetchClient(client);
	}
}
