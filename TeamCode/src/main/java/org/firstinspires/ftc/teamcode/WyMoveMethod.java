package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

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
    }

    //获取三个编码轮的函数，将编码轮的值进行初始化
    public void extraEncoderInit(@NonNull DcMotor l, @NonNull DcMotor r, @NonNull DcMotor m) {//编码器init
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
    public final float maxSpeed = 1.0f;

    public void basicMoveThroughGamePad() {
        //根据控制板控制
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
    }
}