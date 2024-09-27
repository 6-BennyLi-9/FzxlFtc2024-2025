package org.firstinspires.ftc.team19419.Localizers.Plugin;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.team19419.Localizers.Definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.team19419.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.team19419.Hardwares.Classic;
import org.firstinspires.ftc.team19419.Utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public class BNOHeadingLocalizer implements HeadingLocalizerPlugin {
	private final Sensors sensors;
	public double RobotHeading;

	public BNOHeadingLocalizer(@NonNull Classic classic){
		sensors=classic.sensors;
	}

	@Override
	public double getHeadingDeg() {
		return RobotHeading;
	}

	@Override
	public void update() {
		sensors.imu.update();
		RobotHeading=sensors.RobotAngle();
	}
}
