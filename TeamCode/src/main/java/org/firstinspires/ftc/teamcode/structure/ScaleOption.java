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
		unknown;
	}
	private static ScalePosition recent=ScalePosition.unknown;
	public static ScalePosition recent() {
		return recent;
	}

	private static final class ScaleController implements Action{

		@Override
		public boolean run() {
			switch (recent){
				case back:
					HardwareConstants.leftScale.setPosition(1);
					HardwareConstants.rightScale.setPosition(0.5);
					break;
				case probe:
					HardwareConstants.leftScale.setPosition(0.5);
					HardwareConstants.rightScale.setPosition(1);
					break;
				case unknown:
					break;
			}
			return false;
		}
		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "now:"+recent.name();
		}

	}
	public static void init(){
		back();
	}

	public static void probe(){
		recent=ScalePosition.probe;
	}
	public static void back(){
		recent=ScalePosition.back;
	}
	public static void flip(){
		if(ScalePosition.probe == recent){
			back();
		}else{
			probe();
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController() {
		return new ScaleController();
	}
}
