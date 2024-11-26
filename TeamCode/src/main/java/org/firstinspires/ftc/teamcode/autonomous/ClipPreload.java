package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(group = "1_Utils")
public final class ClipPreload extends IntegralAutonomous {
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
