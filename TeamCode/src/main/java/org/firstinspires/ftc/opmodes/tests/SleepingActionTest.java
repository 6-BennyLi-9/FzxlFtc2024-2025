package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

@Autonomous(group = "9_Test")
public class SleepingActionTest extends LinearOpMode {
	public static final class DebugSleepingAction extends SleepingAction {
		public DebugSleepingAction(long sleepMilliseconds) {
			super(sleepMilliseconds);
		}

		@Override
		public boolean activate() {
			boolean activate = super.activate();
			FtcLogTunnel.MAIN.report("now:"+activate);
			return activate;
		}
	}

	@Override
	public void runOpMode() throws InterruptedException {
		Actions.runAction(new DebugSleepingAction(1000));
		FtcLogTunnel.MAIN.save();
		telemetry.addLine("OP FINISHED");
		telemetry.update();
		sleep(Long.MAX_VALUE);
	}
}
