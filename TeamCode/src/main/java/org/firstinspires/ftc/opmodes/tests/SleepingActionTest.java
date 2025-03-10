package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

@Autonomous(group = "9_Test")
public class SleepingActionTest extends LinearOpMode {
	public static final class DebugSleepingAction extends SleepingAction {
		public long tick;

		public DebugSleepingAction(long sleepMilliseconds) {
			super(sleepMilliseconds);
		}

		@Override
		public boolean activate() {
			boolean activate = super.activate();
			++tick;
			if (tick == 1000) {
				FtcLogTunnel.MAIN.report("now:" + activate);
				tick = 0;
			}
			return activate;
		}
	}

	@Override
	public void runOpMode() throws InterruptedException {
		FtcLogTunnel.saveAndClear();
		waitForStart();
		Actions.runAction(new LinkedAction(
				new StatementAction(()->{
					telemetry.addLine("OP STARTED");
					telemetry.update();
				}),
				new DebugSleepingAction(1000),
				new StatementAction(()->{
					FtcLogTunnel.MAIN.report("EOF");
					telemetry.addLine("OP FINISHED");
					telemetry.update();
				})
		));
		FtcLogTunnel.MAIN.save(this.getClass().getSimpleName() + System.nanoTime());
		sleep(Long.MAX_VALUE);
	}
}
