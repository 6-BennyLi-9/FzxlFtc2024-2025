package org.firstinspires.ftc.teamcode.eventloop;

import static org.firstinspires.ftc.teamcode.HardwareParams.pushIn;
import static org.firstinspires.ftc.teamcode.HardwareParams.pushOut;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LinearEventMode;
import org.firstinspires.ftc.teamcode.RearLiftLocation;

@Disabled
@Deprecated
@Autonomous(group = Character.MAX_VALUE + "bata", preselectTeleOp = "放篮子")
public class Left extends LinearEventMode {
	@Override
	public void initialize() {
		Pose2d forward                 = new Pose2d(55, 54.5, Math.toRadians(- 135));
		Pose2d toGetFirstYellowSample  = new Pose2d(54, 47.1, Math.toRadians(- 90));
		Pose2d toPutFirstYellowSample  = new Pose2d(54, 54, Math.toRadians(- 135));
		Pose2d toGetSecondYellowSample = new Pose2d(47, 46.8, Math.toRadians(- 93)); //y49.2x64.2
		Pose2d toPutSecondYellowSample = new Pose2d(57, 52, Math.toRadians(- 135));//150
		Pose2d toGetThirdYellowSample  = new Pose2d(58.3, 46.5, Math.toRadians(- 77));//150
		Pose2d toPutThirdYellowSample  = new Pose2d(56, 53, Math.toRadians(- 135));//150
		Pose2d GoToPark                = new Pose2d(34, 4, Math.toRadians(- 180));// 40，4

		MAIN_BUILDER
				.addTemporalMarker(()->{
					utils.setRearLiftPosition(RearLiftLocation.up);
					sleep(100);
					utils.setPushPose(pushOut); //伸前电梯
				})
				.lineToLinearHeading(forward)
				.addTemporalMarker(()->{
					sleep(500);
					utils.armOperation(false);
					sleep(450);
					utils.clipOperation(true);
					sleep(200);
				})
				.lineToLinearHeading(toGetFirstYellowSample)
				.addTemporalMarker(()->{
					sleep(100);
					utils.claw_rotate_rst(true);  //前面夹子翻转下去、打开
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
					utils.armOperationL(true);

					utils.claw_rotate_rst(false);  //前面夹子翻转下去、打开
					sleep(200); //500
					utils.clawOperation(false);      //夹住
					sleep(200);
					utils.claw_rotate(true);   //翻转上去
					//sleep(300);
					utils.setPushPose(pushIn);  //前电梯收回
					sleep(250);
					utils.armOperationL(false);     //打开夹子，下降去夹
					sleep(300);
					utils.clipOperation(false);   //夹住
					sleep(200);
					utils.clawOperation(true);   //打开
					sleep(150);
				})
				.lineToLinearHeading(toPutFirstYellowSample)
				.addTemporalMarker(()->{
					utils.setRearLiftPosition(RearLiftLocation.up);  //后电梯抬

					sleep(1000);
					utils.armOperation(false); //手臂翻转,同时将前夹子放下
					sleep(450);
					utils.clipOperation(true);//打开夹子
					sleep(200);
					utils.armOperationL(true);   //翻转回来，打开夹子
					sleep(100);

					utils.setPushPose(pushOut); //frontLiftPosition(up); //伸前电梯
				})
				.lineToLinearHeading(toGetSecondYellowSample)
				.addTemporalMarker(()->{
					sleep(100);
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
					utils.claw_rotate_rst(false);    //翻转下去，打开
					sleep(250);   //500
					utils.clawOperation(false);       //夹住
					sleep(300);
					utils.claw_rotate(true);         //翻转上去
					sleep(200);

					utils.setPushPose(pushIn);
					sleep(250);
					utils.armOperationL(false);     //打开夹子，下降去夹
					sleep(300);
					utils.clipOperation(false);
					sleep(350);
					utils.clawOperation(true);
				})
				.lineToLinearHeading(toPutSecondYellowSample)
				.addTemporalMarker(()->{
					utils.setRearLiftPosition(RearLiftLocation.up);
					sleep(1200);
					utils.armOperation(false); ////手臂翻转,同时将前夹子放下
					sleep(450);
					utils.clipOperation(true);
					sleep(200);
					utils.armOperationL(true);
					sleep(100);

					utils.setPushPose(pushOut); //伸前电梯
				})
				.lineToLinearHeading(toGetThirdYellowSample)
				.addTemporalMarker(()->{
					sleep(100);
					//utils.setRearLiftPosition(RearLiftLocation.low); //回电梯
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯/

					utils.claw_rotate_rst1(false);    //翻转下去，打开
					sleep(500);
					utils.clawOperation(false);       //夹住
					sleep(300);
					utils.claw_rotate(true);         //翻转上去
					sleep(300);

					utils.setPushPose(pushIn);
					sleep(250);//前电梯回位
					utils.armOperationL(false);     //打开夹子，下降去夹
					sleep(300);//前电梯回位
					utils.clipOperation(false);
					sleep(350);
					utils.clawOperation(true);
				})
				.lineToLinearHeading(toPutThirdYellowSample)
				.addTemporalMarker(()->{
					utils.setRearLiftPosition(RearLiftLocation.up);
					sleep(1000);
					utils.armOperation(false);
					utils.rotate.setPosition(0.7);
					sleep(450);
					utils.clipOperation(true);
					sleep(200);

					service.execute(()->{
						sleep(1500);
						utils.setRearLiftPosition(RearLiftLocation.down);
					});
				})
				.lineToLinearHeading(GoToPark)
				.forward(18)
				.addTemporalMarker(()->{
					utils.claw_rotate(true);
					utils.arm.setPosition(0.67);
				});
	}

	@Override
	public Pose2d getInitialPoseEstimate() {
		return new Pose2d(36, 58, Math.toRadians(- 90));
	}
}
