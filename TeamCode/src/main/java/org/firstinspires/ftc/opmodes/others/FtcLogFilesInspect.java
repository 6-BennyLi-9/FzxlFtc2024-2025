package org.firstinspires.ftc.opmodes.others;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.log.FtcLogFiles;

@TeleOp(name = "日志", group = "zzz")
public class FtcLogFilesInspect extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		FtcLogFiles.getFiles().forEach(f->telemetry.addData("[ ]",f.getSaveTime()));
	}
}
