package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;
import org.firstinspires.ftc.teamcode.util.ops.UtilMng;

@Autonomous(group = "9_Tests")
public class TestAutonomous extends IntegralAutonomous {
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
