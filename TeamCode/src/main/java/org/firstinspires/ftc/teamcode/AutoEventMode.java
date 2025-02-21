package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.HardwareParams.pushIn;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequenceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class AutoEventMode extends OpMode {
	public static final class PositionRunnable implements Runnable, Callable <Pose2d> {
		private final Runnable runnable;
		private final Pose2d   pose;

		public PositionRunnable(Runnable runnable, Pose2d pose) {
			this.runnable = runnable;
			this.pose = pose;
		}

		@Override
		public void run() {
			runnable.run();
		}

		@Override
		public Pose2d call() {
			return pose;
		}
	}

	public TrajectorySequence        MAIN_SEQUENCE;
	public TrajectorySequenceBuilder MAIN_BUILDER;
	public Utils                     utils;
	public SampleMecanumDrive        drive;
	public Pose2d                  allowErr = new Pose2d(2,2,Math.toRadians(10));
	public List <PositionRunnable> marks    = new ArrayList <>();
	public int                    iterator;
	public Runnable                   runnable;

	@Override
	public void init() {
		telemetry.addData("Status","正在初始化");
		telemetry.addLine("不要在这个阶段启动程序！");
		telemetry.update();

		drive=new SampleMecanumDrive(hardwareMap);
		utils=new Utils();

		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("leftLift", "rightLift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
		utils.motorInit();
		drive.setPoseEstimate(getInitialPoseEstimate());
		MAIN_BUILDER = drive.trajectorySequenceBuilder(getInitialPoseEstimate());

		initialize();

		MAIN_SEQUENCE=MAIN_BUILDER.build();

		telemetry.addData("Status","初始化已完成");
		telemetry.update();
	}

	@Override
	public void start() {
		drive.followTrajectorySequenceAsync(MAIN_SEQUENCE);

		runnable = ()->{
			if(iterator < marks.size()){
				PositionRunnable next = marks.get(iterator);
				Pose2d pose = next.call().minus(drive.getPoseEstimate());
				pose=new Pose2d(
						Math.abs(pose.getX()),
						Math.abs(pose.getY()),
						Math.abs(pose.getHeading())
				);
				if(pose.getX()<allowErr.getX() && pose.getY()<allowErr.getY() && pose.getHeading()<allowErr.getHeading()){
					next.run();
					++iterator;
				}
			}else{
				runnable=()-> {};
			}
		};
	}

	@Override
	public void loop() {
		drive.update();

	}

	/**
	 * 要求：修改 MAIN_BUILDER
	 */
	public abstract void initialize();
	@Const
	public abstract Pose2d getInitialPoseEstimate();
}
