package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

public enum ScaleOp {
	;
	public enum ScalePositionTypes {
		back,
		probe,
		unknown
	}
	private static ScalePositionTypes recent = ScalePositionTypes.unknown;
	private static ServoCtrl          leftScaleController,rightScaleController;

	public static void connect() {
		leftScaleController =new ServoCtrl(HardwareConstants.leftScale,1);
		rightScaleController=new ServoCtrl(HardwareConstants.rightScale,0.5);

		leftScaleController.setTag("leftScale");
		rightScaleController.setTag("rightScale");
	}

	public static ScalePositionTypes recent() {
		return recent;
	}

	public static void manage(double position){
		position= Math.min(Math.max(position,0.58),0.92);
		leftScaleController.setTargetPosition(1.5-position);
		rightScaleController.setTargetPosition(position);
	}

	public static void init(){
		back();
	}
	public static void flip(){
		if(ScalePositionTypes.probe == recent){
			back();
		}else{
			probe();
		}
	}

	public static void probe(){
		recent= ScalePositionTypes.probe;
		manage(1);
	}
	public static void back(){
		recent= ScalePositionTypes.back;
		manage(0.5);
	}
	public static void operate(final double position){
		recent= ScalePositionTypes.probe;
		manage(position);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return new ThreadedAction(leftScaleController,rightScaleController);
	}
}
