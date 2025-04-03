package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftDecantingStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftSample;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.utils.LinkedAction;
import org.firstinspires.ftc.teamcode.cores.eventloop.ActionBasedAutonomous;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	public static final double scaleGetPosition1 = 0.238;
	public static final double scaleGetPosition2 = 0.2905;
	public static final double scaleGetPosition3 = 0.28;

	@Override
	public void actionBuildEntry() {
		drive.setPoseEstimate(LeftDecantingStart);
		appendThreaded(
				utils.armSafe().liftDecantHigh().pack(),
				new LinkedAction(
						lineTrack(LeftDecantingStart, Decant),
						utils.boxDecant().pack()
				)
		);

		utils.waitMs(200);
		utils.boxRst();
		inputMngAction();

		appendThreaded(
				utils.liftDown().scaleOperate(scaleGetPosition1).pack(),
				lineTrack(Decant, LeftSample)
		);
	}
}
