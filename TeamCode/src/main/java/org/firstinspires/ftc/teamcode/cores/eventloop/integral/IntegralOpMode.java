package org.firstinspires.ftc.teamcode.cores.eventloop.integral;

import org.firstinspires.ftc.teamcode.cores.eventloop.OpTerminateException;
import org.firstinspires.ftc.teamcode.cores.eventloop.TerminateReason;

public interface IntegralOpMode {
	default void sendTerminateSignal(final TerminateReason reason){
		sendTerminateSignal(reason, new OpTerminateException(reason.name()));
	}

	void sendTerminateSignal(TerminateReason reason, Exception e);
}
