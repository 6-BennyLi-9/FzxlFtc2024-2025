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

		robotMng.operateThroughGamepad();
		robotMng.driveThroughGamepad();

		//主程序结束
		robotMng.printThreadDebugs();
		GamepadRequestMemories.printValues();

		robotMng.runThread();

		TelemetryClient.getInstance().update();//更新缓存
	}
}
