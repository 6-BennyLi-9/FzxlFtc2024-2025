package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftSuspend;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.cores.eventloop.AutonomousHead;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends AutonomousHead {
	public static final double scaleGetPosition1 = 0.238;
	public static final double scaleGetPosition2 = 0.2905;
	public static final double scaleGetPosition3 = 0.28;

	@Override
	public void actionBuildEntry() {
		drive.setPoseEstimate(LeftStart);
		builder.append(new ThreadedAction(
				utils.armsToSafePosition().liftSuspendHighPrepare().pack(),
				new LinkedAction(
						driveAction(drive.trajectoryBuilder(LeftStart).lineToLinearHeading(LeftSuspend).build()),
						utils.liftSuspendHigh().pack()
				)
		));
	}
}
