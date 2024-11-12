package org.firstinspires.ftc.teamcode.roadrunner.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

/**
 * This is a simple teleop routine for debugging your motor configuration.
 * Pressing each of the buttons will power its respective motor.
 * <p>
 * Button Mappings:
 * <p>
 * Xbox/PS4 Button - Motor
 * <p>
 *   X / ▢         - Front Left
 * <p>
 *   Y / Δ         - Front Right
 * <p>
 *   B / O         - Rear  Right
 * <p>
 *   A / X         - Rear  Left
 * <p>
 *                                    The buttons are mapped to match the wheels spatially if you
 * <p>
 *                                    were to rotate the gamepad 45deg°. x/square is the front left
 * <p>
 *                    ________        and each button corresponds to the wheel as you go clockwise
 * <p>
 *                   / ______ \
 * <p>
 *     ------------.-'   _  '-..+              Front of Bot
 * <p>
 *              /   _  ( Y )  _  \                  ^
 * <p>
 *             |  ( X )  _  ( B ) |     Front Left   \    Front Right
 * <p>
 *        ___  '.      ( A )     /|       Wheel       \      Wheel
 * <p>
 *      .'    '.    '-._____.-'  .'       (x/▢)        \     (Y/Δ)
 * <p>
 *     |       |                 |                      \
 * <p>
 *      '.___.' '.               |          Rear Left    \   Rear Right
 * <p>
 *               '.             /             Wheel       \    Wheel
 * <p>
 *                \.          .'              (A/X)        \   (B/O)
 * <p>
 *                  \________/
 * <p>
 * Uncomment the @Disabled tag below to use this opmode.
 */
@Disabled
@Config
@TeleOp(group = "drive")
public class MotorDirectionDebugger extends LinearOpMode {
    public static double MOTOR_POWER = 0.7;

	private MotorDirectionDebugger() {
	}

	static MotorDirectionDebugger createMotorDirectionDebugger() {
		return new MotorDirectionDebugger();
	}

	@Override
    public void runOpMode() throws InterruptedException {
        final Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        final SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Press play to begin the debugging opmode");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        telemetry.clearAll();
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);

        while (!isStopRequested()) {
            telemetry.addLine("Press each button to turn on its respective motor");
            telemetry.addLine();
            telemetry.addLine("<font face=\"monospace\">Xbox/PS4 Button - Motor</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;X / ▢&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Left</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;Y / Δ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Right</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;B / O&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Right</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;A / X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Left</font>");
            telemetry.addLine();

            if(gamepad1.x) {
                drive.setMotorPowers(MOTOR_POWER, 0, 0, 0);
                telemetry.addLine("Running Motor: Front Left");
            } else if(gamepad1.y) {
                drive.setMotorPowers(0, 0, 0, MOTOR_POWER);
                telemetry.addLine("Running Motor: Front Right");
            } else if(gamepad1.b) {
                drive.setMotorPowers(0, 0, MOTOR_POWER, 0);
                telemetry.addLine("Running Motor: Rear Right");
            } else if(gamepad1.a) {
                drive.setMotorPowers(0, MOTOR_POWER, 0, 0);
                telemetry.addLine("Running Motor: Rear Left");
            } else {
                drive.setMotorPowers(0, 0, 0, 0);
                telemetry.addLine("Running Motor: None");
            }

            telemetry.update();
        }
    }
}
