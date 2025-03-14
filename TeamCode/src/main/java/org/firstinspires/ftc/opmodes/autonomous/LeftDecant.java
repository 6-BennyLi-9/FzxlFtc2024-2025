package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftStart;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.cores.eventloop.StructuralLinearMode;

//@Deprecated
@Autonomous(name = "Left(只倒)", preselectTeleOp = "19419", group = "0_Main")
public class LeftDecant extends StructuralLinearMode {
	@Override
	public void linear() {
		drive.setPoseEstimate(LeftStart);
		client.putData("初始化点位", ">| _ _");

		final Trajectory decant_preload = drive.trajectoryBuilder(LeftStart).lineToLinearHeading(Decant).build();

		waitForStart();

		drive.followTrajectory(decant_preload);

		flagging_op_complete();
	}
}
