package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.Annotations;
import org.firstinspires.ftc.teamcode.cores.eventloop.IntegralTeleOp;

@TeleOp(group = "zzz")
@Disabled
@Annotations.TestDoneSuccessfully
public class FtcLogExceptionTest extends IntegralTeleOp {
	@Override
	public void op_loop_entry() {
		throw new RuntimeException("Test exception");
	}
}
