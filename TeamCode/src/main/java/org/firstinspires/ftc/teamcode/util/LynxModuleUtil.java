package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.internal.system.Misc;

import java.util.HashMap;
import java.util.Map;

/**
 * Collection of utilities for interacting with Lynx modules.
 */
public enum LynxModuleUtil {
	;

	private static final LynxFirmwareVersion MIN_VERSION = new LynxFirmwareVersion(1, 8, 2);

    /**
     * Parsed representation of a Lynx module firmware version.
     */
    public static class LynxFirmwareVersion implements Comparable<LynxFirmwareVersion> {
        public final int major;
        public final int minor;
        public final int eng;

        public LynxFirmwareVersion(final int major, final int minor, final int eng) {
            this.major = major;
            this.minor = minor;
            this.eng = eng;
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof LynxFirmwareVersion) {
                final LynxFirmwareVersion otherVersion = (LynxFirmwareVersion) other;
                return major == otherVersion.major && minor == otherVersion.minor &&
                        eng == otherVersion.eng;
            } else {
                return false;
            }
        }

        @Override
        public int compareTo(final LynxFirmwareVersion other) {
            final int majorComp = Integer.compare(major, other.major);
            if (0 == majorComp) {
                final int minorComp = Integer.compare(minor, other.minor);
                if (0 == minorComp) {
                    return Integer.compare(eng, other.eng);
                } else {
                    return minorComp;
                }
            } else {
                return majorComp;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return Misc.formatInvariant("%d.%d.%d", major, minor, eng);
        }
    }

    /**
     * Retrieve and parse Lynx module firmware version.
     * @param module Lynx module
     * @return parsed firmware version
     */
    public static LynxFirmwareVersion getFirmwareVersion(final LynxModule module) {
        final String versionString = module.getNullableFirmwareVersionString();
        if (null == versionString) {
            return null;
        }

        final String[] parts = versionString.split("[ :,]+");
        try {
            // note: for now, we ignore the hardware entry
            return new LynxFirmwareVersion(
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[5]),
                    Integer.parseInt(parts[7])
            );
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    /**
     * Exception indicating an outdated Lynx firmware version.
     */
    public static class LynxFirmwareVersionException extends RuntimeException {
        public LynxFirmwareVersionException(final String detailMessage) {
            super(detailMessage);
        }
    }

    /**
     * Ensure all of the Lynx modules attached to the robot satisfy the minimum requirement.
     * @param hardwareMap hardware map containing Lynx modules
     */
    public static void ensureMinimumFirmwareVersion(final HardwareMap hardwareMap) {
        final Map<String, LynxFirmwareVersion> outdatedModules = new HashMap<>();
        for (final LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            final LynxFirmwareVersion version = getFirmwareVersion(module);
            if (null == version || 0 > version.compareTo(MIN_VERSION)) {
                for (final String name : hardwareMap.getNamesOf(module)) {
                    outdatedModules.put(name, version);
                }
            }
        }
        if (!outdatedModules.isEmpty()) {
            final StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append("One or more of the attached Lynx modules has outdated firmware\n");
            msgBuilder.append(Misc.formatInvariant("Mandatory minimum firmware version for Road Runner: %s\n",
                    MIN_VERSION.toString()));
            for (final Map.Entry<String, LynxFirmwareVersion> entry : outdatedModules.entrySet()) {
                msgBuilder.append(Misc.formatInvariant(
                        "\t%s: %s\n", entry.getKey(),
		                null == entry.getValue() ? "Unknown" : entry.getValue().toString()));
            }
            throw new LynxFirmwareVersionException(msgBuilder.toString());
        }
    }
}
