package org.firstinspires.ftc.team19419.Tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team19419.Params;
import org.firstinspires.ftc.team19419.Templates.AutonomousProgramTemplate;

@TeleOp(name = "LateralInchPerTickTest",group = "tune")
@Disabled
public class LateralInchPerTickTest extends AutonomousProgramTemplate {
	@Override
	public void runOpMode() {
		Init(new Pose2d(0,0,0));

		if(WaitForStartRequest())return;

		while (!isStopRequested()){
			robot.update();
			robot.changeData("Ticks",robot.sensors.getDeltaL());
			robot.changeData("Inch Drove",robot.sensors.getDeltaL()* Params.LateralInchPerTick);
		}
	}
}
