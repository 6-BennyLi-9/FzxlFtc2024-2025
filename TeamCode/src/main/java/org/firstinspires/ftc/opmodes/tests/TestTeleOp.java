package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.cores.structure.DriveMode;
import org.firstinspires.ftc.cores.structure.DriveOp;
import org.firstinspires.ftc.opmodes.teleops.TeleOpCore;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Local;

@TeleOp(name = "19419(Test)", group = "9_Beta")
public class TestTeleOp extends TeleOpCore {
	@Override
	public void op_init() {
		super.op_init();
		DriveOp.config = DriveMode.SIMPLE_CALIBRATE;
	}

	@Override
	public void op_start() {
		super.op_start();
		Global.threadService.execute(() -> {
			Local.sleep(5000);
			throw new RuntimeException("test");
		});
	}
}
