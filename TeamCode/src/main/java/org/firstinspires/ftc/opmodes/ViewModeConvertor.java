package org.firstinspires.ftc.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.client.ClientViewMode;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.util.ButtonProcessor;
import org.betastudio.ftc.util.selection.SelectElement;
import org.betastudio.ftc.util.selection.SelectPackage;
import org.firstinspires.ftc.teamcode.ButtonConfig;

import java.util.concurrent.atomic.AtomicReference;

@TeleOp(group = "zzz")
public class ViewModeConvertor extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		final SelectPackage                    selections  = new SelectPackage();
		final AtomicReference <ClientViewMode> target      = new AtomicReference <>(ClientViewMode.ORIGIN_TELEMETRY);
		final BaseMapClient                    client      = new BaseMapClient(telemetry);
		final ButtonProcessor                  select_prev = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor                  select_next = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
		final ButtonProcessor                  submit      = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);

		for (final ClientViewMode mode : ClientViewMode.values()) {
			selections.add(new SelectElement(mode.name(), () -> target.set(mode)));
		}

		client.setUpdateConfig(UpdateConfig.MANUAL_UPDATE_REQUESTED);
		client.configViewMode(ClientViewMode.ORIGIN_TELEMETRY);
		selections.update();
		client.sendMsg(selections.buildTelemetryMsg());
		client.update();

		while (opModeInInit()) {
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
			client.putLine("启动OpMode以保存配置。");
			client.sendMsg(selections.buildTelemetryMsg());
			client.update();
		}

		if (! isStopRequested()) {
			client.configViewMode(target.get());
		}
	}
}
