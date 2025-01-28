package org.firstinspires.ftc.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "强制重启机器", group = "zzz")
public class RobotRestart extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new Thread(() -> {throw new NullPointerException();}).start();
	}
}
