package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
@Disabled
@Deprecated
@Autonomous(name="OpenCVDetection", group="Auto")
public class OpenCVAutoMode extends LinearOpMode {
    DuckDetection detector = new DuckDetection(telemetry);
    OpenCvCamera webcam;
    @Override
    public void runOpMode() throws InterruptedException {


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.setPipeline(detector);
        //webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {

                webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();
        while (opModeIsActive())
        {
            switch (detector.getLocation()) {
                case "LEFT":
                    telemetry.addData("Location", "LEFT");
                    telemetry.update();
                    break;
                case "MIDDLE":
                    telemetry.addData("Location", "MIDDLE");
                    telemetry.update();
                    break;
                case "RIGHT":
                    telemetry.addData("Location", "RIGHT");
                    telemetry.update();
                    break;
            }
        }

    }

}