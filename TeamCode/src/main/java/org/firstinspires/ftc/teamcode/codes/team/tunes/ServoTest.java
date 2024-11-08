package org.firstinspires.ftc.teamcode.codes.team.tunes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "ServoTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class ServoTest extends OpMode {
	public Servo leftScale,rightScale;

	@Override
	public void init() {
		leftScale=hardwareMap.get(Servo.class,"leftScale");
		rightScale=hardwareMap.get(Servo.class,"rightScale");
	}

	public boolean isPushed=true;

	@Override
	public void loop() {
		if(gamepad1.x){
			isPushed=false;
		}else if(gamepad1.b){
			isPushed=true;
		}

		if(isPushed){
			leftScale.setPosition(1);
			rightScale.setPosition(0.5);
		}else{
			leftScale.setPosition(0.5);
			rightScale.setPosition(1);
		}
	}
}
