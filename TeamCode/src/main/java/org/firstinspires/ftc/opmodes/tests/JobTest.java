package org.firstinspires.ftc.opmodes.tests;

import static org.betastudio.ftc.job.Workflows.activeJob;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.job.Job;
import org.betastudio.ftc.job.implementation.TreeJob;
import org.betastudio.ftc.job.Workflows;
import org.betastudio.ftc.job.render.JobClientRender;
import org.betastudio.ftc.job.render.JobLogRender;
import org.betastudio.ftc.job.render.MultipleJobRender;
import org.betastudio.ftc.time.Timer;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

import java.util.concurrent.atomic.AtomicInteger;

@Autonomous
public class JobTest extends LinearOpMode {
	public AtomicInteger count  = new AtomicInteger();
	public Client        client;

	public Job buildJob(int index) {
		if (index == 0){
			return Workflows.newSteppedJob("print step", ()->client.putData("count",count.incrementAndGet()));
		} else {
			TreeJob build = new TreeJob();
			build.addDependency(buildJob(index-1));
			build.addDependency(Workflows.newSteppedJob("print step", ()->client.putData("count",count.incrementAndGet())));
			return build;
		}
	}

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		client = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.AUTOMATIC);
		client.update();

		activeJob(Workflows.newSteppedJob("print step", ()->client.putData("count",count.incrementAndGet())), new MultipleJobRender(new JobClientRender(client),new JobLogRender()));

		FtcLogTunnel.MAIN.save("JobTest" + Timer.getCurrentTime());

		sleep(Long.MAX_VALUE);
	}
}
