package org.firstinspires.ftc.cores.eventloop;

public interface IntegralOpMode {
	default void sendTerminateSignal(final TerminateReason reason){
		sendTerminateSignal(reason, new OpTerminateException(reason.name()));
	}

	void sendTerminateSignal(TerminateReason reason, Exception e);
}
