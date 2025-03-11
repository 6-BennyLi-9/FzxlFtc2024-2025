package org.firstinspires.ftc.teamcode.cores.eventloop;

/**
 * 提供结构化方法
 */
public abstract class StructuralLinearMode extends IntegralLinearMode {
	@Override
	public Thread getLinearThread() {
		return new Thread(this::linear);
	}

	public abstract void linear();
}
