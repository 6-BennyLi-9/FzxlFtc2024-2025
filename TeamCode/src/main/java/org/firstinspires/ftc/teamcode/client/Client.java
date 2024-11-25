package org.firstinspires.ftc.teamcode.client;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.action.Actions;
import org.firstinspires.ftc.teamcode.action.utils.SleepingAction;

public class Client extends TelemetryClient{
	private final Thread updateThread;
	public Client(final Telemetry telemetry) {
		super(telemetry);
		updateThread=new  Thread(()->{
			while (true){
				telemetry.update();
				Actions.runAction(new SleepingAction(1000));
			}
		});
		try {
//			telemetry.setAutoClear(false);
			autoUpdate=false;
		}catch (UnsupportedOperationException ignore){}
	}

	public void interrupt(){
		updateThread.interrupt();
		clear();
	}
}
