package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.ops.IntegralTeleOp;

@TeleOp(name = "19419", group = "0_Main")
public class TeleOpCore extends IntegralTeleOp {
	@Override
	public void op_init() {
		super.op_init();
		auto_terminate_when_TLE(false);
	}

	@Override
	public void op_loop() {
		super.op_loop();
		//主程序开始

		robot.operateThroughGamepad();
		robot.driveThroughGamepad();

		//主程序结束

		robot.printThreadDebugs();
//		HardwareConstants.printVoltages();
//		GamepadRequestMemories.printValues();

		robot.runThread();
	}
}
