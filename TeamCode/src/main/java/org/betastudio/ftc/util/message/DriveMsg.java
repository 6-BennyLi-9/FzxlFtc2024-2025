package org.betastudio.ftc.util.message;

/**
 * 仅重命名
 */
public class DriveMsg extends DriveBufMsg {
	public DriveMsg(final double bufX, final double bufY, final double bufTurn) {
		super(bufX, bufY, bufTurn);
	}
}
