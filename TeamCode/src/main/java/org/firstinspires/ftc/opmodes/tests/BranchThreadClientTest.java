package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.client.BranchThreadClient;
import org.firstinspires.ftc.opmodes.teleops.TeleOpCore;

@TeleOp(group = "9_Beta")
@Disabled
@TestDone
public class BranchThreadClientTest extends TeleOpCore {
	@Override
	public void op_init() {
		super.op_init();
		client=new BranchThreadClient(telemetry,60);
		robot.fetchClient(client);
	}

	@Override
	public void op_end() {
		super.op_end();
		assert client instanceof BranchThreadClient;
		((BranchThreadClient) client).closeTask();
	}
}
