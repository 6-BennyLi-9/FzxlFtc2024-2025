package org.firstinspires.ftc.team19419.DriveControls.Commands;

import org.firstinspires.ftc.team19419.DriveControls.OrderDefinition.DriveOrder;
import org.firstinspires.ftc.team19419.DriveControls.OrderDefinition.DriveOrderPackage;

import java.util.LinkedList;

/**
 * 为了方便存储，查询
 */
public class DriveCommandPackage implements DriveOrderPackage {
	public LinkedList<DriveCommand> commands;

	public DriveCommandPackage() {
		commands = new LinkedList<>();
	}

	@Override
	public LinkedList<DriveOrder> getOrder() {
		LinkedList<DriveOrder> res=new LinkedList<>();
		for (DriveOrder order : commands) {
			res.push(order);
		}
		return res;
	}

}
