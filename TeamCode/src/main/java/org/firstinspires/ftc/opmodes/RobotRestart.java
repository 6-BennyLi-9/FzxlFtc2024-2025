package org.firstinspires.ftc.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Robot Force Restart", group = "2_Guarder")
public final class RobotRestart extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new Thread(() -> {throw new NullPointerException();}).start();
	}
}
