package org.firstinspires.ftc.teamcode.cores.eventloop;

import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.ActionCommand;
import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.Command;
import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.TrajectoryCommand;
import static org.firstinspires.ftc.teamcode.cores.eventloop.commands.AutonomousCommands.TrajectorySequenceCommand;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.time.Timer;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.cores.UtilsMng;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class LoopCommandAutonomous extends OverclockOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public    SampleMecanumDrive drive;
	public    Client             client;
	public    UtilsMng           utils;
	public    Timer              timer;
	protected boolean            is_terminate_method_called;
	protected Exception          inline_exception;
	private   Queue <Command>    commands;
	private   TerminateReason    reason;

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

		if (is_terminate_method_called){
			CoreDatabase.writeInVals(this, reason, timer.getDeltaTime());
			if (inline_exception != null) {
				if (inline_exception instanceof OpModeManagerImpl.ForceStopException) {
					closeTask();
				} else {
					FtcLogTunnel.MAIN.report(inline_exception);
					FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
					throw new RuntimeException(inline_exception);
				}
			}else{
				FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
			}
		}
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason, Exception e) {
		is_terminate_method_called = true;
		inline_exception = e;
		this.reason = reason;
	}

	@Override
	public void closeTask() {
		is_terminate_method_called = true;
	}
}
