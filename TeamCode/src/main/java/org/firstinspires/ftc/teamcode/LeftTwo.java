package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.Utils.pushIn;
import static org.firstinspires.ftc.teamcode.Utils.pushOut;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

//12.5*17 (in)
//6*9
//arm和claw的值需要确定
@Config
@Autonomous(name = "Left 左停靠", group = "drive", preselectTeleOp = "放篮子")
public class LeftTwo extends LinearOpMode {

	Utils utils = new Utils();

	@Override
	public void runOpMode() throws InterruptedException {
		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("leftLift", "rightLift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.armOperation1(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
		utils.motorInit();
		utils.imuInit();



		SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
		Pose2d blueLeft = new Pose2d(36, 58, Math.toRadians(-90));
		Pose2d forward = new Pose2d(55, 54.5, Math.toRadians(-125));
		Pose2d toYellowSample = new Pose2d(54, 47.1, Math.toRadians(-90));//-90

		Pose2d toPutYellowSample = new Pose2d(53.9, 53.0, Math.toRadians(-125)); //
		Pose2d toGetSecondYellowSample = new Pose2d(47, 47, Math.toRadians(-93)); //y49.2x64.2
		Pose2d toPutSecondYellowSample = new Pose2d(56, 50.9, Math.toRadians(-125));//150
		Pose2d toGetThirdYellowSample = new Pose2d(58.3, 46.5, Math.toRadians(-78));//150
		Pose2d toPutThirdYellowSample = new Pose2d(55.1, 51.8, Math.toRadians(-125));//150

		Pose2d GoToPark = new Pose2d(34, 4, Math.toRadians(-180));// 40，4


		drive.setPoseEstimate(blueLeft);

		TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueLeft)
				.lineToLinearHeading(forward)

				.build();
		Trajectory toPut = drive.trajectoryBuilder(left_put.end())
				.lineToLinearHeading(toYellowSample)
				.build();

		Trajectory toPutYellow = drive.trajectoryBuilder(toPut.end())
				.lineToLinearHeading(toPutYellowSample)
				.build();
		Trajectory toGetSecondYellow = drive.trajectoryBuilder(toPutYellow.end())
				.lineToLinearHeading(toGetSecondYellowSample)
				.build();
		Trajectory toPutSecondYellow = drive.trajectoryBuilder(toGetSecondYellow.end())
				.lineToLinearHeading(toPutSecondYellowSample)
				.build();
		TrajectorySequence toGetThirdYellow = drive.trajectorySequenceBuilder(toPutSecondYellow.end())

				.lineToLinearHeading(toGetThirdYellowSample)
				.build();
		Trajectory toPutThirdYellow = drive.trajectoryBuilder(toGetThirdYellow.end())
				.lineToLinearHeading(toPutThirdYellowSample)
				.build();
		TrajectorySequence toPark = drive.trajectorySequenceBuilder(toPutThirdYellow.end())
				.lineToLinearHeading(GoToPark)
				.forward(15)
				.build();


		telemetry.addData(">>>", "Robot Ready");
		telemetry.update();

		telemetry.addLine("Waiting for start");

		telemetry.update();

		waitForStart();

		if (isStopRequested()) return;


		utils.rearLiftPosition(RearLiftLocation.up1);
		utils.setPushPose(pushOut); //伸前电梯
		drive.followTrajectorySequence(left_put); //放预载
		utils.armOperation1(false);
		sleep(400);
		utils.clipOperation(true);
		sleep(200);

		drive.followTrajectory(toPut);    //去夹第一黄块
		sleep(100);
		utils.claw_rotate_rst(true);  //前面夹子翻转下去、打开
		utils.rearLiftPosition(RearLiftLocation.down1); //回电梯
		utils.armOperationL(true);

		utils.claw_rotate_rst(false);  //前面夹子翻转下去、打开
		sleep(200); //500
		utils.clawOperation(false);      //夹住
		sleep(200);
		utils.claw_rotate(true);   //翻转上去
		//sleep(300);
		utils.setPushPose(pushIn);  //前电梯收回
		sleep(200);
		utils.armOperationL(false);     //打开夹子，下降去夹
		sleep(200);
		utils.clipOperation(false);   //夹住
		sleep(200);
		utils.clawOperation(true);   //打开
		//sleep(150);

		drive.followTrajectory(toPutYellow);  //去放第一个块

		utils.rearLiftPosition(RearLiftLocation.up1);  //后电梯抬

		utils.armOperation1(false); //手臂翻转,同时将前夹子放下
		sleep(400);
		utils.clipOperation(true);//打开夹子
		sleep(200);
		utils.armOperationL(true);   //翻转回来，打开夹子
		//sleep(100);

		utils.setPushPose(pushOut); //frontLiftPosition(up); //伸前电梯

		drive.followTrajectory(toGetSecondYellow);       //去拿第二个块
		sleep(100);
		utils.rearLiftPosition(RearLiftLocation.low1); //回电梯
		utils.rearLiftPosition(RearLiftLocation.down1); //回电梯//
		utils.claw_rotate_rst(false);    //翻转下去，打开
		sleep(200);   //500
		utils.clawOperation(false);       //夹住
		sleep(300);
		utils.claw_rotate(true);         //翻转上去
		sleep(200);

		utils.setPushPose(pushIn);
		sleep(200);
		utils.armOperationL(false);     //打开夹子，下降去夹
		sleep(200);
		utils.clipOperation(false);
		sleep(350);
		utils.clawOperation(true);
		drive.followTrajectory(toPutSecondYellow);
		utils.rearLiftPosition(RearLiftLocation.up1);
		//sleep(250);
		utils.armOperation1(false); ////手臂翻转,同时将前夹子放下
		sleep(400);
		utils.clipOperation(true);
		sleep(200);
		utils.armOperationL(true);
		//sleep(100);

		utils.setPushPose(pushOut); //伸前电梯

		drive.followTrajectorySequence(toGetThirdYellow);       //去拿第三个块
		sleep(100);
		utils.rearLiftPosition(RearLiftLocation.low1); //回电梯
		utils.rearLiftPosition(RearLiftLocation.down1); //回电梯/

		utils.claw_rotate_rst1(false);    //翻转下去，打开
		sleep(300);  //500
		utils.clawOperation(false);       //夹住
		sleep(300);
		utils.claw_rotate(true);         //翻转上去
		sleep(300);

		utils.setPushPose(pushIn);
		sleep(200);//前电梯回位
		utils.armOperationL(false);     //打开夹子，下降去夹
		sleep(300);//前电梯回位
		utils.clipOperation(false);
		sleep(350);
		utils.clawOperation(true);
		drive.followTrajectory(toPutThirdYellow);
		utils.rearLiftPosition(RearLiftLocation.up1);

		utils.armOperation1(false); //
		sleep(400);
		utils.clipOperation(true);
		sleep(200);




		drive.followTrajectorySequence(toPark);//去停靠
		utils.rearLiftPosition(RearLiftLocation.low1); //回电梯
		utils.rearLiftPosition(RearLiftLocation.down1);



	}
}
