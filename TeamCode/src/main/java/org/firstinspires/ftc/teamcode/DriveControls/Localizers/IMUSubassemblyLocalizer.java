package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.plugins.ImuLocalizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.Localizer;
import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.SubassemblyLocalizer;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.utils.Annotations.LocalizationSubassembly;

@LocalizationSubassembly
public class IMUSubassemblyLocalizer extends SubassemblyLocalizer implements Localizer {
	public IMUSubassemblyLocalizer(Classic classic) {
		super(new ImuLocalizer(classic));
	}

	@Override
	public Pose2d getCurrentPose() {
		update();
		return RobotPosition;
	}
}
