package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.util.GamepadRequestMemories;
import org.firstinspires.ftc.teamcode.util.ops.IntegralTeleOp;

@TeleOp(name = "19419", group = "0_Main")
public class TeleOpCore extends IntegralTeleOp {
	@Override
	public void loop() {
		super.loop();
		//主程序开始

		robot.operateThroughGamepad();
		robot.driveThroughGamepad();

		//主程序结束
		robot.printThreadDebugs();
		GamepadRequestMemories.printValues();

		robot.runThread();

		TelemetryClient.getInstance().update();//更新缓存
		auto_terminate_when_TLE(false);
	}
}
