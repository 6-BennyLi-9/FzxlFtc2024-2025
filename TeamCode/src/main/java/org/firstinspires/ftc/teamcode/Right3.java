package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.pushIn;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Autonomous(group = "untested", preselectTeleOp = "TELE_RIGHT")
public class Right3 extends LinearOpMode {
	public Right3(){
		super();
		telemetry.speak("hello");
		telemetry.update();
	}

	public static final int PUSH_LENGTH        = 32;
	public static       int LOOP_SUSPEND_TIMES = 4;
	public static       int DELTA_PLACE_INCH   = -3;

	public Utils              utils = new Utils();
	public SampleMecanumDrive drive;
	public ExecutorService    service = new ThreadPoolExecutor(5,8,1L, TimeUnit.SECONDS,new ArrayBlockingQueue <>(16), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

	public void workingGetSuspend(Trajectory toGetSample) {
		drive.followTrajectory(toGetSample);
		sleep(200);

		service.execute(()->{
			utils.clipOperation(false);    //夹住第一个
			sleep(300);
			utils.setRearLiftPosition(RearLiftLocation.middle);
			utils.armOperationR(false);
		});
	}

	public void workingGetSuspend(TrajectorySequence toGetSample){
		drive.followTrajectorySequence(toGetSample);
		sleep(200);

		service.execute(()->{
			utils.clipOperation(false);    //夹住第一个
			sleep(300);
			utils.setRearLiftPosition(RearLiftLocation.middle);
			utils.armOperationR(false);
		});
	}

	@Override
	public void runOpMode() throws InterruptedException {
		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("leftLift", "rightLift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.imuInit();
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
		utils.motorInit();

		drive = new SampleMecanumDrive(hardwareMap);

		Pose2d blueRight             = new Pose2d(- 35, 58, toRadians(- 90));
		Pose2d prePushFirstSample    = new Pose2d(- 45, 9, toRadians(- 90));
		Pose2d afterPushFirstSample  = new Pose2d(- 50, 9 + PUSH_LENGTH, toRadians(- 90));
		Pose2d prePushSecondSample   = new Pose2d(- 50, 9, toRadians(- 90));
		Pose2d toGetSuspendSample    = new Pose2d(- 43, 54, toRadians(- 90));
		Pose2d toSuspendGottenSample = new Pose2d(- 6, 33, toRadians(- 90));

		List<Pose2d> toSuspendPoints = new ArrayList <>();

		for (int i = 0 ; i < LOOP_SUSPEND_TIMES ; i++) {
			toSuspendPoints.add(toSuspendGottenSample.plus(new Pose2d(DELTA_PLACE_INCH * i)));
		}

		drive.setPoseEstimate(blueRight);
		TrajectorySequence pushSamples = drive.trajectorySequenceBuilder(blueRight)
				.strafeRight(5)
				.addDisplacementMarker(() -> service.execute(()->{
					utils.armOperationR(true);//翻转手臂
					utils.clipOperation(true);
				}))
				.lineToSplineHeading(prePushFirstSample)
				.strafeRight(5)
				.lineToSplineHeading(afterPushFirstSample)
				.lineToSplineHeading(prePushSecondSample)
				.strafeRight(9)
				.lineToSplineHeading(toGetSuspendSample.plus(new Pose2d(- 2)))
				.build();
		List<TrajectorySequence> toSuspendTracks = new ArrayList <>();
		List<Trajectory>		 toReloadTracks  = new ArrayList <>();

		for (int i = 0 ; i < LOOP_SUSPEND_TIMES ; i++) {
			toSuspendTracks.add(drive.trajectorySequenceBuilder(toGetSuspendSample)
					.lineTo(toSuspendPoints.get(i).vec())
					.forward(10.2)
					.addDisplacementMarker(()-> service.execute(()->{
						utils.clipOperation(true);
						sleep(150);
						utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
						sleep(150);
						utils.armOperationR(true);       //翻转手臂
					}))
					.build());
			toReloadTracks.add(drive.trajectoryBuilder(toSuspendTracks.get(i).end())
					.lineToLinearHeading(toGetSuspendSample)
					.build());
		}

		telemetry.addLine("ROBOT READY");
		telemetry.update();
		waitForStart();
		if (isStopRequested())return;

		workingGetSuspend(pushSamples);

		for (int i = 0 ; i < LOOP_SUSPEND_TIMES ; i++) {
			drive.followTrajectorySequence(toSuspendTracks.get(i));
			workingGetSuspend(toReloadTracks.get(i));
		}

		telemetry.addData("time used",getRuntime());
		telemetry.addData("time left",30-getRuntime());
		telemetry.update();
		sleep(Long.MAX_VALUE);
	}
}
