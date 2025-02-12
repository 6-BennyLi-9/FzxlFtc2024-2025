package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * 机器强制重启，可以在 dashboard 中重启机器、重置数据
 */
@Disabled
@Autonomous(group = "zzz")
public final class RobotForceRestart extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		Thread t=new Thread(()->{
			throw new IllegalStateException();
		});
		waitForStart();
		t.start();
	}
}
