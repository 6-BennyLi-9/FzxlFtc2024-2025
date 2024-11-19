package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.utils.UtilPoses;

@Autonomous
public class BlueLeft extends IntegralLinearOpMode {

	@Override
	public void initialize() {
		registerTrajectory("decant",generateBuilder(UtilPoses.BlueLeftStart.pose)
				.lineToSplineHeading(UtilPoses.BlueDecant.pose)
				.build());
		registerTrajectory("intake1",generateBuilder(UtilPoses.BlueDecant.pose)
				.lineToLinearHeading(UtilPoses.BlueLeftSample1.pose)
				.build());

		registerTrajectory("intake2",generateBuilder(UtilPoses.BlueLeftSample1.pose)
				.lineToLinearHeading(UtilPoses.BlueLeftSample2.pose)
				.build());

		registerTrajectory("intake3",generateBuilder(UtilPoses.BlueLeftSample2.pose)
				.lineToLinearHeading(UtilPoses.BlueLeftSample3.pose)
				.build());
	}

	@Override
	public void linear() {
		runTrajectory("decant");
		runTrajectory("intake1");
		runTrajectory("intake2");
		runTrajectory("intake3");
	}
}
