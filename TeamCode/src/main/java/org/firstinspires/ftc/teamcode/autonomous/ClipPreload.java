package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralLinearOp;

@Autonomous(group = "1_utils")
public final class ClipPreload extends IntegralLinearOp {
	@Override
	public void initialize() {
		utils.openClip().runCached();
	}

	@Override
	public void linear() {
		utils.closeClip().runCached();
		sleep(3000);
	}
}
