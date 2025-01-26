package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.client.ViewMode;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.teamcode.Global;

@Autonomous(group = "9_Tests")
@Disabled
@TestDone
public class FtcLogTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		Global.auto_create_monitor(false);
		Global.prepareCoreThreadPool();
		Global.auto_create_monitor(true);
		final BaseMapClient client =new BaseMapClient(telemetry);

		client.configViewMode(ViewMode.LOG);
		client.setTargetLogTunnel(FtcLogTunnel.MAIN);

		FtcLogTunnel.clear();
		FtcLogTunnel.MAIN.report("Op In INIT");
		client.update();

		waitForStart();

		FtcLogTunnel.MAIN.report("Op In RUN");
		client.update();

		sleep(500);

		FtcLogTunnel.MAIN.report("Op Is DONE");
		client.update();

		sleep(1000);

		TelemetryMsg msg=FtcLogTunnel.MAIN.call();

		for (TelemetryElement s : msg.getElements()) {
			telemetry.addData("found element", msg.toString());
			if(s instanceof TelemetryItem){
				telemetry.addLine("the element is instance of TI");
				telemetry.addData(((TelemetryItem) s).capital,((TelemetryItem) s).value);
			} else if (s instanceof TelemetryLine) {
				telemetry.addLine("the element is instance of TL");
				telemetry.addLine(((TelemetryLine) s).line);
			} else {
				telemetry.addLine("the element's type is unknown");
			}
		}
		telemetry.update();

		sleep(500000L);
//		client.closeTask();
	}
}
