package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import static org.firstinspires.ftc.teamcode.Utils.FrontLiftLocation.down;
import static org.firstinspires.ftc.teamcode.Utils.FrontLiftLocation.middle;

//12.5*17 (in)
//6*9
//arm和claw的值需要确定
@Config
@Autonomous(name = "Right 右", group = "drive")
public class Right extends LinearOpMode {

    Utils utils = new Utils();

    @Override
    public void runOpMode() throws InterruptedException {

        utils.init(hardwareMap,telemetry);
        utils.liftMotorInit("frontLift","lift","touch");
        utils.servoInit("arm","clip","rotate","turn","claw");
        utils.armOperation1(true);
        utils.claw_rotate_rst(true);
        utils.motorInit();

        //定义点位
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d blueLeft = new Pose2d(-12,58,Math.toRadians(90));
        Pose2d forward = new Pose2d(-10,31,Math.toRadians(90));
        Pose2d toYellowSample = new Pose2d(-53.2,41.1,Math.toRadians(-90));
        Pose2d toGetSecondYellowSample = new Pose2d(-66.1,42.2,Math.toRadians(-95));//44
        Pose2d toGetThirdYellowSample = new Pose2d(-69.5,41.5,Math.toRadians(-116));
        Pose2d toPark = new Pose2d(-68.5,52.5,Math.toRadians(-90));  //旧的


        //执行点位
        drive.setPoseEstimate(blueLeft);

        Trajectory left_put = drive.trajectoryBuilder(blueLeft)
                .lineToLinearHeading(forward)
                .build();
        Trajectory toPut = drive.trajectoryBuilder(left_put.end())
                .lineToLinearHeading(toYellowSample)
                .build();


        Trajectory toGetSecondYellow = drive.trajectoryBuilder(toPut.end())
                .lineToLinearHeading(toGetSecondYellowSample)
                .build();

        Trajectory toGetThirdYellow = drive.trajectoryBuilder(toGetSecondYellow.end())
                .lineToLinearHeading(toGetThirdYellowSample)
                .build();


        Trajectory Park = drive.trajectoryBuilder(toGetThirdYellow.end())
                .lineToLinearHeading(toPark)
                .build();

        telemetry.addData(">>>", "Robot Ready");
        telemetry.update();

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
            utils.armOperation1(false);
            sleep(200);
            utils.rearLiftPosition(RearLiftLocation.middle1);
            drive.followTrajectory(left_put); //去上挂
            utils.clipOperation(true);     //打开夹子
            sleep(150);

            utils.rearLiftPosition(RearLiftLocation.down1); //回电梯
            utils.armOperationR(true);       //翻转手臂
            //夹第一个
            drive.followTrajectory(toPut);    //去找夹第一黄块
            utils.frontLiftPosition(middle); //伸前电梯
           // sleep(300);                          //
            utils.claw_rotate_rst(false);  //前面夹子翻转下去、打开
            sleep(400);
            utils.clawOperation(false);      //夹住
            sleep(300);
            utils.claw_rotate(true);   //翻转上去
            sleep(250);
            utils.frontLiftPosition(down);//前电梯收回
            sleep(200);
            utils.clipOperation(false);   //夹住
            sleep(400);
            utils.clawOperation(true);   //打开
            sleep(150);
            //翻转手臂

            utils.armOperation1(false); //
            sleep(800);
            utils.clipOperation(true);
            sleep(350);
            utils.armOperationR(true);
            sleep(100);
            //去夹第二个
            drive.followTrajectory(toGetSecondYellow);  //去拿第二个块
            utils.armOperationR(true);
            utils.frontLiftPosition(middle); //伸前电梯
            sleep(300);                          //
            utils.claw_rotate_rst(false);    //翻转下去，打开
            sleep(500);
            utils.clawOperation(false);       //夹住
            sleep(400);
            utils.claw_rotate(true);         //翻转上去
            sleep(300);
            utils.frontLiftPosition(down);//前电梯收回
            sleep(300);
            utils.clipOperation(false);
            sleep(350);
            utils.clawOperation(true);
            sleep(300);




            utils.armOperation1(false); //
            sleep(800);
            utils.clipOperation(true);
            sleep(350);
            utils.armOperationR(true);
            sleep(100);
            //去夹第三个
            drive.followTrajectory(toGetThirdYellow);   //去拿第三个
            utils.frontLiftPosition(middle); //伸前电梯

            utils.claw_rotate_rst(false);  //前面夹子翻转下去、打开
            sleep(400);
            utils.clawOperation(false);      //夹住
            sleep(300);
            utils.claw_rotate(true);   //翻转上去
            sleep(250);
            utils.frontLiftPosition(down);//前电梯收回
            sleep(200);
            utils.clipOperation(false);   //夹住
            sleep(300);
            utils.clawOperation(true);   //打开
            sleep(350);

            utils.armOperation1(false); //
            sleep(800);
            utils.clipOperation(true);
            sleep(350);
            utils.armOperationR(true);
            sleep(100);

            drive.followTrajectory(Park);



                sleep(3000000);
        }
    }
}
