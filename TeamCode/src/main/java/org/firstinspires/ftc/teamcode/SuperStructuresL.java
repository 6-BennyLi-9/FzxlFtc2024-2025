package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.HardwareParams.armDown;
import static org.firstinspires.ftc.teamcode.HardwareParams.armMiddle;
import static org.firstinspires.ftc.teamcode.HardwareParams.armUp;
import static org.firstinspires.ftc.teamcode.HardwareParams.clawOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.clawOpen;
import static org.firstinspires.ftc.teamcode.HardwareParams.clipOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.clipOpen;
import static org.firstinspires.ftc.teamcode.HardwareParams.rotateOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.turnDown;
import static org.firstinspires.ftc.teamcode.HardwareParams.turnMiddle;
import static org.firstinspires.ftc.teamcode.HardwareParams.turnUp;
import static org.firstinspires.ftc.teamcode.HardwareParams.upTurnDown;
import static org.firstinspires.ftc.teamcode.HardwareParams.upTurnUp;

import com.acmerobotics.dashboard.config.Config;

@Config
public class SuperStructuresL extends SuperStructures {
	public void showEncoder() {
		telemetry.addData("lift", leftLift.getCurrentPosition());

		telemetry.addData("touch sensor", touch.isPressed());

		telemetry.addData("push", rightPush.getPosition());
		telemetry.addData("clip", clip.getPosition());
		telemetry.addData("turn", turn.getPosition());
		telemetry.addData("claw", claw.getPosition());
		telemetry.addData("rotate", rotate.getPosition());
		telemetry.addData("right push", rightPush.getPosition());
	}

	public void optionThroughGamePad() {
		if (gamepad2.right_bumper) {
			arm.setPosition(0.88);
			turn.setPosition(turnUp);
			if (! lift_up_event) {
				if (right_encoder_value == 1620) {
					setLiftPosition(2300);//中间位置
				} else {
					setLiftPosition(1620);//中间位置
				}
				lift_up_event = true;
			}
		} else {
			lift_up_event = false;
		}
		if (gamepad2.left_bumper) {
			//clipOperation1(true);
			inlineArmOperation(false);
			arm.setPosition(0.88);
			setLiftPosition(172);//初始位置
		}
		if (gamepad2.dpad_right) {
			inlineClawOpenOperation();
			setLiftPosition(2300);//挂高杆2195
			inlineArmOperation(true);
		}
		if (gamepad2.dpad_down) {
			inlineClipOpenOperation();
			inlineArmOperation(false);
			setLiftPosition(0);//初始位置
		}


		setPushPose(rightPush.getPosition() + gamepad2.left_stick_y * 0.035);

		liftPositionUpdate();
		rotate.setPosition(rotate.getPosition() + 0.03 * (gamepad2.left_trigger - gamepad2.right_trigger));

		inlineClipOperation(gamepad2.a);
		clawOperation(gamepad2.b);
		armOperation(gamepad2.x);
	}

	protected void armOperation(boolean key) {
		telemetry.addData("arm", "%d", armPutEvent);
		telemetry.addData("armPosition", "%f", arm.getPosition());
		if (key) {
			if (! keyFlag_arm) {
				keyFlag_arm = true;
				if (armPutEvent < 1) { //原来值是3
					armPutEvent++;
				} else {
					armPutEvent = 0;
				}
			}
			switch (armPutEvent) {
				case 0:
					clip.setPosition(clipOn);
					break;
				case 1:
					arm.setPosition(armDown);  //打开
					clip.setPosition(clipOpen);
					break;
			}
		} else {
			keyFlag_arm = false;
		}
	}

	protected void clawOperation(boolean key) {
		telemetry.addData("claw", "%d", clawPutEvent);
		if (key) {
			if (! keyFlag_claw) {
				keyFlag_claw = true;
				if (clawPutEvent < 3) { //原来值是3
					clawPutEvent++;
				} else {
					clawPutEvent = 0;
				}
			}
			switch (clawPutEvent) {
				case 0:
					claw.setPosition(clawOn);  //扣住
					turn.setPosition(turnUp);  //翻转上去
					rotate.setPosition(rotateOn); //保持水平0.63，0
					break;
				case 1:
					claw.setPosition(clawOpen);  //打开
					turn.setPosition(turnMiddle);  //翻转下去
					rotate.setPosition(rotateOn); //保持水平0.1,0.83
					break;
				case 2:
					claw.setPosition(clawOpen);  //打开
					turn.setPosition(turnDown);  //翻转下去
					//rotate.setPosition(rotateOn); //保持水平0.1,0.83
					break;
				case 3:
					claw.setPosition(clawOn);  //夹住
					turn.setPosition(turnDown);  //翻转下去

					break;

			}
		} else {
			keyFlag_claw = false;
		}
	}

	protected void inlineArmOperation(boolean y) {
		if (y) {
			clip.setPosition(clipOn);  //夹住
			arm.setPosition(armUp);  //翻转挂矿石
			upTurn.setPosition(upTurnUp);
			claw.setPosition(clawOpen);
		} else {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armMiddle);  //中间等待位置
			upTurn.setPosition(upTurnDown);
		}
	}

	protected void inlineClawOpenOperation() {
		claw.setPosition(clawOpen);   //打开
		turn.setPosition(turnMiddle);  //翻转下去
		rotate.setPosition(rotateOn); //保持水平0.1,0.83
	}
}

