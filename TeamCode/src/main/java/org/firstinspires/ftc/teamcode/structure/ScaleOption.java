package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public enum ScaleOption {
	;
	public enum ScalePosition{
		back,
		probe,
		unknown
	}
	private static ScalePosition recent=ScalePosition.unknown;
	private static ServoCtrl leftScaleController,rightScaleController;

	public static void connect() {
		leftScaleController =new ServoCtrl(HardwareConstants.leftScale,1);
		rightScaleController=new ServoCtrl(HardwareConstants.rightScale,0.5);

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
