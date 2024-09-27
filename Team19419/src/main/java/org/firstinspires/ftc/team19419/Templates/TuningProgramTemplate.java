package org.firstinspires.ftc.team19419.Templates;

import org.firstinspires.ftc.team19419.Robot;
import org.firstinspires.ftc.team19419.Utils.Enums.RunningMode;
import org.firstinspires.ftc.team19419.Utils.Timer;

public abstract class TuningProgramTemplate extends TeleopProgramTemplate{
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		timer=new Timer();
		whenInit();
	}
}
