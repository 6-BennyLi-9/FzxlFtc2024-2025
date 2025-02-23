package org.exboithpath.loclaizer;

import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer;
import org.exboithpath.graphics.Pose;

import java.util.ArrayList;

public class MirrorRoadrunnerLocalizer implements Localizer{
	public ThreeTrackingWheelLocalizer mirror;

	public MirrorRoadrunnerLocalizer(HardwareMap hardwareMap){
		mirror=new StandardTrackingWheelLocalizer(hardwareMap,new ArrayList <>(),new ArrayList <>());
	}

	@Override
	public void setPoseEstimate(Pose newPose) {
		mirror.setPoseEstimate(newPose.toPose2d());
	}

	@Override
	public Pose update() {
		mirror.update();
		return Pose.poseOf(mirror.getPoseEstimate());
	}
}
