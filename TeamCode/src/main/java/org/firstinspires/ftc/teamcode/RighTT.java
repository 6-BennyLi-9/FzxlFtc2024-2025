package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


//12.5*17 (in)
//6*9
//arm和claw的值需要确定
@Config
@Autonomous(name = "Right-2上挂", group = "drive",preselectTeleOp = "挂样本")
public class RighTT extends LinearOpMode {

    Utils utils = new Utils();

    @Override
    public void runOpMode() throws InterruptedException {
        utils.init(hardwareMap,telemetry);
        utils.liftMotorInit("frontLift","lift","touch");
        utils.servoInit("arm","clip","rotate","turn","claw");
        utils.imuInit();
        utils.armOperation1(true);
        utils.claw_rotate(false);
        utils.motorInit();


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d blueRight = new Pose2d(-12,58,Math.toRadians(90));
        Pose2d forward = new Pose2d(-6,33,Math.toRadians(90));
        Pose2d toBlueSample = new Pose2d(-40,30,Math.toRadians(95));

        Pose2d toGetSecondSample = new Pose2d(-43.1,53,Math.toRadians(95)); //
        Pose2d toUpSecondBlueSample = new Pose2d(-8,30,Math.toRadians(90));//44

        Pose2d toGetThirdBlueSample = new Pose2d(-43,55,Math.toRadians(90));

        Pose2d toUpThirdSample = new Pose2d(-10,33,Math.toRadians(90));

        Pose2d toTurn = new Pose2d(-12,37.5,Math.toRadians(90));
        Pose2d toPark = new Pose2d(-46,55.5,Math.toRadians(120));



        drive.setPoseEstimate(blueRight);

        TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
                .lineToLinearHeading(forward)
                .back(7)
                .build();
        TrajectorySequence toGetSecondBlue = drive.trajectorySequenceBuilder(left_put.end())
                .lineToLinearHeading(toGetSecondSample)
                .forward(4)
                .build();
        TrajectorySequence toPut = drive.trajectorySequenceBuilder(left_put.end())
                .lineToLinearHeading(toBlueSample)
                .back(25)
                .strafeLeft(10)
                .forward(38)
                .back(36)
                .strafeLeft(11.2)
                .forward(38)   //推第二个
                //.back(37)
               // .strafeLeft(7)
               // .forward(37)
                //.strafeRight(15)
                .strafeRight(10)
                .turn(Math.toRadians(8))
                .forward(11)
                .build();
        TrajectorySequence toUpSecondBlue = drive.trajectorySequenceBuilder(toGetSecondBlue.end())
                .lineToLinearHeading(toUpSecondBlueSample)
                .back(3.1)
                .build();

        TrajectorySequence toGetThirdBlue = drive.trajectorySequenceBuilder(toUpSecondBlue.end())
                .lineToLinearHeading(toGetThirdBlueSample)
                .forward(7)
                .build();

        TrajectorySequence ToUpThirdBlue = drive.trajectorySequenceBuilder(toGetThirdBlue.end())
                .lineToLinearHeading(toUpThirdSample)
                .back(3.5)
                .build();
        TrajectorySequence Park= drive.trajectorySequenceBuilder(ToUpThirdBlue.end())
                .lineToLinearHeading(toPark)
                .build();


        telemetry.addData(">>>", "Robot Ready");
        telemetry.update();

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;


        utils.rearLiftPosition(RearLiftLocation.middle1);
        utils.armOperationR(false);
        drive.followTrajectorySequence(left_put); //去上挂
        utils.rearLiftPosition(RearLiftLocation.down1); //回电梯
        utils.armOperationR(true);       //翻转手臂
        //夹第一个
        drive.followTrajectorySequence(toGetSecondBlue); //去推第一样本
        sleep(500);
        utils.clipOperation(false);    //夹住第一个
        sleep(400);
        utils.rearLiftPosition(RearLiftLocation.middle1);
        utils.armOperationR(false);
        drive.followTrajectorySequence(toUpSecondBlue);  //上挂第二个样本
        sleep(150);
        utils.rearLiftPosition(RearLiftLocation.down1); //回电梯
        utils.armOperationR(true);
        //去夹第二个
        //
        drive.followTrajectorySequence(toPut);
        sleep(550);
        utils.clipOperation(false);
        sleep(400);
        utils.rearLiftPosition(RearLiftLocation.middle1);
        utils.armOperationR(false);
        drive.followTrajectorySequence(ToUpThirdBlue);  //上挂第二个样本
        sleep(150);
        utils.rearLiftPosition(RearLiftLocation.down1); //回电梯
        utils.armOperationR(true);
        //去拿第三个
        drive.followTrajectorySequence(Park);
        utils.dcFrontLiftPosition(Utils.FrontLiftLocation.low);


    }
}
