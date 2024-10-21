package org.firstinspires.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.DefaultKeyMapController;
import org.firstinspires.ftc.teamcode.DefaultParamsController;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.Templates;
import org.firstinspires.ftc.teamcode.utils.enums.RunningMode;

@Templates
public abstract class TeamAutonomousTemplate extends AutonomousProgramTemplate {
	@Override
	public void Init(Position2d position){
		Global.clear();

		robot=new Robot(hardwareMap, RunningMode.Autonomous,telemetry);

		robot.setParamsOverride(new DefaultParamsController());
		robot.setKeyMapController(new DefaultKeyMapController());

		drive= (SimpleMecanumDrive) robot.InitMecanumDrive(position);

		FtcDashboard.getInstance().sendTelemetryPacket(new TelemetryPacket(true));
	}
}
