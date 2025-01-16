package org.firstinspires.ftc.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.cores.RobotMng;
import org.firstinspires.ftc.cores.eventloop.IntegralTeleOp;
import org.firstinspires.ftc.teamcode.GamepadRequests;

@TeleOp(name = "19419", group = "0_Main")
public class TeleOpCore extends IntegralTeleOp {
	@Override
	public void op_init() {
		super.op_init();
		client.addData("drive buf", "wait For Start.");
	}

	@Override
	public void op_loop_entry() {
		GamepadRequests.syncRequests();
		//主程序开始

		robot.operateThroughGamepad();
		robot.driveThroughGamepad();

		//主程序结束

		robot.printActions();
		//robot.printIMUVariables();
		//HardwareDatabase.printVoltages();
		//GamepadRequestMemories.printValues();

		robot.update();
		client.changeData("drive buf", RobotMng.driveBufPower);
	}
}
