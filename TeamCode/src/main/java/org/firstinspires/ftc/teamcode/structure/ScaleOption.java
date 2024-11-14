package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoController;
import org.jetbrains.annotations.Contract;

public enum ScaleOption {
	;
	public enum ScalePosition{
		back,
		probe,
		unknown
	}
	private static ScalePosition recent=ScalePosition.unknown;
	private final static ServoController leftScaleController,rightScaleController;

	static {
		leftScaleController =new ServoController(HardwareConstants.leftScale,1);
		rightScaleController=new ServoController(HardwareConstants.rightScale,0.5);

		leftScaleController.setTag("leftScale");
		rightScaleController.setTag("rightScale");
	}

	public static ScalePosition recent() {
		return recent;
	}

	public static void init(){
		back();
	}
	public static void flip(){
		if(ScalePosition.probe == recent){
			back();
		}else{
			probe();
		}
	}

	public static void probe(){
		recent=ScalePosition.probe;
		leftScaleController.setTargetPosition(0.5);
		rightScaleController.setTargetPosition(1);

	}
	public static void back(){
		recent=ScalePosition.back;
		leftScaleController.setTargetPosition(1);
		rightScaleController.setTargetPosition(0.5);
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController() {
		return new ThreadedAction(leftScaleController,rightScaleController);
	}
}
