package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Params;

@TeleOp(name = "ChassisTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class ChassisTest extends SecPowerPerInchTuner {
	@Override
	public double getBufPower() {
		return 0.1f;
	}
}
