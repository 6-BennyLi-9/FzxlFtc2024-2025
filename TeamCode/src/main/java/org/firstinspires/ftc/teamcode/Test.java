package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//@Disabled
@Autonomous
public class Test extends LinearOpMode {
    Utils utils=new Utils();

    @Override
    public void runOpMode() throws InterruptedException {

        utils.init(hardwareMap,telemetry);
//        utils.motorInit();
        utils.liftMotorInit("frontLift","lift","touch");
        utils.servoInit("arm","clip","rotate","turn","claw");
        utils.armOperation1(true);
        utils.claw_rotate_rst(true);
        utils.motorInit();

        waitForStart();

//        Thread t= new Thread(()->{
//            while (!Thread.interrupted()){
//                telemetry.addData("encoder", utils.frontLift.getCurrentPosition());
//                telemetry.update();
//            }
//        });
//        t.start();

        SampleMecanumDrive drive=new SampleMecanumDrive(hardwareMap);
        utils.angleCalibration(90,new Pose2d(),drive);
//        sleep(5000);
//        utils.frontLiftPosition(low);
//        sleep(5000);
//        utils.frontLiftPosition(down);
//        sleep(5000);

        sleep(50000);
//        t.interrupt();
    }
}
