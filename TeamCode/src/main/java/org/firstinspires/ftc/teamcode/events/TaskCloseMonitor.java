package org.firstinspires.ftc.teamcode.events;

import org.firstinspires.ftc.teamcode.Local;

public final class TaskCloseMonitor extends Thread{
	public final Thread argument;
	public final long timeLimit;

	public TaskCloseMonitor(final Thread argument){
		this(argument,5000);
	}
	public TaskCloseMonitor(final Thread argument, final long timeLimit){
		this.argument=argument;
		this.timeLimit=timeLimit;
	}

	@Override
	public void run() {
		Local.sleep(timeLimit);
		if(State.TERMINATED != argument.getState()){
			argument.interrupt();
		}
	}
}
