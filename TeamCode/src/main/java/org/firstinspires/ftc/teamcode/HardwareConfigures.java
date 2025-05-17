package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public final class HardwareConfigures {
	public static int    ARM_MIN_POSITION         = 0;
	public static double ARM_MAX_POSITION         = 0.92;
	public static double ARM_LEFT_ADDITION        = 0.08;
	public static double ARM_INTAKE               = 0.1;
	public static double ARM_IDLE                 = 0.8;
	public static double ARM_SAFE                 = 0.61;
	public static double ARM_RISE                 = 0.37;
	public static double CLAW_OPEN                = 0.6;
	public static double CLAW_HALF_OPEN           = 0.55;
	public static double CLAW_CLOSE               = 0.45;
	public static int    CLIP_OPEN                = 0;
	public static double CLIP_CLOSE               = 0.5;
	public static int    LIFT_DECANT_LOW          = 1080;
	public static int    LIFT_DECANT_HIGH         = 2000;
	public static int    LIFT_SUSPEND             = 750;
	public static int    LIFT_SUSPEND_PREPARE     = 1250;
	public static int    LIFT_SUSPEND_Lv1         = 810;//770
	public static int    LIFT_SUSPEND_Lv2_PREPARE = 1690;
	public static int    LIFT_SUSPEND_Lv2         = 800;//750
	public static int    LIFT_IDLE                = 0;
	public static int    PLACE_DECANT             = 1;
	public static int    PLACE_IDLE               = 0;
	public static double PLACE_PREPARE            = 0.5;
	public static double ROTATE_DEFAULT           = 0.79;
	public static double SCALE_MIN_POSITION       = 0;
	public static double SCALE_MAX_POSITION       = 0.58;
	public static double SCALE_PROBE              = 0.5;
	public static double SCALE_BACH               = 0;
	public static double RATCHET_LOOSEN           = 0.05;
	public static double RATCHET_TIGHT            = 0.8;

	public static double DRIVER_TRIGGER_BUF_FAL = 0.2;
	public static double ROTATE_TRIGGER_BUF_FAL = 0.03;
	public static double SCALE_BUF_FAL          = 0.03;
}
