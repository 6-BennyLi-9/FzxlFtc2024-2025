package org.firstinspires.ftc.teamcode.util.ops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class OverSpeedOpMode extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		op_init();

		while (opModeInInit()){
			loop_init();
		}

		if(!opModeIsActive()){
			throw new InterruptedException();
		}

		op_start();

		while (opModeIsActive()){
			op_loop();
		}

		op_end();
	}

	public abstract void op_init();
	public void loop_init(){}
	public void op_start(){}
	public abstract void op_loop();
	public void op_end(){}
}
