package org.firstinspires.ftc.team19419.Localizers.Plugin;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.team19419.Localizers.Definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.team19419.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.team19419.Utils.Clients.Client;

/**
 * 使用imu获取机器的角度
 */
public class BNODeadWheelLocalizer extends DeadWheelLocalizer implements PositionLocalizerPlugin {
	public BNODeadWheelLocalizer(Client client, Sensors sensors) {
		super(client, sensors);
	}

	@Override
	public void update() {
		super.update();
		sensors.imu.update();
		robotPosition=new Pose2d(robotPosition.position,Math.toRadians(sensors.imu.RobotAngle));
	}

	@Override
	public Pose2d getCurrentPose() {
		return robotPosition;
	}
}
