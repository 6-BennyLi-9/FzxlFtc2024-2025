package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.exboithpath.graphics.Pose;
import org.exboithpath.manager.DaemonDriveService;
import org.exboithpath.manager.DriveService;
import org.exboithpath.runner.RunnerStatus;
import org.firstinspires.ftc.teamcode.Global;

public class DriveTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		DriveService driveService = new DaemonDriveService(hardwareMap, Global.defaultThreadExecutor());
		driveService.setPoseEst(new Pose(0,0,0));
		driveService.setTargetIDLEStatus(RunnerStatus.CALIBRATING);

		waitForStart();

		driveService.runToPose(new Pose(20,20,0));
	}
}
