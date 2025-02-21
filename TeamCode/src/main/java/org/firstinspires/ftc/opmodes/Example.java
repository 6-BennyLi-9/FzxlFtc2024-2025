package org.firstinspires.ftc.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Example extends LinearOpMode {
	public Client client;
	public ExecutorService executor;

	@Override
	public void runOpMode() throws InterruptedException {
		client = new BaseMapClient(telemetry);
		executor=new ThreadPoolExecutor(8,16,1, TimeUnit.SECONDS, new ArrayBlockingQueue <>(16), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
		DcMotorEx rightLift = hardwareMap.get(DcMotorEx.class, "rightLift");

		waitForStart();

		executor.execute(()->{
			while (opModeIsActive()){
				client.changeData("pose",rightLift.getCurrentPosition());
			}
		});

		while (opModeIsActive()){
			rightLift.setPower(gamepad1.left_stick_y);
			client.changeData("power",rightLift.getPower());
		}
	}
}
