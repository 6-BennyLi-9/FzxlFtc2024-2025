package org.firstinspires.ftc.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadRequests;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.firstinspires.ftc.teamcode.cores.eventloop.IntegralTeleOp;

@TeleOp(name = "19419", group = "0_Main")
public class TeleOpCore extends IntegralTeleOp {
	@Override
	public void op_init() {
		super.op_init();
		GamepadRequests.reboot();
		client.putData("DriveCtrlMode", "wait for start");
	}

	@Override
	public void op_loop_entry() {
		GamepadRequests.syncRequests();
		//主程序开始

		robot.operateThroughGamepad();
		robot.driveThroughGamepad();

		//主程序结束

		robot.printActions();

		robot.update();
		client.changeData("DriveCtrlMode", ChassisCtrl.mode.name());

		GamepadRequests.printValues(client);
	}
}
