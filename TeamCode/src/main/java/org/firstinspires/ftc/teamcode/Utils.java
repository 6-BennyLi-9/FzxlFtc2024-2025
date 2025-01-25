package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.concurrent.Callable;


@Config
public class Utils {
    public static double turnUp= 0.02;
    public static double turnMiddle= 0.71;
    public static double turnDown= 0.82;
    public static double rotateOn= 0.83;
    public static double rotateTo= 0.4;
    public static double clawOn= 0.47;
    public static double clawOpen= 0.34;
    public static double clipOn= 0.28;
    public static double clipOpen = 0.76;
    public static double armUpL= 0.85;
    public static double armDownL= 0.07;
    public static double armUpR= 0.84;
    public static double armDownR= 0.33;
    public DcMotorEx leftFront = null;
    public DcMotorEx leftRear = null;
    public DcMotorEx rightFront = null;
    public DcMotorEx rightRear = null;

    public DcMotorEx frontLift = null;

    public DcMotorEx rearLift = null;

    public Servo arm = null;         //后电梯摆臂
    public Servo rotate = null;         //前电梯上旋转舵机
    public Servo clip = null;   //后电梯夹子
    public Servo claw = null;  //前电梯夹子
    public Servo turn = null;     //前电梯上，上下翻转
    public BNO055IMU imu = null;
    public TouchSensor touch = null;
    public HardwareMap hardwareMap = null;
    public Telemetry telemetry = null;


    public void init(HardwareMap h, Telemetry t) {
        hardwareMap = h;
        telemetry = t;

        imuInit();
    }
    public void imuInit(){
        imu=hardwareMap.get(BNO055IMU.class,"imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();//不可更改
        parameters.angleUnit= BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit= BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile="BNO055Calibration.json";
        parameters.loggingEnabled=true;
        parameters.loggingTag="IMU";
        parameters.accelerationIntegrationAlgorithm=new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }
    public double get_imuAngle(){//获得偏转角
        return imu.getAngularOrientation().firstAngle;
    }

    public void liftMotorInit(String frontlift, String lift, String touch) {

        frontLift = hardwareMap.get(DcMotorEx.class, frontlift);
        rearLift = hardwareMap.get(DcMotorEx.class, lift);
        this.touch = hardwareMap.get(TouchSensor.class, touch);

        frontLift.setDirection(DcMotorEx.Direction.REVERSE);
        frontLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        frontLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        rearLift.setDirection(DcMotorEx.Direction.FORWARD);
        rearLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        rearLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rearLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


        while (!this.touch.isPressed()) {
            this.rearLift.setPower(-0.2);
        }
        telemetry.addData("touch",this.touch.isPressed());
        telemetry.update();
        this.rearLift.setPower(0);
        this.rearLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.rearLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.frontLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.frontLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void motorInit(){
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        frontLift = hardwareMap.get(DcMotorEx.class, "frontLift");
        rearLift = hardwareMap.get(DcMotorEx.class, "lift");
    }

    public void servoInit( String arm, String clip, String rotate, String turn, String claw) {
        this.arm = hardwareMap.get(Servo.class, arm);
        this.clip = hardwareMap.get(Servo.class, clip);
        this.rotate = hardwareMap.get(Servo.class, rotate);
        this.turn = hardwareMap.get(Servo.class,turn);
        this.claw = hardwareMap.get(Servo.class, claw);

    }

    public void imuInit(String imu_name) {
        imu = hardwareMap.get(BNO055IMU.class, imu_name);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        //延时0.5秒，以确保imu正常工作
        try {
            Thread.sleep(1000);//单位：毫秒
        } catch (Exception ignored) {
        }
        imu.initialize(parameters);
    }

    public void sleepForMS(int time) {
        try {
            Thread.sleep(time);//单位：毫秒
        } catch (Exception ignored) {
        }
    }

    //前电梯
    int front_encoder_value = 0;
    private void setFrontLiftPosition(int val) {
        front_encoder_value = val;
    }

    public void frontLiftToPosition(int front_encoder_value) {
        final int max_position = 1800;
        final int bufVal = 50;
        if (front_encoder_value < max_position && front_encoder_value > 5) {
            if (frontLift.getCurrentPosition() < front_encoder_value - bufVal) {
                frontLift.setPower(1.0);
                while (frontLift.getCurrentPosition() < front_encoder_value - bufVal)
                    ;
            }
            if (frontLift.getCurrentPosition() > front_encoder_value + bufVal) {
                frontLift.setPower(-1.0);
                while (frontLift.getCurrentPosition() > front_encoder_value + bufVal)
                    ;
            }

            frontLift.setPower(0);
        }
    }



    public void frontLiftRst() {
        while (frontLift.getCurrentPosition() > 100) {
            frontLift.setPower(-1);
        }
        if (frontLift.getCurrentPosition() > 15) {
            frontLift.setPower(-0.5);
        }
        if (frontLift.getCurrentPosition() < 0) {
            frontLift.setPower(0.0);
        }
    }

    public enum FrontLiftLocation {
        up,
        middle,
        low,
        down
    }

    public void frontLiftPosition(FrontLiftLocation state) {
        if (state == FrontLiftLocation.down) {
            frontLiftRst();
        } else if (state == FrontLiftLocation.up) {
            frontLiftToPosition(1045);//2700,or+20
        }  else if (state == FrontLiftLocation.middle) {
            frontLiftToPosition(600);
        } else if (state == FrontLiftLocation.low) {
            frontLiftToPosition(177);//原920
        }
    }

   public void dcFrontLiftToPosition(int front_encoder_value){
        frontLift.setTargetPosition(front_encoder_value);
        frontLift.setTargetPositionTolerance(2);
        frontLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        frontLift.setPower(Math.signum(front_encoder_value-frontLift.getCurrentPosition()));
    }


    public void dcFrontLiftRst(){
        dcFrontLiftToPosition(0);
    }


    public void dcFrontLiftPosition(FrontLiftLocation state) {
        if (state == FrontLiftLocation.down) {
            dcFrontLiftRst();
        } else if (state == FrontLiftLocation.up) {
            dcFrontLiftToPosition(1065);//2700,or+20
        }  else if (state == FrontLiftLocation.middle) {
            dcFrontLiftToPosition(1000);
        } else if (state == FrontLiftLocation.low) {
            dcFrontLiftToPosition(179);//原920
        }
    }

    //后电梯
    int rear_encoder_value = 0;
    private void setRearLiftPosition(int val) {
        rear_encoder_value = val;
    }
    public void rearLiftToPosition(int rear_encoder_value) {
        final int max_position1 = 2900;
        final int bufVal1 = 50;
        if (rear_encoder_value < max_position1 && rear_encoder_value > 5) {
            if (rearLift.getCurrentPosition() < rear_encoder_value - bufVal1) {

                rearLift.setPower(1.0);
                while (rearLift.getCurrentPosition() < rear_encoder_value)
                    ;
            }
            if (rearLift.getCurrentPosition() > rear_encoder_value + bufVal1) {

                rearLift.setPower(-1.0);
                while (rearLift.getCurrentPosition() > rear_encoder_value)
                    ;
            }

            rearLift.setPower(0);

        }
    }


    public void rearLiftRst() {
        while (rearLift.getCurrentPosition() > 210) {
            rearLift.setPower(-1);
        }
        if (rearLift.getCurrentPosition() > 15) {
            rearLift.setPower(-0.5);
        }
        while (!touch.isPressed())
            ;

        rearLift.setPower(0);

        rearLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rearLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

    }



    public void rearLiftPosition(RearLiftLocation state) {
        if (state == RearLiftLocation.down1) {
            rearLiftRst();
        } else if (state == RearLiftLocation.up1) {
            rearLiftToPosition(2195);//放篮子
        }  else if (state == RearLiftLocation.middle1) {
            rearLiftToPosition(490);//挂样本
        } else if (state == RearLiftLocation.low1) {
            rearLiftToPosition(450);//原920
        }
    }

   public void armOperationR(boolean y) {
        if (y) {
            clip.setPosition(clipOpen);  //打开
            arm.setPosition(armDownR);  //不翻转0.11
            //claw.setPosition(0.26);  //前夹子打开
        } else {

            clip.setPosition(clipOn);  //夹住
            arm.setPosition(armUpR);  //翻转
        }
    }

    public void armOperationL(boolean y) {
        if (y) {
            clip.setPosition(clipOpen);  //打开
            arm.setPosition(armDownL);  //不翻转0.11

        } else {
            clip.setPosition(clipOn);  //夹住

            arm.setPosition(0.29);  //翻转
        }
    }
    public  void armOperation1(boolean y) {
        if (y) {

            arm.setPosition(0.20);  //不翻转
            clip.setPosition(clipOn);  //夹住0.55
            claw.setPosition(clawOpen);  //前夹子打开
        } else {

            clip.setPosition(clipOn);  //夹住
            arm.setPosition(armUpL); //翻转
            turn.setPosition(turnMiddle);
        }
    }

    public void clipOperation(boolean s) {
        this.clip.setPosition(s ? clipOpen : clipOn); //开/关
    }

    public void clawOperation(boolean s) {
        this.claw.setPosition(s ? clawOpen: clawOn); //开/关
    }
    public void turnOperation(boolean s) {
        this.turn.setPosition(s ? turnUp: turnDown);//上下
    }


    public void rotateOperation(boolean s) {
        this.rotate.setPosition(s ? 0.47 : 0.83);//垂直/水平

    }



    public void claw_rotate_rst(boolean x) {
        if (x) {
           claw.setPosition(clawOpen);  //打开0.26
           turn.setPosition(turnMiddle);   //翻下去
           rotate.setPosition(rotateOn);  //转正
        } else {
           claw.setPosition(clawOpen);  //打开0.26
           turn.setPosition(turnDown);   //翻下去
           rotate.setPosition(rotateOn);  //转正
       }
    }
    public void claw_rotate(boolean Y) {
        if (Y) {
            claw.setPosition(clawOn);  //夹住
            turn.setPosition(turnUp);   //翻上去
            rotate.setPosition(rotateOn); //转正0.1, 0.83
            clip.setPosition(clipOpen);   //夹子打开
        } else {
            claw.setPosition(clawOpen);  //打开0.26
           // turn.setPosition(turnDown);   //翻下去
            rotate.setPosition(rotateOn);  //旋转90度
        }
    }
   //IMU调整
   public static double allowErr = 5;
   
   public void angleCalibration(final double target,
                                @NonNull final Pose2d poseEst,
                                @NonNull SampleMecanumDrive drive) {
       runAction(() -> {
           final double ang = imu.getAngularOrientation().firstAngle;

           TelemetryPacket p = new TelemetryPacket();
           p.put("ang", ang);
           p.put("err", Math.abs(target - ang));
           FtcDashboard.getInstance().sendTelemetryPacket(p);

           if (Math.abs(target - ang) < Math.abs(360 - target + ang)) {
               if (ang > target + allowErr) {
                   simpleDrive(-0.5);
                   return true;
               } else if (ang < target - allowErr) {
                   simpleDrive(0.5);
                   return true;
               }
           } else {
               if (ang > target + allowErr) {
                   simpleDrive(0.5);
                   return true;
               } else if (ang < target - allowErr) {
                   simpleDrive(-0.5);
                   return true;
               }
           }
           simpleDrive(0);
           return false;
       });
       drive.setPoseEstimate(poseEst);
   }
    
   protected void simpleDrive(double angle){
       leftFront.setPower(-angle);
       leftRear.setPower(-angle);
       rightFront.setPower(angle);
       rightRear.setPower(angle);
   }
   protected void runAction(Callable<Boolean> c){
       while (true) {
           try {
               if (!c.call()) break;
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }
   }
}
