package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Sample extends LinearOpMode {

	@Override
	public void runOpMode() throws InterruptedException {
		TelemetryPacket packet=new TelemetryPacket();

		FtcDashboard.getInstance().sendTelemetryPacket(packet);
		FtcDashboard.getInstance().clearTelemetry();
	}
}
