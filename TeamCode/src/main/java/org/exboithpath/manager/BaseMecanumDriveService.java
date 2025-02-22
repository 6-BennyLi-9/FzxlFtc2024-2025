package org.exboithpath.manager;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.exboithpath.graphics.Pose;
import org.exboithpath.loclaizer.Localizer;
import org.exboithpath.loclaizer.MirrorRoadrunnerLocalizer;
import org.exboithpath.path.LinedPath;
import org.exboithpath.path.Path;
import org.exboithpath.path.PathMeta;
import org.exboithpath.path.PosePath;
import org.exboithpath.path.TurnPath;
import org.exboithpath.runner.ExpansionMecanumRunner;
import org.exboithpath.runner.MecanumRunner;
import org.exboithpath.runner.RunnerStatus;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class BaseMecanumDriveService implements DriveService {
	private final MecanumRunner runner;
	private final Localizer localizer;
	private final Queue <PathMeta> paths = new ArrayDeque <>();
	public double DEG_PER_MILLS = 5;
	public double INCH_PER_MILLS = 0.5;
	public RunnerStatus targetIDLEStatus;

	public BaseMecanumDriveService(HardwareMap hardwareMap) {
		this(new MirrorRoadrunnerLocalizer(hardwareMap), hardwareMap);
	}

	public BaseMecanumDriveService(Localizer localizer, @NonNull HardwareMap hardwareMap) {
		this(
				localizer,
				hardwareMap.get(DcMotorEx.class,"leftFront"),
				hardwareMap.get(DcMotorEx.class,"leftRear"),
				hardwareMap.get(DcMotorEx.class,"rightFront"),
				hardwareMap.get(DcMotorEx.class,"rightRear")
		);
	}

	public BaseMecanumDriveService(Localizer localizer, @NonNull DcMotorEx lf, @NonNull DcMotorEx lr, @NonNull DcMotorEx rf, @NonNull DcMotorEx rr) {
		this.localizer = localizer;
		this.localizer.setPoseEstimate(new Pose(0,0,0));

		lf.setDirection(DcMotorSimple.Direction.FORWARD);  //F
		lr.setDirection(DcMotorSimple.Direction.FORWARD);  //F
		rf.setDirection(DcMotorSimple.Direction.REVERSE);  //R
		rr.setDirection(DcMotorSimple.Direction.REVERSE);  //R

		this.runner = new ExpansionMecanumRunner(lf, lr, rf, rr);
		this.runner.setPoseTarget(new Pose(0,0,0));
	}

	@Override
	public void runTillRunnerStatus(RunnerStatus status){
		while (runner.getStatus() != status){
			update();
		}
	}

	@Override
	public void update() {
		boolean remove=false;

		if (!paths.isEmpty()){
			PathMeta meta = paths.element();
			if(meta.isTargetFinished() && runner.getStatus() == RunnerStatus.CALIBRATING){
				remove=true;
			}else if(meta.isInitialized()){
				runner.setPoseTarget(runner.getPoseTarget().plus(meta.update()));
			}else{
				if(meta.isWithinStarted()){
					runner.setPoseTarget(meta.getStartingPose());
				}
				meta.markInitialDone();
			}
		}

		if(remove){
			paths.remove();
		}

		runner.update(localizer);
	}

	public void setupPathMeta(Path path) {
		setupPathMeta(path, targetIDLEStatus);
	}

	@Override
	public void setupPathMeta(Path path, RunnerStatus targetWait){
		paths.add(new PathMeta(path));
		if (! Objects.isNull(targetWait)){
			runTillRunnerStatus(targetWait);
		}
	}

	@Override
	public void runToPose(Pose target){
		setupPathMeta(new PosePath(target));
	}

	@Override
	public void turn(double headingRad){
		setupPathMeta(new TurnPath(headingRad,DEG_PER_MILLS));
	}

	@Override
	public void lineTo(Pose to) {
		lineTo(runner.getPoseTarget(), to);
	}

	@Override
	public void lineTo(Pose from, Pose to){
		setupPathMeta(new LinedPath(from,to,INCH_PER_MILLS,DEG_PER_MILLS));
	}

	@Override
	public void setPoseEst(Pose localizePose){
		localizer.setPoseEstimate(localizePose);
		update();
	}
}
