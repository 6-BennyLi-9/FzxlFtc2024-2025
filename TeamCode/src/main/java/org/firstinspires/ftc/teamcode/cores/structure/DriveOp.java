package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.message.DriveBufMsg;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.jetbrains.annotations.Contract;

@Config
public strictfp class DriveOp implements Interfaces.HardwareController, Interfaces.TagOptionsRequired {
	public static       ChassisCtrl chassisCtrl;
	private static      double      x;
	private static      double      y;
	private static      double      turn;
	private static      DriveOp     instance;

	public static DriveOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		chassisCtrl = new ChassisCtrl(HardwareDatabase.leftFront, HardwareDatabase.leftRear, HardwareDatabase.rightFront, HardwareDatabase.rightRear);

		chassisCtrl.setTag(Labeler.gen().summon(chassisCtrl));
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

	public DriveBufMsg getDriveMsg(){
		return new DriveBufMsg(x,y,turn);
	}

	public void sync(double x, double y, double turn) {
		DriveOp.x = x;
		DriveOp.y = y;
		DriveOp.turn = turn;

		chassisCtrl.setPowers(x, y, turn);
	}


	public void turn(double turn) {
		turn(turn, 1);
	}

	public void turn(double turn, double buf) {
		sync(x, y, DriveOp.turn + turn * buf);
	}
}
