package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.client.BranchThreadClient;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.ViewMode;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

public class FtcLogTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		final Client client =new BranchThreadClient(telemetry);
		client.configViewMode(ViewMode.LOG);
		FtcLogTunnel.clear();
		FtcLogTunnel.MAIN.report("Op In INIT");

		waitForStart();

		FtcLogTunnel.MAIN.report("Op In RUN");

		sleep(500);

		FtcLogTunnel.MAIN.report("Op Is DONE");

		sleep((long) 1.0e10);
	}
}
