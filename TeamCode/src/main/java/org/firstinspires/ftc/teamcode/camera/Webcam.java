package org.firstinspires.ftc.teamcode.camera;

import org.betastudio.ftc.client.Client;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class Webcam extends OpenCvPipeline {
	protected boolean debugMode = true;
	protected Client  telemetry;
	Mat mat     = new Mat();
	Mat matRed  = new Mat();
	Mat matBlue = new Mat();

	public String location = "";

	//定义三个roi检测物体 1280*720
	static final Rect   LEFT_ROI                = new Rect(new Point(100, 305),//原x=110,y=305
			new Point(310, 560));//原x=300，y=560
	static final Rect   MIDDLE_ROI              = new Rect(new Point(560, 280),//原x=610，y=280
			new Point(840, 490));//原x=690，y=490
	static final Rect   RIGHT_ROI               = new Rect(new Point(1050, 295),//原x=1050，y=295
			new Point(1240, 570));//原x=1240，y=570
	static       double PERCENT_COLOR_THRESHOLD = 0.20;

	@Override
	public Mat processFrame(final Mat input) {
		Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

		//HSV颜色值 需要根据物体更改
		final Scalar redLowHSV  = new Scalar(156, 43, 46);//s越大越黑
		final Scalar redHighHSV = new Scalar(180, 255, 255);//v越大越白

		final Scalar blueLowHSV  = new Scalar(100, 43, 46);//s越大越黑
		final Scalar blueHighHSV = new Scalar(124, 255, 255);//v越大越白

		Core.inRange(mat, redLowHSV, redHighHSV, matRed);
		Core.inRange(mat, blueLowHSV, blueHighHSV, matBlue);

		final Mat leftRed   = matRed.submat(LEFT_ROI);
		final Mat middleRed = matRed.submat(MIDDLE_ROI);
		final Mat rightRed  = matRed.submat(RIGHT_ROI);

		final Mat leftBlue   = matBlue.submat(LEFT_ROI);
		final Mat middleBlue = matBlue.submat(MIDDLE_ROI);
		final Mat rightBlue  = matBlue.submat(RIGHT_ROI);

		final double left_red_Value   = Core.sumElems(leftRed).val[0] / LEFT_ROI.area() / 255;
		final double middle_red_Value = Core.sumElems(middleRed).val[0] / MIDDLE_ROI.area() / 255;
		final double right_red_Value  = Core.sumElems(rightRed).val[0] / RIGHT_ROI.area() / 255;

		final double left_blue_Value   = Core.sumElems(leftBlue).val[0] / LEFT_ROI.area() / 255;
		final double middle_blue_Value = Core.sumElems(middleBlue).val[0] / MIDDLE_ROI.area() / 255;
		final double right_blue_Value  = Core.sumElems(rightBlue).val[0] / RIGHT_ROI.area() / 255;


		leftRed.release();
		middleRed.release();
		rightRed.release();

		leftBlue.release();
		middleBlue.release();
		rightBlue.release();

		//输出roi范围内的概率

		if (debugMode) {
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
		}
		final boolean LeftRed   = left_red_Value > PERCENT_COLOR_THRESHOLD;
		final boolean MiddleRed = middle_red_Value > PERCENT_COLOR_THRESHOLD;
		final boolean RightRed  = right_red_Value > PERCENT_COLOR_THRESHOLD;

		final boolean LeftBlue   = left_blue_Value > PERCENT_COLOR_THRESHOLD;
		final boolean MiddleBlue = middle_blue_Value > PERCENT_COLOR_THRESHOLD;
		final boolean RightBlue  = right_blue_Value > PERCENT_COLOR_THRESHOLD;


		if (LeftRed) {
			location = "LEFT";
			telemetry.addData("Location", "LEFT");
		} else if (MiddleRed) {
			location = "MIDDLE";
			telemetry.addData("Location", "MIDDLE");
		} else if (RightRed) {
			location = "RIGHT";
			telemetry.addData("Location", "RIGHT");
		} else if (LeftBlue) {
			location = "LEFT";
			telemetry.addData("Location", "LEFT");
		} else if (MiddleBlue) {
			location = "MIDDLE";
			telemetry.addData("Location", "MIDDLE");
		} else if (RightBlue) {
			location = "RIGHT";
			telemetry.addData("Location", "RIGHT");
		}
		telemetry.update();

		//Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

		final Scalar color = new Scalar(255, 0, 0);
		Imgproc.rectangle(mat, LEFT_ROI, color);
		Imgproc.rectangle(mat, MIDDLE_ROI, color);
		Imgproc.rectangle(mat, RIGHT_ROI, color);
		return mat;
	}

	public String getLocation() {
		return location;
	}

}
