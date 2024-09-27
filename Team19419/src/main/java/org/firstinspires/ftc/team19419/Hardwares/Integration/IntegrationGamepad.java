package org.firstinspires.ftc.team19419.Hardwares.Integration;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team19419.Hardwares.Integration.Gamepad.BasicIntegrationGamepad;
import org.firstinspires.ftc.team19419.KeyMap;
import org.firstinspires.ftc.team19419.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.team19419.Hardwares.Integration.Gamepad.KeyTag;

public class IntegrationGamepad {
	public BasicIntegrationGamepad gamepad1,gamepad2;
	public KeyMap map;

	@UserRequirementFunctions
	public IntegrationGamepad(Gamepad gamepad1,Gamepad gamepad2){
		this(new BasicIntegrationGamepad(gamepad1),new BasicIntegrationGamepad(gamepad2));
	}
	public IntegrationGamepad(BasicIntegrationGamepad gamepad1,BasicIntegrationGamepad gamepad2){
		this.gamepad1=gamepad1;
		this.gamepad2=gamepad2;
		map=new KeyMap();
		map.initKeys();
	}

	@UserRequirementFunctions
	public void swap(){
		BasicIntegrationGamepad tmp=gamepad1;
		gamepad1=gamepad2;
		gamepad2=tmp;
	}

	@UserRequirementFunctions
	public boolean getButtonRunAble(KeyTag tag){
		if(map.IsControlledByGamepad1(tag)){
			return map.getButtonStateFromTagAndGamePad(tag,gamepad1);
		}else {
			return map.getButtonStateFromTagAndGamePad(tag,gamepad2);
		}
	}
	@UserRequirementFunctions
	public double getRodState(KeyTag tag){
		if(map.IsControlledByGamepad1(tag)){
			return map.getRodStateFromTagAndGamePad(tag,gamepad1);
		}else {
			return map.getRodStateFromTagAndGamePad(tag,gamepad2);
		}
	}
}
