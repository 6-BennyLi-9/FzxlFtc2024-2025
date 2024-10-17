package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Global;
import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.ric.utils.Timer;

@TeleOp(name = "MotorReverseTest",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class MotorReverseTest extends TuningProgramTemplate {
	IntegrationMotor motor;
	boolean          lst =false, now;

	@Override
	public void whenInit() {
		motor = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.LeftRear);
		client.addLine("按A鍵以REVERSE電機");
		registerGamePad();
		robot.gamepad.keyMap=new KeyMap();
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed);

		Global.integrationHardwareMap.printSettings();
	}

	@Override
	public void whileActivating() {
		now =robot.gamepad.getButtonRunAble(KeyTag.TuningButton1);
		if(!lst && now){
			motor.reverse();
			client.addData("["+Timer.getCurrentTime()+"]","Motors Reversed");
		}
		lst = now;

		motor.setPower(0.5);
		motor.update();

		client.changeData("Motor Direction",motor.isReversed());
	}
}