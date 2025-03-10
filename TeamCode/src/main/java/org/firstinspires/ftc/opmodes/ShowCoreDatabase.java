package org.firstinspires.ftc.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CoreDatabase;

@Disabled
@TeleOp(name = "显示数据库", group = "zzz")
public class ShowCoreDatabase extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.addData("autonomous_time_used", CoreDatabase.autonomous_time_used);
		telemetry.addData("pose", CoreDatabase.pose);
		telemetry.addData("last_terminate_reason", CoreDatabase.last_terminate_reason.name());
		telemetry.addData("orientation", CoreDatabase.orientation);
		telemetry.update();

		sleep(1000000000);
	}
}
