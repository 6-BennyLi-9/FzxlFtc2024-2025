package org.exboithpath.loclaizer;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.exboithpath.graphics.Pose;

public class ThreeDeadWheelLocalizer extends AbstractLocalizer{
	private Pose poseEst;

	protected ThreeDeadWheelLocalizer(HardwareMap hardwareMap) {
		super(hardwareMap);
	}

	@Override
	public void setPoseEstimate(Pose newPose) {
		poseEst=newPose;
	}

	@Override
	public Pose update() {
		return poseEst;
	}
}
