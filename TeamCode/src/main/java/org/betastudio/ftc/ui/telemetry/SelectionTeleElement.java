package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SelectionTeleElement implements TelemetryElement{
	private final String title;
	private boolean selected;

	public SelectionTeleElement(final String title) {
		this.title = title;
	}

	@Override
	public void push(@NonNull final Telemetry telemetry) {
		if(selected){
			telemetry.addData("[ ]", title);
		}else{
			telemetry.addData("[X]", title);
		}
	}

	public void press(){
		selected =!selected;
	}

	public String getTitle() {
		return title;
	}
}
