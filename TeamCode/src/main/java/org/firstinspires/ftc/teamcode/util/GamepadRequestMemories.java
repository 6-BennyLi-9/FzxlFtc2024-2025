package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.Robot.*;
import static org.firstinspires.ftc.teamcode.util.UtilButtonControlSystem.ButtonConfig.SingleWhenPressed;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;

/**
 * gamepad 控制请求的数据库
 * @see UtilButtonControlSystem
 */
public enum GamepadRequestMemories {
	;
	/**输入样本*/
	public static UtilButtonControlSystem sampleIO;
	/**电梯上筐*/
	public static UtilButtonControlSystem liftDecantUpping;
	/**电梯闲置（下）*/
	public static UtilButtonControlSystem liftIDLE;
	/**电梯挂样本准备*/
	public static UtilButtonControlSystem liftHighSuspendPrepare;
	/**倒筐与挂样本*/
	public static UtilButtonControlSystem decantOrSuspend;
	/**打开/关闭样本夹*/
	public static UtilButtonControlSystem clipOption;
	/**吸取滑轨与收取杆操作*/
	public static UtilButtonControlSystem armScaleOperate;

	static {
		sampleIO 				=new UtilButtonControlSystem(SingleWhenPressed);
		liftDecantUpping		=new UtilButtonControlSystem(SingleWhenPressed);
		liftHighSuspendPrepare	=new UtilButtonControlSystem(SingleWhenPressed);
		liftIDLE      			=new UtilButtonControlSystem(SingleWhenPressed);
		decantOrSuspend			=new UtilButtonControlSystem(SingleWhenPressed);
		clipOption				=new UtilButtonControlSystem(SingleWhenPressed);
		armScaleOperate 		=new UtilButtonControlSystem(SingleWhenPressed);

	}

	public static void syncRequests(){
		sampleIO		.sync( gamepad2.a);
		liftDecantUpping.sync( gamepad2.left_bumper);
		liftHighSuspendPrepare .sync( gamepad2.dpad_up);
		liftIDLE      	.sync( gamepad2.dpad_down);
		decantOrSuspend.sync( gamepad2.x);
		clipOption		.sync( gamepad2.b);
		armScaleOperate.sync( gamepad2.right_bumper);
	}

	public static void printValues(){
		final TelemetryClient instance=TelemetryClient.getInstance();
		instance.changeData("liftDecantUpping",liftDecantUpping);
		instance.changeData("sampleIO",sampleIO);
		instance.changeData("liftHighSuspendPrepare", liftHighSuspendPrepare);
		instance.changeData("liftIDLE",liftIDLE);
		instance.changeData("decantOrSuspend", decantOrSuspend);
		instance.changeData("clipOption",clipOption);
		instance.changeData("armScaleOperate", armScaleOperate);
	}
}
