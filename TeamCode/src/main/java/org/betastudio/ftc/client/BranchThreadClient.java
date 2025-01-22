package org.betastudio.ftc.client;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;

public class BranchThreadClient extends BaseMapClient implements ThreadAdditions {
	private static  boolean auto_start_updater = true;
	protected final Action  updateAction;
	protected final Thread  updateThread;

	public BranchThreadClient(final Telemetry telemetry) {
		this(telemetry, 1);
	}

	public BranchThreadClient(final Telemetry telemetry, final double targetTPS) {
		super(telemetry);
		updateAction = new InfinityLoopAction(() -> Actions.runAction(new ThreadedAction(new SleepingAction((long) (1000 / targetTPS)), new StatementAction(super::update))));
		updateThread = new Thread(() -> Actions.runAction(updateAction));

		setAutoUpdate(false);

		if (auto_start_updater) {
			Global.threadManager.add("client-updater", updateThread);
		}
	}

	public static boolean auto_start_updater() {
		return auto_start_updater;
	}

	public static void auto_start_updater(final boolean config) {
		auto_start_updater = config;
	}

	public void start_updater() {
		if (! updateThread.isAlive()) {
			Global.threadManager.add("client-updater", updateThread);
		}
	}

	@Override
	public void closeTask() {
		assert updateAction instanceof ThreadAdditions;
		((ThreadAdditions) updateAction).closeTask();
	}

	@Override
	public boolean isUpdateRequested() {
		return false;
	}
}
