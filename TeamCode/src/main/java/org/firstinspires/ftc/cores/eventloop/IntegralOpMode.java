package org.firstinspires.ftc.cores.eventloop;

public interface IntegralOpMode {
	void sendTerminateSignal(TerminateReason reason);

	void sendTerminateSignal(TerminateReason reason, Exception e);
}
