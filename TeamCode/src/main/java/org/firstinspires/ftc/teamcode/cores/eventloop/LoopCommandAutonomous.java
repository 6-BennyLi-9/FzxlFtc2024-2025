package org.firstinspires.ftc.teamcode.cores.eventloop;

import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.ActionCommand;
import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.Command;
import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.TrajectoryCommand;
import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.TrajectorySequenceCommand;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.time.Timer;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.firstinspires.ftc.teamcode.cores.UtilsMng;

import java.util.LinkedList;
import java.util.Queue;

public abstract class LoopCommandAutonomous extends OverclockOpMode{
	public    SampleMecanumDrive drive;
	public    Client             client;
	public    UtilsMng           utils;
	public    Timer              timer;
	protected boolean            is_terminate_method_called;
	protected Exception          inlineUncaughtException;// FIXME: 2025/2/27 undone
	private   Queue <Command>    commands;

	@Override
	public void op_init() {
		commands = new LinkedList <>();
		drive = new SampleMecanumDrive(hardwareMap);
		client = new BaseMapClient(telemetry);
		utils = new UtilsMng();
		timer = new Timer();
	}

	@Override
	public void op_loop() {
		drive.update();
		if (! drive.isBusy() && ! commands.isEmpty()) {
			Command element = commands.remove();
			if (element instanceof TrajectoryCommand) {
				((TrajectoryCommand) element).execute(drive);
			} else if (element instanceof TrajectorySequenceCommand) {
				((TrajectorySequenceCommand) element).execute(drive);
			} else if (element instanceof ActionCommand) {
				((ActionCommand) element).execute();
			}
		}
	}
}
