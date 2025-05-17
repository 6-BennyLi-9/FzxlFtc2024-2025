package org.firstinspires.ftc.teamcode.structure;

public final class HardwareSituation {
	public enum ArmPositions {
		IDLE, INTAKE, SAFE, RISE
	}

	public enum ClawPositions {
		OPEN, CLOSE
	}

	public enum ClipPositions {
		OPEN, CLOSE
	}

	public enum LiftMode {
		IDLE, DECANT_LOW, DECANT_HIGH, SUSPEND, SUSPEND_PREPARE, SUSPEND_Lv1, SUSPEND_Lv2_PREPARE, SUSPEND_Lv2
	}

	public enum PlacePositions {
		IDLE, DECANT, PREPARE
	}

	public enum ScalePositions {
		BACK, PROBE
	}
}
