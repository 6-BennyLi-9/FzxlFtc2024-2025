package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DuckDetection extends OpenCvPipeline {
    Telemetry telemetry;
    Mat mat = new Mat();

    public String location = "";

    //定义roi检测物体
    static final Rect ROI = new Rect(
            new Point(300, 100),
            new Point(600, 300));
    static double PERCENT_COLOR_THRESHOLD = 0.4;

    public DuckDetection(Telemetry t) { telemetry = t; }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        //HSV颜色值 需要根据物体更改
        Scalar lowHSV = new Scalar(26, 43, 46);
        Scalar highHSV = new Scalar(34, 255, 255);

        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat roi = mat.submat(ROI);

        double Value = Core.sumElems(roi).val[0] / ROI.area() / 255;


        roi.release();

        //输出roi范围内的概率

        telemetry.addData("roi percentage", Math.round(Value * 100) + "%");

        boolean value = Value > PERCENT_COLOR_THRESHOLD;

        if (value) {
            location = "YES";
            telemetry.addData("Location", "YES");
        }
        telemetry.update();

        //Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar color = new Scalar(255, 0, 0);

        Imgproc.rectangle(mat, ROI, color);

        return mat;
    }

    public String getLocation() {
        return location;
    }
}