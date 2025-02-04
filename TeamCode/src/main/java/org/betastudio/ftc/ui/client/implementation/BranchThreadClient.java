package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.ui.client.InfinityLoopAction;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.util.entry.ThreadEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;

public class BranchThreadClient extends BaseMapClient implements ThreadEx {
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

		super.setUpdateConfig(UpdateConfig.MANUAL_UPDATE_REQUESTED);

		if (auto_start_updater) {
			start_updater();
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
		assert updateAction instanceof ThreadEx;
		((ThreadEx) updateAction).closeTask();
	}

	@Override
	public boolean isUpdateRequested() {
		return false;
	}

	@Override
	public UpdateConfig getUpdateConfig() {
		return UpdateConfig.THREAD_REQUIRED;
	}

	@Override
	public void setUpdateConfig(@NonNull final UpdateConfig updateConfig) {
		throw new IllegalStateException("Cannot set update config for BranchThreadClient");
	}
}
