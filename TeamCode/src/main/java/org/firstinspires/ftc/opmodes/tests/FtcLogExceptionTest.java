package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.cores.eventloop.IntegralTeleOp;

@TeleOp(group = "zzz")
public class FtcLogExceptionTest extends IntegralTeleOp {
	@Override
	public void op_loop_entry() {
		throw new RuntimeException("Test exception");
	}
}
