package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.utils.UtilPoses;

@Autonomous(preselectTeleOp = "19419")
public class BlueLeft extends IntegralLinearOpMode {

	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.BlueLeftStart);

		registerTrajectory("decant preload",generateBuilder(UtilPoses.BlueLeftStart)
				.lineToLinearHeading(UtilPoses.BlueDecant)
				.build());
		registerTrajectory("intake1",generateBuilder(UtilPoses.BlueDecant)
				.lineToLinearHeading(UtilPoses.BlueLeftSample1)
				.build());

		registerTrajectory("decant1",generateBuilder(UtilPoses.BlueLeftSample1)
				.lineToLinearHeading(UtilPoses.BlueDecant)
				.build());

		registerTrajectory("intake2",generateBuilder(UtilPoses.BlueLeftSample1)
				.lineToLinearHeading(UtilPoses.BlueLeftSample2)
				.build());

		registerTrajectory("intake3",generateBuilder(UtilPoses.BlueLeftSample2)
				.lineToLinearHeading(UtilPoses.BlueLeftSample3)
				.build());
	}

	@Override
	public void linear() {
		runTrajectory("decant preload");
		runTrajectory("intake1");
		utils.displayArms().intake().runCached();
		sleep(3000);
		utils.stopIO().armsIDLE().runCached();
		sleep(1500);
//		runTrajectory("decant1");
//		runTrajectory("intake2");
//		runTrajectory("intake3");
	}
}
