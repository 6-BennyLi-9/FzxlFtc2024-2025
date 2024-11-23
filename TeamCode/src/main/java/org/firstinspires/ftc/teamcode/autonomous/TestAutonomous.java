package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;
import org.firstinspires.ftc.teamcode.autonomous.utils.Util;
import org.firstinspires.ftc.teamcode.autonomous.utils.structure.DcAutoLiftCtrl;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

@Autonomous(group = "9_tests")
public class TestAutonomous extends IntegralLinearOp {
	@Override
	public void initialize() {
		utils=new Util(){
			@Override
			protected LiftCtrl liftControllerGenerator(long target) {
				return new DcAutoLiftCtrl(HardwareConstants.lift,target);
			}
		};
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		utils.liftDecantLow().runCached();
		sleep(1000);
		utils.liftDecantHigh().runCached();
		sleep(1000);
		utils.liftSuspendHighPrepare().runCached();
		sleep(1000);
		utils.liftSuspendHigh().runCached();
		sleep(1000);
		utils.liftDown().runCached();
		sleep(1000);
	}
}
