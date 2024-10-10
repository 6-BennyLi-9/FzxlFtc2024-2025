package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import org.firstinspires.ftc.teamcode.ric.keymap.KeyMap;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMapButtonContent;
import org.firstinspires.ftc.teamcode.ric.keymap.KeyMapContent;

import java.util.Map;

public class KeyMapTest extends TuningProgramTemplate {
	@Override
	public void whileActivating() {
		for(Map.Entry<KeyTag, KeyMapContent> entry : robot.gamepad.keyMap.contents.entrySet()){
			if(entry.getValue() instanceof KeyMapButtonContent){
				robot.client.changeData(entry.getValue().tag.name(),robot.gamepad.getButtonRunAble(entry.getKey()));
			}
		}
	}

	@Override
	public void whenInit() {
		robot.gamepad.keyMap =new KeyMap();

		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.SinglePressToChangeRunAble);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ClassicRunForward, KeyRodType.LeftStickY,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ClassicRunStrafe, KeyRodType.LeftStickX,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ClassicTurn, KeyRodType.RightStickX,KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.ClassicSpeedConfig, KeyButtonType.X, KeyMapSettingType.SinglePressToChangeRunAble);
	}
}
