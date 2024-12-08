package org.betastudio.ftc.client;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BranchThreadClient extends TelemetryClient {
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

	public BranchThreadClient(final Telemetry telemetry) {
		this(telemetry, 1);
	}

	public BranchThreadClient(final Telemetry telemetry, double targetTPS) {
		super(telemetry);
		updateThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()){
				new ThreadedAction(
						new SleepingAction((long) (1000 / targetTPS)),
						new StatementAction(this::update)
				).run();
			}
		});
		try {
//			telemetry.setAutoClear(false);
			autoUpdate = true;
		} catch (final UnsupportedOperationException ignore) {
		}
	}

	public void startThread(){
		updateThread.start();
	}

	public void interrupt() {
		updateThread.suspend();
		clear();
	}
}
