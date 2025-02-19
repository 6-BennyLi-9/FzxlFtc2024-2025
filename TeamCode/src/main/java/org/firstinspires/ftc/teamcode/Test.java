package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

@Disabled
@Autonomous(group = "zzz")
public class Test extends LinearOpMode {
	public Utils utils=new Utils();

	@Override
	public void runOpMode() throws InterruptedException {
		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("leftLift", "rightLift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.motorInit();
		utils.imuInit();

		waitForStart();

		utils.setPushPose(0.1);

		sleep(1000);

		utils.angleCalibration(90, new Pose2d(), new SampleMecanumDrive(hardwareMap));

		sleep(50000);
	}
}
