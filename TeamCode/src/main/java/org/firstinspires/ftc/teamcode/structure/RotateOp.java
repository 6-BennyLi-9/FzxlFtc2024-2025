package org.firstinspires.ftc.teamcode.structure;

import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

public enum RotateOp {
	;
	private static ServoCtrl rotateControl;

	public static void connect() {
		rotateControl =new ServoCtrl(HardwareConstants.claw,0.79);

		rotateControl.setTag("rotate");
	}

	public static void mid(){
		rotateControl.setTargetPosition(0.79);
	}
	public static void turnLeft(){
		rotateControl.changeTargetPositionBy(0.1);
	}
	public static void turnRight(){
		rotateControl.changeTargetPositionBy(-0.1);
	}
}
