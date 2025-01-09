package org.betastudio.ftc.events;

import org.firstinspires.ftc.teamcode.Local;

public class TaskCloseMonitor extends Thread{
	public Thread argument;
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
