package org.firstinspires.ftc.teamcode.structure;

import static org.betastudio.ftc.Interfaces.HardwareController;
import static org.betastudio.ftc.Interfaces.TagOptionsRequired;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.message.DriveMsg;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.Hardwares;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.firstinspires.ftc.teamcode.controllers.SimpleDriveAction;
import org.jetbrains.annotations.Contract;

public strictfp class DriveOp implements HardwareController, TagOptionsRequired {
	public static  ChassisCtrl chassisCtrl;
	private static double      x;
	private static double      y;
	private static double      turn;
	private static DriveOp     instance;

	public static DriveOp getInstance() {
		return instance;
	}

	@NonNull
	@Contract("_, _, _ -> new")
	public static Action build(final double x, final double y, final double turn) {
		return new SimpleDriveAction(x, y, turn);
	}

	@NonNull
	@Contract("_, _, _, _ -> new")
	public static Action build(final double x, final double y, final double turn, final double bufPower) {
		return new SimpleDriveAction(x * bufPower, y * bufPower, turn * bufPower);
	}

	@Override
	public void connect() {
		chassisCtrl = new ChassisCtrl(Hardwares.leftFront, Hardwares.leftRear, Hardwares.rightFront, Hardwares.rightRear);

		chassisCtrl.setTag(Labeler.summon(chassisCtrl));
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return chassisCtrl;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	@NonNull
	public Action initController() {
		connect();
		return getController();
	}

	@Override
	public String getTag() {
		return chassisCtrl.getTag();
	}

	@Override
	public void setTag(final String tag) {
		chassisCtrl.setTag(tag);
	}

	public DriveMsg getDriveMsg() {
		return new DriveMsg(x, y, turn);
	}

	public void sync(final double x, final double y, final double turn) {
		DriveOp.x = x;
		DriveOp.y = y;
		DriveOp.turn = turn;

		chassisCtrl.setPowers(x, y, turn);
	}


	public void turn(final double turn) {
		turn(turn, 1);
	}

	public void turn(final double turn, final double buf) {
		sync(x, y, DriveOp.turn + turn * buf);
	}
}
