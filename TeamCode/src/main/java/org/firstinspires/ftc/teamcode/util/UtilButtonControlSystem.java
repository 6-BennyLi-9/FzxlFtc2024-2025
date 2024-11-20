package org.firstinspires.ftc.teamcode.util;

public final class UtilButtonControlSystem {
	public enum ButtonConfig{
		WhilePressing,
		SingleWhenPressed
	}
	public final ButtonConfig config;
	public final Ticker ticker;

	private boolean lst,now;

	public UtilButtonControlSystem(final ButtonConfig config){
		this.config=config;
		ticker=new Ticker();
	}

	public void sync(final boolean input){
		switch (config){
			case WhilePressing:
				now=input;
				break;
			case SingleWhenPressed:
			default:
				lst=now;
				now=input;
				break;
		}
	}

	public boolean getEnabled(){
		switch (config){
			case WhilePressing:
				return now;
			case SingleWhenPressed:
			default:
				return now&&!lst;
		}
	}
}
