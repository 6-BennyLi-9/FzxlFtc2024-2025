package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class WyMoveMethod {
    public DcMotor leftFront = null;
    public DcMotor leftRear = null;
    public DcMotor rightFront = null;
    public DcMotor rightRear = null;

    private BNO055IMU imu = null;
    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;
    private Gamepad gamepad1 = null;


    public void init(HardwareMap h, Telemetry t, Gamepad g1, Gamepad g2) {
        hardwareMap = h;
        telemetry = t;
        gamepad1 = g1;
    }

    public void motorInit(String lf, String lr, String rf, String rr) {
        leftFront = hardwareMap.get(DcMotor.class, lf);
        leftRear = hardwareMap.get(DcMotor.class, lr);
        rightFront = hardwareMap.get(DcMotor.class, rf);
        rightRear = hardwareMap.get(DcMotor.class, rr);


        leftFront.setDirection(DcMotor.Direction.REVERSE);//try
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightRear.setDirection(DcMotor.Direction.FORWARD);


        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//stop
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//init
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

    public void imuInit(String imu_name) {//感知器init
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
            Thread.sleep(500);
        } catch (Exception ignored) {
        }
        imu.initialize(parameters);
//        Position p = new Position();
//        Velocity v = new Velocity();
        //imu.startAccelerationIntegration(p,v,5);
    }

    //获取三个编码轮的函数，将编码轮的值进行初始化
    public void extraEncoderInit(DcMotor l, DcMotor r, DcMotor m) {//编码器init

        l.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        r.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        l.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        r.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //更换新的机器时需要按照新的编码器接口来改变下面各个参数
    public DcMotor get_left_encoder() {
        return leftFront;
    }

    public DcMotor get_right_encoder() {
        return leftRear;
    }

    public DcMotor get_mid_encoder() {
        return rightFront;
    }


    public void showAngle() {
        Orientation angle;
        angle = imu.getAngularOrientation();
        telemetry.addData("第1个角", angle.firstAngle);
        telemetry.addData("第2个角", angle.secondAngle);
        telemetry.addData("第3个角", angle.thirdAngle);
    }

    private double targetAngle = 0;
    public float maxSpeed = 1.0f;
    private boolean lbFlag = false, rbFlag = false;//左右bumper是否按下标志，以确保每次按下之后只调整一次角度

    public void basicMoveThroughGamePad() {
        //根据控制板控制
        //if(gamepad1.right_bumper){maxSpeed=0.4f;}
        //if(gamepad1.left_bumper){maxSpeed=1.0f;}
        float x = gamepad1.left_stick_x * maxSpeed;   //原值0.6f
        float y = gamepad1.left_stick_y * maxSpeed;  //原值0.6f
        float turn = gamepad1.right_stick_x * maxSpeed;

        if(gamepad1.left_bumper){
            turn-= 0.1F;
        }else if(gamepad1.right_bumper){
            turn+= 0.1F;
        }

        turn+=(gamepad1.right_trigger-gamepad1.left_trigger)*0.4F;

        //double speedModify = -gyroModifyPID(); //PID参数
        leftFront.setPower(y - x - turn);
        leftRear.setPower(y + x - turn);
        rightFront.setPower(y + x + turn);
        rightRear.setPower(y - x + turn);

       /* final double angleIncreaseCoefficient = 5.5;//用来转弯的参数，0-10
//        final double angleIncreaseCoefficient = 5.5;
        targetAngle += angleIncreaseCoefficient * gamepad1.left_trigger;
        targetAngle -= angleIncreaseCoefficient * gamepad1.right_trigger;
        targetAngle -= angleIncreaseCoefficient * gamepad1.right_stick_x;
        if (gamepad1.left_bumper) {
            if (!lbFlag) {
                lbFlag = true;
                targetAngle += 6;//原是6，为天的角度数
            }
        } else
            lbFlag = false;
        if (gamepad1.right_bumper) {
            if (!rbFlag) {
                rbFlag = true;
                targetAngle -= 6;
            }
        } else
            rbFlag = false;
        if (gamepad1.b)
            targetAngle = 0;
        if (targetAngle > 180)
            targetAngle -= 360;
        if (targetAngle < -180)
            targetAngle += 360;
    }

    public void showEncoders() {
        Integer lf, lr, rf, rr, l, m, r;

        //获取底盘四个电机的编码器和给电Power
        //lf = leftFront.getCurrentPosition();
        // rf = rightFront.getCurrentPosition();
        // rr = rightRear.getCurrentPosition();
        //lr = leftRear.getCurrentPosition();

        //获取底盘四个电机的编码器状态
        lf = leftFront.getCurrentPosition();
        rf = rightFront.getCurrentPosition();
        rr = rightRear.getCurrentPosition();
        lr = leftRear.getCurrentPosition();

        //获取外接编码器的值
        l = left.getCurrentPosition();
        m = mid.getCurrentPosition();
        r = -right.getCurrentPosition();

        telemetry.addData("左前轮", lf);
        telemetry.addData("左后轮", lr);
        telemetry.addData("右前轮", rf);
        telemetry.addData("右后轮", rr);
        telemetry.addData("左编码器", l);
        telemetry.addData("中间编码器", m);
        telemetry.addData("右编码器", r);
    }



    public double position_x, position_y;
    private void positionUpdate() {
    }

    public void turnToAngle(float angle) {
        targetAngle = angle;
    }
    public void turnToAngleImmediately(float angle) {
        targetAngle = angle;
        waitTillAngle();
    }
    public void turnAngle(double angle) {
        targetAngle += angle;
        if (targetAngle > 180)
            targetAngle -= 360;
        if (targetAngle < -180)
            targetAngle += 360;
    }
    public void turnAngleImmediately(double angle) {
        turnAngle(angle);
        waitTillAngle();
    }
    private void waitTillAngle() {
        double speed = gyroModifyPID();
        while (Math.abs(speed) > 0.05) {
            leftRear.setPower(-speed);
            leftFront.setPower(-speed);
            rightRear.setPower(speed);
            rightFront.setPower(speed);
            speed = gyroModifyPID();
        }
        speed = 0;
        leftRear.setPower(-speed);
        leftFront.setPower(-speed);
        rightRear.setPower(speed);
        rightFront.setPower(speed);
    }


    private double integralAngle = 0;//pid变量
    private double lastAngleErr = 0;


    private double gyroModifyPID() {
        final double Kp = -0.1, Ki = 0, Kd = 0.04;//pid参数-0.145 0  0.04
        //final double Kp = 0.45, Ki = -0.2, Kd = 0.05;//pid参数
        double proportionAngle, differentiationAngle;
        Orientation angle;
        angle = imu.getAngularOrientation();
        proportionAngle = targetAngle - angle.firstAngle;//计算角度误差，作为比例误差
        if(proportionAngle<-180)
            proportionAngle+=360;
        else if(proportionAngle>180)
            proportionAngle-=360;
        differentiationAngle = proportionAngle - lastAngleErr;//微分
        integralAngle = integralAngle + proportionAngle;//积分
        lastAngleErr = proportionAngle;
        //telemetry.addData("mjj", targetAngle);
        return (Kp * proportionAngle + Ki * integralAngle + Kd * differentiationAngle) / 15;
    }

        */
    }
}