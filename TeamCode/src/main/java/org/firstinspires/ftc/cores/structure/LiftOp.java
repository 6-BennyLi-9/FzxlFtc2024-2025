package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.TagOptionsRequired;
import org.firstinspires.ftc.cores.structure.positions.LiftMode;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.Labeler;
import org.firstinspires.ftc.teamcode.controllers.LiftControllers;
import org.firstinspires.ftc.teamcode.controllers.LiftCtrl;
import org.jetbrains.annotations.Contract;

/**
 * 升降机构操作类。
 * 该类用于操作升降机构，包括设置目标位置、获取当前位置、获取错误位置、获取标签、设置标签等。
 */
@Config
@SuppressWarnings("PublicField")
public class LiftOp implements HardwareController, TagOptionsRequired {
	/**当前的电梯状态*/
	public static  LiftMode recent = LiftMode.IDLE;
	/**结构控制器*/
	public static  LiftCtrl liftCtrl;
	/**电梯的具体点位*/
	public static long idlePosition, decantLow = 1080, decantHigh = 2000, highSuspend = 740, highSuspendPrepare = 1250, suspendLv1 = 770;
	private static LiftOp   instance;

	public static LiftOp getInstance() {
		return instance;
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return liftCtrl;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	@Override
	public void connect() {
		liftCtrl = new LiftControllers.DcLiftCtrl(HardwareDatabase.lift);

		liftCtrl.setTag(Labeler.generate().summonID(liftCtrl));
	}

	public void sync(@NonNull final LiftMode option) {
		recent = option;
		switch (option) {
			case IDLE:
				liftCtrl.setTargetPosition(idlePosition);
				break;
			case DECANT_LOW:
				liftCtrl.setTargetPosition(decantLow);
				break;
			case DECANT_HIGH:
				liftCtrl.setTargetPosition(decantHigh);
				break;
			case HIGH_SUSPEND:
				liftCtrl.setTargetPosition(highSuspend);
				break;
			case HIGH_SUSPEND_PREPARE:
				liftCtrl.setTargetPosition(highSuspendPrepare); // 设置目标位置为高悬准备位置
				break;
			case SUSPEND_LV1:
				liftCtrl.setTargetPosition(suspendLv1); // 设置目标位置为悬停等级 1 位置
				break;
			default:
				throw new IllegalStateException("Unexpected enum state:" + option.name()); // 抛出异常，表示意外的枚举状态
		}
	}

	/**
	 * @return 返回 {@code recent} 是否是 {@code decant} 状态
	 */
	public boolean decanting() {
		return LiftMode.DECANT_HIGH == recent || LiftMode.DECANT_LOW == recent; // 检查 recent 是否为 DECANT_HIGH 或 DECANT_LOW
	}

	/**
	 * 初始化控制器并返回控制动作。
	 * 首先调用 connect 方法连接硬件组件，然后返回升降机构控制器实例。
	 *
	 * @return 控制动作实例
	 */
	@NonNull
	public Action initController() {
		connect(); // 连接硬件组件并初始化控制器
		return getController(); // 返回控制器实例
	}

	@Override
	public String getTag() {
		return liftCtrl.getTag(); // 返回控制器的标签
	}

	@Override
	public void setTag(final String tag) {
		liftCtrl.setTag(tag); // 设置控制器的标签
	}
}
