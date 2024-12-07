package org.betastudio.ftc.client;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;

public class Client extends TelemetryClient {
	public static class InfinityLoopAction implements Action {
		private final Runnable runnable;
		private       boolean  interrupted;

		InfinityLoopAction(final Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public boolean run() {
			runnable.run();
			return ! interrupted;
		}

		public void interrupt() {
			interrupted = true;
		}
	}

	private final Thread updateThread;

	public Client(final Telemetry telemetry) {
		this(telemetry, 1);
	}

	public Client(final Telemetry telemetry, double targetTPS) {
		super(telemetry);
		updateThread = new Thread(() -> Actions.runAction(new InfinityLoopAction(() -> Actions.runAction(new ThreadedAction(new SleepingAction((long) (1000 / targetTPS)), new StatementAction(super::update))))));
		try {
//			telemetry.setAutoClear(false);
			autoUpdate = true;
		} catch (final UnsupportedOperationException ignore) {
		}
	}

	public void interrupt() {
		updateThread.interrupt();
		clear();
	}
}
