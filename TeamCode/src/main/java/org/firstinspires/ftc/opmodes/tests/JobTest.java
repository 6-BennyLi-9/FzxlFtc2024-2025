package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.job.Step;
import org.betastudio.ftc.job.TreeJob;

import java.util.concurrent.atomic.AtomicInteger;

@Autonomous
public class JobTest extends LinearOpMode {
	public TreeJob target = new TreeJob();
	public AtomicInteger count = new AtomicInteger();
	
	public void addPrint(){
		target.addDependency(new Step(()->{
			telemetry.addData("count", count.addAndGet(1));
			telemetry.update();
		}));
	}
	
	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.setAutoClear(false);

		for (int i = 0 ; i < 10 ; i++) {
			addPrint();
		}

		telemetry.addData("dependency count", target.getCount());
		telemetry.update();
	}
}
