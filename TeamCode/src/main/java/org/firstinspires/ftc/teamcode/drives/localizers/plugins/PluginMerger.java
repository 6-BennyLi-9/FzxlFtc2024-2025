package org.firstinspires.ftc.teamcode.drives.localizers.plugins;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.localizers.definition.HeadingLocalizerPlugin;
import org.firstinspires.ftc.teamcode.drives.localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.annotations.LocalizationSubassembly;
import org.firstinspires.ftc.teamcode.utils.clients.DashboardClient;
import org.jetbrains.annotations.Contract;

@LocalizationSubassembly
public class PluginMerger {
	@NonNull
	public static PositionLocalizerPlugin mergePlugins(PositionLocalizerPlugin positionPlugin, HeadingLocalizerPlugin headingPlugin,
	                                                   String color,String tag){
		return new PositionLocalizerPlugin() {
			@NonNull
			@Contract(" -> new")
			@Override
			public Position2d getCurrentPose() {
				return new Position2d(positionPlugin.getCurrentPose().toVector(),headingPlugin.getHeadingDeg());
			}

			@Override
			public void update() {
				positionPlugin.update();
				headingPlugin.update();
			}

			@Override
			public void drawRobotWithoutSendingPacket() {
				DashboardClient.getInstance().drawRobot(getCurrentPose(),color,tag);
			}
		};
	}
}
