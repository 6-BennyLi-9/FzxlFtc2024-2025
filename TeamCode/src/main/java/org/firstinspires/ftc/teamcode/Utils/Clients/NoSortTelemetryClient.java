package org.firstinspires.ftc.teamcode.Utils.Clients;

import android.util.Pair;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

import java.util.Map;
import java.util.Objects;

@UserRequirementFunctions
public class NoSortTelemetryClient extends TelemetryClient{
	public NoSortTelemetryClient(Telemetry telemetry) {
		super(telemetry);
	}

	@Override
	public void update() {
		String cache;
		for (Map.Entry<String, Pair<String, Integer>> entry : data.entrySet()) {
			String k = entry.getKey();
			Pair<String, Integer> v = entry.getValue();
			cache= Objects.equals(v.first, "") ? v.first:k+":"+v.first;
			if (showIndex) {
				telemetry.addLine("[" + v.second + "]" + cache);
			} else {
				telemetry.addLine(k + ":" + cache);
			}
		}

		telemetry.update();
	}
}
