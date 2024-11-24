package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;
import org.firstinspires.ftc.teamcode.autonomous.utils.UtilMng;

@Autonomous(group = "9_tests")
public class TestAutonomous extends IntegralLinearOp {
	@Override
	public void initialize() {
		utils=new UtilMng();
		utils.armsToSafePosition().runCached();
	}

	@Override
	public void linear() {
		angleCalibration(0,new Pose2d());
		sleep(1000);
		angleCalibration(90,new Pose2d(0,0,90));//逆时针
	}
}
