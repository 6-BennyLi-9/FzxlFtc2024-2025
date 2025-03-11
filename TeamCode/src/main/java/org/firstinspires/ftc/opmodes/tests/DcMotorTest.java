package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.betastudio.ftc.Annotations;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Local;

import java.util.concurrent.ExecutorService;

@Annotations.TestDoneSuccessfully
@Disabled
@Deprecated
@Autonomous
public class DcMotorTest extends LinearOpMode {
	public Client          client;
	public DcMotorEx       lift;
	public ExecutorService service;

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.setAutoClear(false);
		telemetry.update();
		lift =hardwareMap.get(DcMotorEx.class, "rightLift");
		service = Global.defaultThreadExecutor();

		Telemetry.Item liftPosition = telemetry.addData("rightLift position", null);

		lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		lift.setDirection(DcMotorSimple.Direction.REVERSE);

		waitForStart();

		service.execute(()->{
			while (opModeIsActive()){
				telemetry.update();
				Local.sleep(50);
			}
		});
		service.execute(()->{
			while (opModeIsActive()){
				liftPosition.setValue(lift.getCurrentPosition());
			}
		});

		lift.setTargetPosition(500);
		lift.setTargetPositionTolerance(10);
		lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		telemetry.addLine("Reach Line 1");
		lift.setPower(1);
		telemetry.addLine("Reach Line 2");

		Local.sleep(Long.MAX_VALUE);
		FtcLogTunnel.MAIN.report("reported line");
	}
}
