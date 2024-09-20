package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Templates.TeleopProgramTemplate;

/**
 * @see AxialInchPerTickTest
 * @see LateralInchPerTickTest
 * @see TurningDegPerTickTest
 * */
@TeleOp(name = "[3 in one!]DeadWheelTuner")
public class ThreeInOne_DeadWheelTuner extends TeleopProgramTemplate {
	@Override
	public void whenInit() {
		robot.addLine("该调参程序不具备定位和直接数据处理能力，以下所能看到的辅助调参数据都是直接相乘得到的，因此不具备定位能力！！！");
	}

	@Override
	public void whileActivating() {
		robot.changeData("TurningDegPerTick",robot.sensors.getDeltaT());
		robot.changeData("AxialInchPerTick",robot.sensors.getDeltaA());
		robot.changeData("LateralInchPerTick",robot.sensors.getDeltaL());

		robot.addLine("如果你已经填入了Params数据，则在这里将会直接得到相乘结果");

		robot.changeData("TurningDegPerTick",robot.sensors.getDeltaT() * Params.TurningDegPerTick);
		robot.changeData("AxialInchPerTick",robot.sensors.getDeltaA() * Params.AxialInchPerTick);
		robot.changeData("LateralInchPerTick",robot.sensors.getDeltaL() * Params.LateralInchPerTick);
	}
}
