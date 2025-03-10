package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetFirstSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetSecondSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightSuspend;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.cores.eventloop.IntegralAutonomous;
import org.firstinspires.ftc.teamcode.cores.structure.SimpleDriveOp;

@Config
@Autonomous(name = "Right", preselectTeleOp = "19419", group = "0_Main")
public class RightStale extends IntegralAutonomous {
	public static final double scaleGetPosition = 0.238;

	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.putData("初始化点位", "_ _ |<");

		registerTrajectory("suspend preload", generateBuilder(RightStart).lineToLinearHeading(RightSuspend).build());

		registerTrajectory("get sample 1", generateBuilder(RightSuspend).lineToLinearHeading(RightGetFirstSample).build());
		registerTrajectory("get sample 2", generateBuilder(RightGetFirstSample).lineToLinearHeading(RightGetSecondSample).build());

		registerTrajectory("get sample suspend 1", generateSequenceBuilder(RightGetSecondSample).lineToSplineHeading(GetSample.plus(new Pose2d(0, - 5))).build());
		registerTrajectory("get sample suspend 2", generateSequenceBuilder(RightSuspend).lineToSplineHeading(GetSample.plus(new Pose2d(0, - 5))).build());

		registerTrajectory("suspend got sample 1", generateSequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(5, 5))).build());
		registerTrajectory("suspend got sample 2", generateSequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(10, 5))).build());

		registerTrajectory("park", generateBuilder(RightSuspend.plus(new Pose2d(10, 5)).plus(new Pose2d(0, - 5.1))).lineToLinearHeading(GetSample).build());
	}

	@Override
	public void linear() {
		utils.integralLiftUpPrepare();
		utils.liftSuspendHighPrepare();
		utils.runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh();
		utils.waitMs(300);
		utils.openClip();
		utils.waitMs(100);
		utils.liftDown();
		utils.integralIntakes();
		utils.scaleOperate(scaleGetPosition);
		utils.runAsThread();
		sleep(600);

		runTrajectory("get sample 1");
		utils.displayArms();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(300);
		utils.integralIntakesEnding();
		utils.runCached();

		sleep(900);
		utils.openClaw();
		utils.waitMs(100);
		utils.closeClaw();
		utils.waitMs(100);
		utils.openClaw();
		utils.waitMs(200);
		utils.scaleOperate(scaleGetPosition);
		utils.armsToSafePosition();
		utils.waitMs(200);
		utils.decant();
		utils.runAsThread();
		runTrajectory("get sample 2");
		sleep(200);
		utils.displayArms();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(300);
		utils.integralIntakesEnding();
		utils.runCached();

		sleep(900);
		utils.openClaw();
		utils.waitMs(100);
		utils.closeClaw();
		utils.waitMs(100);
		utils.openClaw();
		utils.waitMs(200);
		utils.armsToSafePosition();
		utils.decant();
		utils.runAsThread();
		sleep(900);

		runTrajectory("get sample suspend 1");
		sleep(500);
		utils.boxRst();
		utils.addAction(SimpleDriveOp.build(0, - 0.2, 0));
		utils.waitMs(800);
		utils.closeClip();
		utils.waitMs(1000);
		utils.integralLiftUpPrepare();
		utils.liftSuspendHighPrepare();
		utils.runAsThread();
		sleep(1500);
		utils.rstMotors();

		runTrajectory("suspend got sample 1");
		angleCalibration(0, RightSuspend.plus(new Pose2d(5, 5)));
		drive.followTrajectory(generateBuilder(RightSuspend.plus(new Pose2d(5, 5))).back(3.5).build());
		utils.liftSuspendHigh();
		utils.waitMs(300);
		utils.openClip();
		utils.waitMs(100);
		utils.liftDown();
		utils.integralIntakes();
		utils.runAsThread();
		sleep(500);
		runTrajectory("get sample suspend 2");
		sleep(500);
		utils.addAction(SimpleDriveOp.build(0, - 0.2, 0));
		utils.waitMs(800);
		utils.closeClip();
		utils.waitMs(1000);
		utils.integralLiftUpPrepare();
		utils.liftSuspendHighPrepare();
		utils.runAsThread();
		sleep(1500);
		utils.rstMotors();

		runTrajectory("suspend got sample 2");
		angleCalibration(0, RightSuspend.plus(new Pose2d(10, 5)));
		drive.followTrajectory(generateBuilder(RightSuspend.plus(new Pose2d(10, 5))).back(3.2).build());
		utils.liftSuspendHigh();
		utils.waitMs(300);
		utils.openClip();
		utils.waitMs(100);
		utils.liftDown();
		utils.integralIntakes();
		utils.runAsThread();
		sleep(500);

		runTrajectory("park");

		flagging_op_complete();
	}
}
