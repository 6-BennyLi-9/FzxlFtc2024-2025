package org.firstinspires.ftc.teamcode.eventloop.integral;

import org.firstinspires.ftc.teamcode.eventloop.OpTerminateException;
import org.firstinspires.ftc.teamcode.eventloop.TerminateReason;

/**
 * 运行结束返回结束原因
 */
public interface IntegralOpMode {
	default void sendTerminateSignal(final TerminateReason reason) {
		sendTerminateSignal(reason, new OpTerminateException(reason.name()));
	}

	void sendTerminateSignal(TerminateReason reason, Exception e);
}
