package org.firstinspires.ftc.teamcode.teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.structure.DriveOption;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.Robot;

@TeleOp(name = "19419(using PID)",group = "Main")
public class TeleOpUsingPID extends TeleOpCore{
	@Override
	public void init() {
		DriveOption.setDriveUsingPID(true);

		HardwareConstants.sync(hardwareMap, true);
		HardwareConstants.chassisConfig();
		robot=new Robot();
		robot.registerGamepad(gamepad1,gamepad2);
		robot.initActions();
		TelemetryClient.registerInstance(new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()));

		TelemetryClient.getInstance()
				.addData("TPS","wait for start")
				.addData("time","wait for start");
	}
}
