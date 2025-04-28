package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.Annotations.TestDoneSuccessfully;
import static org.betastudio.ftc.util.Pose2dUtil.p;

import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * 自动程序的所有点位！
 * <p>
 * 标记有 {@link TestDoneSuccessfully} 的不要修改，已测试完毕。
 */
public final class AutonomousPositions {
	@TestDoneSuccessfully
	public static final Pose2d LeftStart       = p(40, 65, - 90);
	@TestDoneSuccessfully
	public static final Pose2d Decant          = p(55.5, 55.5, - 135);
	@TestDoneSuccessfully
	public static final Pose2d LeftSample      = p(59, 49, - 90);
	@TestDoneSuccessfully
	public static final Pose2d LeftParkPrepare = p(36, 10, 0);
}
