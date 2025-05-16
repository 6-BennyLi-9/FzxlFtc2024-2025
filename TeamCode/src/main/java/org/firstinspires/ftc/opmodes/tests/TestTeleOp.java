package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.render.ClientRender;
import org.firstinspires.ftc.opmodes.teleops.TeleOpCore;

@TeleOp(name = "19419(Test)", group = "9_Test")
public final class TestTeleOp extends TeleOpCore {
	@Override
	public void op_start() {
		super.op_start();
		robot.hardwareAction = Actions.metaFor(robot.hardwareAction, new ClientRender(client));
	}
}
