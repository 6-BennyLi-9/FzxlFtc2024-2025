package org.firstinspires.ftc.cores.eventloop;

public abstract class IntegralLinearModeImpl extends IntegralLinearMode {
	@Override
	public Thread getLinearThread() {
		return new Thread(this::linear);
	}

	public abstract void linear();
}
