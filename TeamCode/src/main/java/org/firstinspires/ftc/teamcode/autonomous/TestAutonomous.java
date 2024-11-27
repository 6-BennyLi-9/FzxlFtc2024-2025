package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;
import org.firstinspires.ftc.teamcode.util.ops.UtilMng;

@Autonomous(group = "9_Tests")
public final class TestAutonomous extends IntegralAutonomous {
	@Override
	public void initialize() {
		utils = new UtilMng();
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		utils.displayArms().openClaw().runCached();
		sleep(1000);
		utils.armsIDLE().closeClaw().runCached();
		flagging_op_complete();
	}
}
