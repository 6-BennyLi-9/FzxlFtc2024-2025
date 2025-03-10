package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.RunMode;
import org.betastudio.ftc.selection.SelectElement;
import org.betastudio.ftc.selection.SelectPackage;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.util.ButtonConfig;
import org.betastudio.ftc.util.ButtonProcessor;
import org.firstinspires.ftc.teamcode.Global;

//@Annotations.TestDoneSuccessfully
@TeleOp(group = "9_Tests")
//@Disabled
public class SelectPackageTest extends LinearOpMode {
	public static final int ITEM_COUNT = 50;

	@Override
	public void runOpMode() throws InterruptedException {
		Global.runMode = RunMode.TELEOP;
		Global.prepareCoreThreadPool();
		final SelectPackage selections = new SelectPackage();
		final BaseMapClient client     = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.MANUALLY);

		for (int i = 0 ; i < ITEM_COUNT ; i++) {
			final int index = i + 1;
			selections.add(new SelectElement("item" + index, () -> telemetry.speak("item"+index+" selected")));
		}
		selections.update();

		client.sendMsg(selections.buildTelemetryMsg());
		client.putData("range", selections.getShowRange());
		client.putLine("操作方式：按下LEFT_BUMPER键选择上一个选项，按下RIGHT_BUMPER键选择下一个选项。");
		client.putLine("按下A键确认选择。");
		client.putLine("等待op开始后，测试也会开始。");

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
			client.sendMsg(selections.buildTelemetryMsg());
			client.update();
			//			sleep(200);
		}

		Global.runMode = RunMode.TERMINATE;
	}
}
