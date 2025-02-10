package org.firstinspires.ftc.opmodes.tests;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.dashboard.DashboardUtils;

@TestShelved
@Autonomous(group = "9_Tests")
public class DashboardUtilsTest extends LinearOpMode {
	public DashboardUtils dashboard = DashboardUtils.generateInstance();

	@Override
	public void runOpMode() throws InterruptedException {
		DashboardUtils.fetch();
		waitForStart();
		while (opModeIsActive()) {
			final TelemetryPacket packet = new TelemetryPacket();
			packet.put("time", getRuntime());
			DashboardUtils.sendTelemetryPacket(packet);
			dashboard.update();
		}
	}
}
