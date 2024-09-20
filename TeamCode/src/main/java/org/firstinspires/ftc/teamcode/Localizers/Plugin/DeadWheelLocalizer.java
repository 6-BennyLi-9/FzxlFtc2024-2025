package org.firstinspires.ftc.teamcode.Localizers.Plugin;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.Localizers.Definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Localizers.Odometry.ArcOrganizedOdometer;
import org.firstinspires.ftc.teamcode.Localizers.Odometry.Odometry;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationPlugin;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;

@LocalizationPlugin
public class DeadWheelLocalizer implements PositionLocalizerPlugin {
	protected final Odometry odometry;
	protected final Sensors sensors;
	public Pose2d robotPosition;

	public DeadWheelLocalizer(Client client,Sensors sensors){
		odometry=new ArcOrganizedOdometer(client);
		this.sensors=sensors;
	}

	@Override
	public void update() {
		sensors.updateEncoders();//防止mspt过高
		odometry.update(sensors.getDeltaL()* Params.LateralInchPerTick ,sensors.getDeltaA() * Params.AxialInchPerTick,
				sensors.getDeltaT() * Params.TurningDegPerTick);
		robotPosition=odometry.getCurrentPose();
	}

	@Override
	public Pose2d getCurrentPose() {
		return robotPosition;
	}
}
