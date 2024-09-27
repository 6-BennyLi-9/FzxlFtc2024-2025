package org.firstinspires.ftc.team19419.Templates;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team19419.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.team19419.Robot;
import org.firstinspires.ftc.team19419.Utils.Enums.RunningMode;

public abstract class AutonomousProgramTemplate extends LinearOpMode {
	public Robot robot;
	public SimpleMecanumDrive drive;

	public void Init(Pose2d position){
		robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);
		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(position);
	}

	/**
	 * @return 如果程序被停止，则返回true
	 */
	public boolean WaitForStartRequest(){
		while(opModeIsNotActive()){
			sleep(500);
		}

		return opModeStopped();
	}
	public boolean opModeIsNotActive(){
		return !opModeIsActive()&&!isStopRequested();
	}
	public boolean opModeStopped(){
		return !opModeIsActive() || isStopRequested();
	}
}
