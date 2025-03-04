package org.firstinspires.ftc.opmodes.tests;

import static org.betastudio.ftc.job.Workflows.activeJob;
import static org.betastudio.ftc.job.Workflows.newSteppedJob;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.job.TreeJob;

import java.util.concurrent.atomic.AtomicInteger;

@Autonomous
public class JobTest extends LinearOpMode {
	public TreeJob target = new TreeJob();
	public AtomicInteger count = new AtomicInteger();
	
	public void addPrint(){
		target.addDependency(newSteppedJob("print",()->{
			telemetry.addData("count", count.addAndGet(1));
			telemetry.update();
			sleep(1000);
		}));
	}
	
	@Override
	public void runOpMode() throws InterruptedException {
		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		telemetry.addLine("TEST LINE 1");
		telemetry.update();

		for (int i = 0 ; i < 10 ; i++) {
			addPrint();
		}

		waitForStart();

		telemetry.addLine("TEST LINE 2");
		telemetry.update();

		telemetry.addData("dependency count", target.getCount());
		telemetry.update();

		activeJob(target);
	}
}
