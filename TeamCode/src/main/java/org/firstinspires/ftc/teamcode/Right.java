package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.pushIn;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;


//12.5*17 (in)
//6*9
//arm和claw的值需要确定
@Autonomous(name = "Right", group = "drive",preselectTeleOp = "挂样本")
public class Right extends LinearOpMode {
    Utils utils = new Utils();

    @Override
    public void runOpMode() throws InterruptedException {
        utils.init(hardwareMap,telemetry);
        utils.liftMotorInit("leftLift","rightLift","touch");
        utils.servoInit("arm","clip","rotate","turn","claw", "upTurn", "leftPush", "rightPush");
        utils.imuInit();
        utils.armOperation1(true);
        utils.claw_rotate(false);
        utils.setPushPose(pushIn); //收前电梯
        utils.motorInit();


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d blueRight = new Pose2d(-12,58,Math.toRadians(-90));
        Pose2d forward = new Pose2d(-6,33,Math.toRadians(-90));

        Pose2d toGetSecondSample = new Pose2d(-43.1,53,Math.toRadians(-90)); //
        Pose2d toUpSecondBlueSample = new Pose2d(-8,30,Math.toRadians(-90));//44

        Pose2d toBlueSample = new Pose2d(-40,30,Math.toRadians(-90)); //-95

        Pose2d toGetThirdBlueSample = new Pose2d(-43,55,Math.toRadians(-90));

        Pose2d toUpThirdSample = new Pose2d(-10.5,33,Math.toRadians(-90));

        Pose2d toTurn = new Pose2d(-12,37.5,Math.toRadians(-90));
        Pose2d toPark = new Pose2d(-49,55,Math.toRadians(-90));

        drive.setPoseEstimate(blueRight);

        TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
                .lineToLinearHeading(forward)
                .forward(7)
                .build();
        TrajectorySequence toGetSecondBlue = drive.trajectorySequenceBuilder(left_put.end())
                .lineToLinearHeading(toGetSecondSample)
                .back(2.0)
                .build();

        TrajectorySequence toUpSecondBlue = drive.trajectorySequenceBuilder(toGetSecondBlue.end())
                .lineToLinearHeading(toUpSecondBlueSample)
                .forward(5.0)
                .build();

        TrajectorySequence toPut = drive.trajectorySequenceBuilder(left_put.end())
                .lineToLinearHeading(toBlueSample)
                .forward(23)
                .strafeRight(10)
                .back(37)
                .forward(35)
                .strafeRight(10.8)
                .back(37)   //推第二个
                //.back(37)
                // .strafeLeft(7)
                // .forward(37)
                //.strafeRight(15)
                .strafeLeft(10)
                //.turn(Math.toRadians(8))
                .back(9.0)
                .build();
        TrajectorySequence toGetThirdBlue = drive.trajectorySequenceBuilder(toUpSecondBlue.end())
                .lineToLinearHeading(toGetThirdBlueSample)
                .back(3.1)
                .build();

        TrajectorySequence ToUpThirdBlue = drive.trajectorySequenceBuilder(toGetThirdBlue.end())
                .lineToLinearHeading(toUpThirdSample)
                .forward(6.5)
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


        utils.setRearLiftPosition(RearLiftLocation.middle);
        utils.armOperationR(false);
        drive.followTrajectorySequence(left_put); //去上挂
        utils.clipOperation(true);
        utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
        sleep(50);
        utils.armOperationR(true);       //翻转手臂
        //夹第一个
        drive.followTrajectorySequence(toGetSecondBlue); //去推第一样本
        sleep(500);
        utils.clipOperation(false);    //夹住第一个
        sleep(400);
        utils.setRearLiftPosition(RearLiftLocation.middle);
        utils.armOperationR(false);
        drive.followTrajectorySequence(toUpSecondBlue);  //上挂第二个样本
        sleep(100);
        utils.clipOperation(true);
        utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
        sleep(50);
        utils.armOperationR(true);
        //去夹第二个
        //
        drive.followTrajectorySequence(toPut);
        sleep(550);
        utils.clipOperation(false);
        sleep(400);
        utils.setRearLiftPosition(RearLiftLocation.middle);
        utils.armOperationR(false);
        drive.followTrajectorySequence(ToUpThirdBlue);  //上挂第二个样本
        sleep(100);//150
        utils.clipOperation(true);
        utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
        sleep(50);
        utils.armOperationR(true);
        //去拿第三个
        drive.followTrajectorySequence(Park);
        utils.claw_rotate(true);

        sleep(Long.MAX_VALUE);
    }
}
