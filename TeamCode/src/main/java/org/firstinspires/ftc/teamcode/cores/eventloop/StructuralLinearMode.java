package org.firstinspires.ftc.teamcode.cores.eventloop;

public abstract class StructuralLinearMode extends IntegralLinearMode {
	@Override
	public Thread getLinearThread() {
		return new Thread(this::linear);
	}

	public abstract void linear();
}
