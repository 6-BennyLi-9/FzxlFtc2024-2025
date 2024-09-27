package org.firstinspires.ftc.team19419.Hardwares.Integration.Sensors;

import org.firstinspires.ftc.team19419.Hardwares.Integration.Integrations;

public abstract class IntegrationSensor implements Integrations {
	public final String name;

	public IntegrationSensor(String name){
		this.name=name;
	}

	public abstract void update();
}
