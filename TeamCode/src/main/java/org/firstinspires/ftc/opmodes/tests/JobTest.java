package org.firstinspires.ftc.opmodes.tests;

import static org.betastudio.ftc.job.Workflows.activeJob;
import static org.betastudio.ftc.job.Workflows.newSteppedJob;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.job.Job;
import org.betastudio.ftc.job.TreeJob;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;

import java.util.concurrent.atomic.AtomicInteger;

@Autonomous
public class JobTest extends LinearOpMode {
	public Job           target = new TreeJob();
	public AtomicInteger count  = new AtomicInteger();
	public Client        client;

	public void addPrint(){
		target.addDependency(newSteppedJob("print",()->{
			client.changeData("count", String.valueOf(count.incrementAndGet()));
			sleep(1000);
		}));
	}
	
	@Override
	public void runOpMode() throws InterruptedException {
		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		client = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.AUTO_UPDATE_WHEN_OPTION_PUSHED);
		client.putLine("TEST LINE 1");

		for (int i = 0 ; i < 10 ; i++) {
			addPrint();
		}

		waitForStart();

		client.putLine("TEST LINE 2");

		client.putData("dependency count", ((Interfaces.Countable) target).getCount());

		target = (Job) ((Interfaces.StoreRequired<?>) target).store();
		activeJob(target);
	}
}
