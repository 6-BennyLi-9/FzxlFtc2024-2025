package org.firstinspires.ftc.teamcode.events;

import org.firstinspires.ftc.teamcode.Local;

public final class TaskCloseMonitor extends Thread{
	private final Thread argument;
	public TaskCloseMonitor(Thread argument){
		this.argument=argument;
	}

	@Override
	public void run() {
		Local.sleep(5000);
		if(argument.getState()!=State.TERMINATED){
			argument.interrupt();
		}
	}
}
