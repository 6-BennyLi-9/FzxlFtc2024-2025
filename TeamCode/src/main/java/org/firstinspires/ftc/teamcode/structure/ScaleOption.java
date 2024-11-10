package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum ScaleOption {
	;
	public enum ScalePosition{
		back,
		probe,
		unknown
	}
	private static ScalePosition recent=ScalePosition.unknown;

	public static ScalePosition recent() {
		return recent;
	}

	private static final class ScaleProbe implements Action{
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(0.5);
			HardwareConstants.rightScale.setPosition(1);
			return false;
		}
	}
	private static final class ScaleBack implements Action{
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(1);
			HardwareConstants.rightScale.setPosition(0.5);
			return false;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action init(){
		recent=ScalePosition.back;
		return new ScaleBack();
	}
	@NonNull
	@Contract(" -> new")
	public static Action probe(){
		recent=ScalePosition.probe;
		return new ScaleProbe();
	}
	@NonNull
	@Contract(" -> new")
	public static Action back(){
		recent=ScalePosition.back;
		return new ScaleBack();
	}

	public static Action flip(){
		if(ScalePosition.probe == recent){
			return back();
		}else{
			return probe();
		}
	}
}
