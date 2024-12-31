package org.betastudio.ftc.client;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.Global;

@Deprecated
public class BranchThreadClient extends TelemetryClient {
	public BranchThreadClient(final Telemetry telemetry) {
		this(telemetry, 1);
	}

	public BranchThreadClient(final Telemetry telemetry, double targetTPS) {
		super(telemetry);
		Thread updateThread = new Thread(() -> Actions.runAction(
				new InfinityLoopAction(() -> Actions.runAction(
						new ThreadedAction(new SleepingAction((long) (1000 / targetTPS)),
						new StatementAction(super::update)
				)))
		));

		autoUpdate = true;

		Global.coreThreads.add("client-updater", updateThread);
	}
}
