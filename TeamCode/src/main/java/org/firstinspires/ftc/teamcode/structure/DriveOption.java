package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum DriveOption {
	;
	private static final class DriveOptions implements Action {
		private final double x,y,turn;
		DriveOptions(final double x, final double y, final double turn){
			this.x=x;
			this.y=y;
			this.turn=turn;
		}

		@Override
		public boolean run() {
			HardwareConstants.leftFront.setPower	(y+x-turn);
			HardwareConstants.leftRear.setPower		(y-x-turn);
			HardwareConstants.rightFront.setPower	(y-x+turn);
			HardwareConstants.rightRear.setPower	(y+x+turn);
			return false;
		}
	}

	@NonNull
	@Contract("_, _, _ -> new")
	public static Action build(final double x, final double y, final double turn){
		return new DriveOptions(x,y,turn);
	}
	@NonNull
	@Contract("_, _, _, _ -> new")
	public static Action build(final double x, final double y, final double turn,final double bufPower){
		return new DriveOptions(x*bufPower,y*bufPower,turn*bufPower);
	}

}
