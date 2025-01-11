package org.firstinspires.ftc.teamcode.events;

import org.firstinspires.ftc.teamcode.Local;

public final class TaskCloseMonitor extends Thread{
	public final Thread argument;
	public final long timeLimit;

	public TaskCloseMonitor(Thread argument){
		this(argument,5000);
	}
	public TaskCloseMonitor(Thread argument,long timeLimit){
		this.argument=argument;
		this.timeLimit=timeLimit;
	}

	@Override
	public void run() {
		Local.sleep(timeLimit);
		if(argument.getState()!=State.TERMINATED){
			argument.interrupt();
		}
	}
}
