package org.firstinspires.ftc.opmodes.others;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.client.ClientViewMode;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.log.FtcLogFile;
import org.betastudio.ftc.ui.log.FtcLogFiles;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.util.ButtonProcessor;
import org.betastudio.ftc.util.selection.SelectElement;
import org.betastudio.ftc.util.selection.SelectPackage;
import org.firstinspires.ftc.teamcode.ButtonConfig;

@TeleOp(name = "日志检查", group = "zzz")
public class FtcLogFilesInspect extends LinearOpMode {
	private final ButtonProcessor select_prev        = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
	private final ButtonProcessor select_next        = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
	private final ButtonProcessor chang_message_sent = new ButtonProcessor(ButtonConfig.SINGLE_WHEN_PRESSED);
	private SelectPackage files_select;
	private FtcLogFile    selected_file;
	private Boolean       is_files_selected = false, show_logs = false;

	@Override
	public void runOpMode() throws InterruptedException {
		FtcLogTunnel.saveAndClear();
		files_select = new SelectPackage();
		SelectPackage logs_select = new SelectPackage();
		BaseMapClient client = new BaseMapClient(telemetry);

		FtcLogFiles.getFiles().forEach(file -> files_select.add(new SelectElement(String.valueOf(file.getFileName()), () -> {
			selected_file = file;
			is_files_selected = true;
		})));
		files_select.update();
		client.configViewMode(ClientViewMode.ORIGIN_TELEMETRY);
		client.setUpdateConfig(UpdateConfig.MANUAL_UPDATE_REQUESTED);

		while (opModeInInit()) {
			select_prev.sync(gamepad1.left_bumper);
			select_next.sync(gamepad1.right_bumper);

			if (select_prev.getEnabled()) {
				files_select.select_previous();
			}
			if (select_next.getEnabled()) {
				files_select.select_next();
			}

			client.clear();
			client.addLine("启动OpMode以进入日志查看模式。");
			client.send(files_select.buildTelemetryMsg());
			client.update();
		}

		files_select.submit_selected();
		for (TelemetryElement element : selected_file.call().getElements()) {
			logs_select.add(new SelectElement(element.toString(), null));
		}
		FtcLogTunnel.MAIN.report("Log selected:" + selected_file.getFileName());

		while (! isStopRequested()) {
			select_prev.sync(gamepad1.left_bumper);
			select_next.sync(gamepad1.right_bumper);
			chang_message_sent.sync(gamepad1.a);

			if (select_prev.getEnabled()) {
				logs_select.select_previous();
			}
			if (select_next.getEnabled()) {
				logs_select.select_next();
			}
			if (chang_message_sent.getEnabled()) {
				show_logs = ! show_logs;
			}

			client.clear();
			client.send(show_logs ? logs_select.buildTelemetryMsg() : selected_file.call());
			client.update();
		}

		sleep(Long.MAX_VALUE);
	}
}
