package org.firstinspires.ftc.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.betastudio.ftc.ui.client.BaseMapClient;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.selection.SelectElement;
import org.betastudio.ftc.util.selection.SelectPackage;

@TeleOp(group = "9_Tests")
public class SelectPackageTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		SelectPackage selections=new SelectPackage();
		BaseMapClient client=new BaseMapClient(telemetry);

		selections.add(new SelectElement("item1",()-> telemetry.speak("item1 selected")));
		selections.add(new SelectElement("item2",()-> telemetry.speak("item2 selected")));
		selections.add(new SelectElement("item3",()-> telemetry.speak("item3 selected")));
		selections.add(new SelectElement("item4",()-> telemetry.speak("item4 selected")));
		selections.add(new SelectElement("item5",()-> telemetry.speak("item5 selected")));

		client.send(selections.buildTelemetryMsg());
		client.addData("range",selections.getShow_range());
//		selections.getElements().forEach(e-> client.addData(e.getName(),e.isSelected()));
		client.update();
		sleep(1000);
		for (TelemetryElement s : selections.buildTelemetryMsg().getElements()) {
			telemetry.addData("found element", s.toString());
			if(s instanceof TelemetryItem){
				telemetry.addLine("the element is instance of TI");
				telemetry.addData(((TelemetryItem) s).capital,((TelemetryItem) s).value);
			} else if (s instanceof TelemetryLine) {
				telemetry.addLine("the element is instance of TL");
				telemetry.addLine(((TelemetryLine) s).line);
			} else {
				telemetry.addLine("the element's type is unknown");
			}
		}
		telemetry.update();
		

		sleep(5000000);
	}
}
