package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Enums.runningState;

public class RoboticActions extends Robot{
	public RoboticActions(HardwareMap hardwareMap, runningState state, Client client) {
		super(hardwareMap, state, client);
	}
}
