package org.firstinspires.ftc.opmodes;

import static org.betastudio.ftc.ui.client.ClientViewMode.ORIGIN_TELEMETRY;
import static org.betastudio.ftc.ui.client.UpdateConfig.MANUALLY;
import static org.betastudio.ftc.util.ButtonConfig.SINGLE_WHEN_PRESSED;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.selection.SelectElement;
import org.betastudio.ftc.selection.SelectPackage;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.log.FtcLogFile;
import org.betastudio.ftc.ui.log.FtcLogFilesBase;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.ui.telemetry.LogTelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.ButtonProcessor;
import org.betastudio.ftc.util.message.TelemetryMsg;

import java.util.Objects;

@TeleOp(name = "日志检查", group = "zzz")
public class FtcLogFilesInspect extends LinearOpMode {
	private final        ButtonProcessor select_prev       = new ButtonProcessor(SINGLE_WHEN_PRESSED);
	private final        ButtonProcessor select_next       = new ButtonProcessor(SINGLE_WHEN_PRESSED);
	private final        ButtonProcessor submit            = new ButtonProcessor(SINGLE_WHEN_PRESSED);
	private              SelectPackage   files_select;
	private              FtcLogFile      selected_file;
	private              Boolean         is_files_selected = false;
	private              TelemetryMsg    log_message       = new TelemetryMsg(new TelemetryLine("**unselected**"));

	@Override
	public void runOpMode() throws InterruptedException {
		FtcLogTunnel.saveAndClear();
		files_select = new SelectPackage();
		final SelectPackage logs_select = new SelectPackage();
		final BaseMapClient client      = new BaseMapClient(telemetry);

		FtcLogFilesBase.getFiles().forEach(file -> files_select.add(new SelectElement(String.valueOf(file.getFileName()), () -> {
			selected_file = file;
			is_files_selected = true;
		})));
		files_select.update();
		client.configViewMode(ORIGIN_TELEMETRY);
		client.setUpdateConfig(MANUALLY);

		while (opModeInInit() && ! isStopRequested()) {
			select_prev.sync(gamepad1.left_bumper);
			select_next.sync(gamepad1.right_bumper);

			if (select_prev.getEnabled()) {
				files_select.select_previous();
			}
			if (select_next.getEnabled()) {
				files_select.select_next();
			}

			client.clear();
			client.putLine("启动OpMode以进入日志查看模式。");
			client.sendMsg(files_select.buildTelemetryMsg());
			client.update();
		}

		files_select.submit_selected();
		for (final TelemetryElement element : Objects.requireNonNull(selected_file.callMsg()).getElements()) {
			if (element instanceof LogTelemetryItem) {
				logs_select.add(new SelectElement(element.toString(), () -> log_message = ((LogTelemetryItem) element).getLogElement().getMessage().buildTelemetryMsg()));
			} else {
				logs_select.add(new SelectElement(element.toString(), () -> {}));
			}
		}
		FtcLogTunnel.MAIN.report("Log selected:" + selected_file.getFileName());

		while (! isStopRequested()) {
			select_prev.sync(gamepad1.left_bumper);
			select_next.sync(gamepad1.right_bumper);
			submit.sync(gamepad1.a);

			if (select_prev.getEnabled()) {
				logs_select.select_previous();
			}
			if (select_next.getEnabled()) {
				logs_select.select_next();
			}
			if (submit.getEnabled()) {
				logs_select.submit_selected();
			}

			client.clear();
			client.sendMsg(logs_select.buildTelemetryMsg());
			client.putLine("===================");
			client.sendMsg(log_message);
			client.update();
		}
	}
}
