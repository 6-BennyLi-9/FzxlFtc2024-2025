package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import static org.firstinspires.ftc.teamcode.Utils.FrontLiftLocation.low;
import static org.firstinspires.ftc.teamcode.Utils.FrontLiftLocation.up;

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
        utils.liftMotorInit("frontLift", "lift", "touch");
        utils.servoInit("arm", "clip", "rotate", "turn", "claw");
        utils.armOperation1(true);
        utils.claw_rotate(false);
        utils.motorInit();


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d blueLeft = new Pose2d(36, 58, Math.toRadians(-90));
        Pose2d forward = new Pose2d(54.3, 54, Math.toRadians(-125));
        Pose2d toYellowSample = new Pose2d(54.8, 46.5, Math.toRadians(-85));//-90

        Pose2d toPutYellowSample = new Pose2d(53.9, 53.9, Math.toRadians(-125)); //
        Pose2d toGetSecondYellowSample = new Pose2d(44.5, 45.5, Math.toRadians(-85));//y49.2x64.2
        Pose2d toPutSecondYellowSample = new Pose2d(56, 52, Math.toRadians(-125));//150
        Pose2d toGetThirdYellowSample = new Pose2d(59, 46.5, Math.toRadians(-67));//150
        Pose2d toPutThirdYellowSample = new Pose2d(55.1, 53.1, Math.toRadians(-125));//150

        Pose2d GoToPark = new Pose2d(38, 4, Math.toRadians(-170));// 40，4


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
                .forward(10)
                .build();


        telemetry.addData(">>>", "Robot Ready");
        telemetry.update();

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;


        utils.rearLiftPosition(RearLiftLocation.up1);
        utils.dcFrontLiftPosition(up); //伸前电梯
        drive.followTrajectorySequence(left_put); //放预载
        utils.armOperation1(false);
        sleep(800);
        utils.armOperationL(true);     //打开夹子，翻转手臂
        sleep(100);
        drive.followTrajectory(toPut);    //去夹第一黄块
        utils.claw_rotate_rst(true);  //前面夹子翻转下去、打开
        utils.rearLiftPosition(RearLiftLocation.down1); //回电梯

        utils.claw_rotate_rst(false);  //前面夹子翻转下去、打开
        sleep(400); //500
        utils.clawOperation(false);      //夹住
        sleep(300);
        utils.claw_rotate(true);   //翻转上去
        sleep(700);
        utils.dcFrontLiftPosition(low);  //frontLiftPosition(low);
        sleep(300);
        utils.clipOperation(false);   //夹住
        sleep(200);
        utils.clawOperation(true);   //打开
        //sleep(150);

        drive.followTrajectory(toPutYellow);  //去放第一个块

        utils.rearLiftPosition(RearLiftLocation.up1);  //后电梯抬
        sleep(250);
        utils.armOperation1(false); //手臂翻转,同时将前夹子放下
        sleep(800);
        utils.clipOperation(true);//打开夹子
        sleep(300);
        utils.armOperationL(true);   //翻转回来，打开夹子
        sleep(100);
        utils.rearLiftPosition(RearLiftLocation.down1);  //回电梯
        utils.dcFrontLiftPosition(up); //frontLiftPosition(up); //伸前电梯

        drive.followTrajectory(toGetSecondYellow);       //去拿第二个块
        sleep(100);
        //
        utils.claw_rotate_rst(false);    //翻转下去，打开
        sleep(400);   //500
        utils.clawOperation(false);       //夹住
        sleep(400);
        utils.claw_rotate(true);         //翻转上去
        sleep(300);

        utils.dcFrontLiftPosition(low);
        sleep(300);//前电梯回位
        utils.clipOperation(false);
        sleep(350);
        utils.clawOperation(true);
        drive.followTrajectory(toPutSecondYellow);
        utils.rearLiftPosition(RearLiftLocation.up1);
        sleep(250);
        utils.armOperation1(false); ////手臂翻转,同时将前夹子放下
        sleep(800);
        utils.clipOperation(true);
        sleep(350);
        utils.armOperationL(true);
        sleep(100);
        utils.rearLiftPosition(RearLiftLocation.down1);
        utils.dcFrontLiftPosition(up); //伸前电梯

        drive.followTrajectorySequence(toGetThirdYellow);       //去拿第二个块
        sleep(50);

        utils.claw_rotate_rst(false);    //翻转下去，打开
        sleep(400);  //500
        utils.clawOperation(false);       //夹住
        sleep(400);
        utils.claw_rotate(true);         //翻转上去
        sleep(300);

        utils.dcFrontLiftPosition(low);
        sleep(300);//前电梯回位
        utils.clipOperation(false);
        sleep(350);
        utils.clawOperation(true);
        drive.followTrajectory(toPutThirdYellow);
        utils.rearLiftPosition(RearLiftLocation.up1);
        sleep(250);
        utils.armOperation1(false); //
        sleep(800);
        utils.clipOperation(true);
        sleep(350);
        utils.armOperationL(true);
        utils.rearLiftPosition(RearLiftLocation.down1);

        utils.dcFrontLiftPosition(low);
        drive.followTrajectorySequence(toPark);//去停靠


    }
}
