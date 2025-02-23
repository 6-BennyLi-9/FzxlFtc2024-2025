package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.exboithpath.graphics.Pose;
import org.exboithpath.manager.DaemonDriveService;
import org.exboithpath.manager.DriveService;
import org.exboithpath.runner.RunnerStatus;
import org.firstinspires.ftc.teamcode.Global;

import java.util.concurrent.ThreadPoolExecutor;

@Autonomous
public class DriveTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		ThreadPoolExecutor executor = Global.defaultThreadExecutor();
		Client client = new BaseMapClient(telemetry);
		DriveService drive = new DaemonDriveService(hardwareMap, executor);
		drive.setPoseEst(new Pose(0,0,0));
		drive.setTargetIDLEStatus(RunnerStatus.IDLE);

		waitForStart();

		executor.execute(()->{
			while (!isStopRequested()){
				client.changeData("runner status",drive.getRunnerStatus());
				client.changeData("pose", drive.getPoseEst());
			}
		});
		drive.runToPose(new Pose(20,20,0));

		sleep(Integer.MAX_VALUE);
	}
}
