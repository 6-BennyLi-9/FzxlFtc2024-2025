package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Deprecated
public class OpenCV extends OpenCvPipeline {
    Telemetry telemetry;
    Mat mat = new Mat();
    Mat matRed = new Mat();
    Mat matBlue = new Mat();

    public String location = "";

    //定义三个roi检测物体
    static final Rect LEFT_ROI = new Rect(
            new Point(160, 120),//原x70,y200
            new Point(340, 320));//原x250，y400
    static final Rect MIDDLE_ROI = new Rect(
            new Point(580,100),//原x490，y200
            new Point(720,290));//原x680，y400
    static final Rect RIGHT_ROI = new Rect(
            new Point(1100,130),//原x960，y200
            new Point(1280,330));//原x1140，y400
    static double PERCENT_COLOR_THRESHOLD = 0.20;

    public OpenCV(Telemetry t) { telemetry = t; }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        //HSV颜色值 需要根据物体更改
        Scalar redlowHSV = new Scalar(156, 43, 46);//s越大越黑
        Scalar redhighHSV = new Scalar(180, 255, 255);//v越大越白

        Scalar bluelowHSV = new Scalar(100, 43, 46);//s越大越黑
        Scalar bluehighHSV = new Scalar(124, 255, 255);//v越大越白

        Core.inRange(mat, redlowHSV, redhighHSV, matRed);
        Core.inRange(mat, bluelowHSV, bluehighHSV, matBlue);

        Mat leftRed = matRed.submat(LEFT_ROI);
        Mat middleRed = matRed.submat(MIDDLE_ROI);
        Mat rightRed = matRed.submat(RIGHT_ROI);

        Mat leftBlue = matBlue.submat(LEFT_ROI);
        Mat middleBlue = matBlue.submat(MIDDLE_ROI);
        Mat rightBlue = matBlue.submat(RIGHT_ROI);

        double left_red_Value = Core.sumElems(leftRed).val[0] / LEFT_ROI.area() / 255;
        double middle_red_Value = Core.sumElems(middleRed).val[0] / MIDDLE_ROI.area() / 255;
        double right_red_Value = Core.sumElems(rightRed).val[0] / RIGHT_ROI .area() / 255;

        double left_blue_Value = Core.sumElems(leftBlue).val[0] / LEFT_ROI.area() / 255;
        double middle_blue_Value = Core.sumElems(middleBlue).val[0] / MIDDLE_ROI.area() / 255;
        double right_blue_Value = Core.sumElems(rightBlue).val[0] / RIGHT_ROI .area() / 255;


        leftRed.release();
        middleRed.release();
        rightRed.release();

        leftBlue.release();
        middleBlue.release();
        rightBlue.release();

        //输出roi范围内的概率

        telemetry.addData("leftRed roi raw value", (int) Core.sumElems(leftRed).val[0]);
        telemetry.addData("middleRed roi raw value", (int) Core.sumElems(middleRed).val[0]);
        telemetry.addData("rightRed roi raw value", (int) Core.sumElems(rightRed).val[0]);
        telemetry.addData("leftRed roi percentage", Math.round(left_red_Value * 100) + "%");
        telemetry.addData("middleRed roi percentage", Math.round(middle_red_Value * 100) + "%");
        telemetry.addData("rightRed roi percentage", Math.round(right_red_Value * 100) + "%");

        telemetry.addData("leftBlue roi raw value", (int) Core.sumElems(leftBlue).val[0]);
        telemetry.addData("middleBlue roi raw value", (int) Core.sumElems(middleBlue).val[0]);
        telemetry.addData("rightBlue roi raw value", (int) Core.sumElems(rightBlue).val[0]);
        telemetry.addData("leftBlue roi percentage", Math.round(left_blue_Value * 100) + "%");
        telemetry.addData("middleBlue roi percentage", Math.round(middle_blue_Value * 100) + "%");
        telemetry.addData("rightBlue roi percentage", Math.round(right_blue_Value * 100) + "%");

        boolean LeftRed = left_red_Value > PERCENT_COLOR_THRESHOLD;
        boolean MiddleRed = middle_red_Value > PERCENT_COLOR_THRESHOLD;
        boolean RightRed = right_red_Value > PERCENT_COLOR_THRESHOLD;

        boolean LeftBlue = left_blue_Value > PERCENT_COLOR_THRESHOLD;
        boolean MiddleBlue= middle_blue_Value > PERCENT_COLOR_THRESHOLD;
        boolean RightBlue = right_blue_Value > PERCENT_COLOR_THRESHOLD;


        if (LeftRed) {
            location = "LEFT";
            telemetry.addData("Location", "LEFT");
        }
        else if (MiddleRed) {
            location = "MIDDLE";
            telemetry.addData("Location", "MIDDLE");
        }
        else if (RightRed) {
            location = "RIGHT";
            telemetry.addData("Location", "RIGHT");
        }
        else if (LeftBlue){
            location = "LEFT";
            telemetry.addData("Location", "Left");
        }
        else if (MiddleBlue){
            location = "MIDDLE";
            telemetry.addData("Location", "MIDDLE");
        }
        else if (RightBlue){
            location = "RIGHT";
            telemetry.addData("Location", "Right");
        }
        telemetry.update();

        //Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar color = new Scalar(255, 0, 0);

        Imgproc.rectangle(mat, LEFT_ROI, color);
        Imgproc.rectangle(mat, MIDDLE_ROI, color);
        Imgproc.rectangle(mat, RIGHT_ROI, color);

        return mat;
    }

    public String getLocation() {
        return location;
    }
}