package org.betastudio.ftc.client;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.Global;

@Deprecated
public class BranchThreadClient extends TelemetryClient {
	private static boolean auto_start_updater=true;
	final Thread updateThread;

	public BranchThreadClient(final Telemetry telemetry) {
		this(telemetry, 1);
	}

	public BranchThreadClient(final Telemetry telemetry, final double targetTPS) {
		super(telemetry);
		updateThread = new Thread(() -> Actions.runAction(
				new InfinityLoopAction(() -> Actions.runAction(
						new ThreadedAction(new SleepingAction((long) (1000 / targetTPS)),
						new StatementAction(super::update)
				)))
		));

		setAutoUpdate(false);

		if(auto_start_updater){
			Global.threadManager.add("client-updater", updateThread);
		}
	}
	public static boolean auto_start_updater(){
		return auto_start_updater;
	}
	public static void auto_start_updater(final boolean config){
		auto_start_updater=config;
	}

	public void start_updater(){
		if(!updateThread.isAlive()){
			updateThread.start();
		}
	}
}
