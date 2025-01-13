package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.msg.ChassisCtrlValsMessage;

public class GamepadDriveProcessor {
	public static double vS,vF;

	public static ChassisCtrlValsMessage process(ChassisCtrlValsMessage message, @NonNull GamepadDriveMode mode){
		switch(mode){
			case FASTER_POWER:
				return new ChassisCtrlValsMessage(
						getFuncResult(vF,message.pX),
						getFuncResult(vF,message.pY),
						getFuncResult(vF,message.pAngle)
				);
			case SLOWER_POWER:
				return new ChassisCtrlValsMessage(
						getFuncResult(vS,message.pX),
						getFuncResult(vS,message.pY),
						getFuncResult(vS,message.pAngle)
				);			case STRAIGHT_LINEAR:
			default:
				return message;
		}
	}

	private static double getFuncResult(double kA, double argument){
		return kA * argument * argument + (1 - kA) * argument;
	}
}
