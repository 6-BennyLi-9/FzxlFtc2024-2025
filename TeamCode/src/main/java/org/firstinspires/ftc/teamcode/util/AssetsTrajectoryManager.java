package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfig;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryGroupConfig;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Set of utilities for loading trajectories from assets (the plugin save location).
 */
public enum AssetsTrajectoryManager {
	;

	/**
     * Loads the group config.
     */
    public static @Nullable
    TrajectoryGroupConfig loadGroupConfig() {
        try {
            final InputStream inputStream = AppUtil.getDefContext().getAssets().open(
                    "trajectory/" + TrajectoryConfigManager.GROUP_FILENAME);
            return TrajectoryConfigManager.loadGroupConfig(inputStream);
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * Loads a trajectory config with the given name.
     */
    public static @Nullable TrajectoryConfig loadConfig(final String name) {
        try {
            final InputStream inputStream = AppUtil.getDefContext().getAssets().open(
                    "trajectory/" + name + ".yaml");
            return TrajectoryConfigManager.loadConfig(inputStream);
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * Loads a trajectory builder with the given name.
     */
    public static @Nullable TrajectoryBuilder loadBuilder(final String name) {
        final TrajectoryGroupConfig groupConfig = loadGroupConfig();
        final TrajectoryConfig config = loadConfig(name);
        if (null == groupConfig || null == config) {
            return null;
        }
        return config.toTrajectoryBuilder(groupConfig);
    }

    /**
     * Loads a trajectory with the given name.
     */
    public static @Nullable Trajectory load(final String name) {
        final TrajectoryBuilder builder = loadBuilder(name);
        if (null == builder) {
            return null;
        }
        return builder.build();
    }
}
