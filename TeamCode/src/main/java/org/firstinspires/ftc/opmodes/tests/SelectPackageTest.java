package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.util.ButtonProcessor;
import org.betastudio.ftc.util.selection.SelectElement;
import org.betastudio.ftc.util.selection.SelectPackage;
import org.firstinspires.ftc.teamcode.ButtonConfig;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.RunMode;

@TestDoneSuccessfully
@TeleOp(group = "9_Tests")
@Disabled
public class SelectPackageTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		Global.runMode = RunMode.TELEOP;
		Global.prepareCoreThreadPool();
		final SelectPackage selections = new SelectPackage();
		final BaseMapClient client     = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.MANUAL_UPDATE_REQUESTED);

		selections.add(new SelectElement("item1", () -> telemetry.speak("item1 selected")));
		selections.add(new SelectElement("item2", () -> telemetry.speak("item2 selected")));
		selections.add(new SelectElement("item3", () -> telemetry.speak("item3 selected")));
		selections.add(new SelectElement("item4", () -> telemetry.speak("item4 selected")));
		selections.add(new SelectElement("item5", () -> telemetry.speak("item5 selected")));
		selections.update();

		client.send(selections.buildTelemetryMsg());
		client.addData("range", selections.getShow_range());
		client.addLine("操作方式：按下LEFT_BUMPER键选择上一个选项，按下RIGHT_BUMPER键选择下一个选项。");
		client.addLine("按下A键确认选择。");
		client.addLine("等待op开始后，测试也会开始。");

		final ButtonProcessor select_prev = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor select_next = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor submit      = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);

		client.update();
		waitForStart();

		while (! isStopRequested()) {
			select_prev.sync(gamepad1.left_bumper);
			select_next.sync(gamepad1.right_bumper);
			submit.sync(gamepad1.a);

			if (select_prev.getEnabled()) {
				selections.select_previous();
			}
			if (select_next.getEnabled()) {
				selections.select_next();
			}
			if (submit.getEnabled()) {
				selections.submit_selected();
			}

			client.clear();
			client.send(selections.buildTelemetryMsg());
			client.update();
			//			sleep(200);
		}

		Global.runMode = RunMode.TERMINATE;
	}
}
