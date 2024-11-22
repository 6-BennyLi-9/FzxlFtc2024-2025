package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.structure.LiftOption;
import org.firstinspires.ftc.teamcode.structure.controllers.lift.LinearLiftController;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

@TeleOp(name = "19419(Beta)",group = "9_Beta")
public class TeleOpTesting extends TeleOpCore{
	@Override
	public void start() {
		super.start();
		LiftOption.liftController=new LinearLiftController(HardwareConstants.lift);
	}
}
