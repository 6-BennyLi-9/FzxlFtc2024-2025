package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.HardwareParams.armGet;
import static org.firstinspires.ftc.teamcode.HardwareParams.armPut;
import static org.firstinspires.ftc.teamcode.HardwareParams.clawOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.clawOpen;
import static org.firstinspires.ftc.teamcode.HardwareParams.clipOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.clipOpen;
import static org.firstinspires.ftc.teamcode.HardwareParams.rotateOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.turnDown;
import static org.firstinspires.ftc.teamcode.HardwareParams.turnMiddle;
import static org.firstinspires.ftc.teamcode.HardwareParams.turnUp;
import static org.firstinspires.ftc.teamcode.HardwareParams.upTurnGet;
import static org.firstinspires.ftc.teamcode.HardwareParams.upTurnPut;

import com.acmerobotics.dashboard.config.Config;

@Config
public class SuperStructuresR extends SuperStructures{
	@Override
	public void optionThroughGamePad() {
		if (gamepad2.right_bumper) {
			arm.setPosition(0.88);

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
			setLiftPosition(875);   //挂样本883，910, 852, 849, 872
			inlineArmOperation(true);
		}
		if (gamepad2.dpad_left) {
			//clipOperation1(true);
			inlineArmOperation(false);
			arm.setPosition(0.88);
			setLiftPosition(172);//初始位置
		}
		if (gamepad2.dpad_down) {
			//clipOperation1(true);
			inlineArmOperation(false);
			setLiftPosition(0);//初始位置
		}

		setPushPose(rightPush.getPosition() + gamepad2.left_stick_y * 0.035);

		liftPositionUpdate();
		rotate.setPosition(rotate.getPosition() + 0.03 * (gamepad2.left_trigger - gamepad2.right_trigger));

		clipOperation(gamepad2.a);
		clawOperation(gamepad2.b);
		armOperation(gamepad2.x);
	}

	@Override
	protected void armOperation(boolean key) {
		telemetry.addData("arm", "%d", armPutEvent);
		telemetry.addData("armPosition", "%f", arm.getPosition());
		if (key) {
			if (! keyFlag_arm) {
				keyFlag_arm = true;
				if (armPutEvent < 1) {//3
					armPutEvent++;
				} else {
					armPutEvent = 0;
				}
			}
			switch (armPutEvent) {

				case 0:
					//夹住
					arm.setPosition(armGet);  //翻转挂矿石
					upTurn.setPosition(upTurnGet);
					break;
				case 1:
					//夹住
					//arm.setPosition(armPut);  //翻转挂矿石
					upTurn.setPosition(upTurnGet);
					clip.setPosition(clipOpen);
					break;

			}
		} else {
			keyFlag_arm = false;
		}
	}

	@Override
	protected void clawOperation(boolean key) {
		telemetry.addData("claw", "%d", clawPutEvent);
		if (key) {
			if (! keyFlag_claw) {
				keyFlag_claw = true;
				if (clawPutEvent < 3) {
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
					rotate.setPosition(rotateOn); //保持水平0.1,0.83
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
	protected void clipOperation(boolean key) {
		telemetry.addData("clip", "%d", clipPutEvent);
		if (key) {
			if (! keyFlag_clip) {
				keyFlag_clip = true;
				if (clipPutEvent < 1) {
					clipPutEvent++;
				} else {
					clipPutEvent = 0;
				}
			}
			switch (clipPutEvent) {
				case 0:
					clip.setPosition(clipOpen);  //释放
					upTurn.setPosition(upTurnGet);
					break;
				case 1:
					//夹住
					clip.setPosition(clipOn);
					break;
			}
		} else {
			keyFlag_clip = false;
		}
	}

	@Override
	protected void inlineArmOperation(boolean y) {
		if (y) {
			clip.setPosition(clipOn);  //夹住
			arm.setPosition(armPut);  //翻转挂矿石
			upTurn.setPosition(upTurnPut);
			claw.setPosition(clawOpen);
		} else {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armGet);  //中间等待位置
			upTurn.setPosition(upTurnGet);
		}
	}


	@Override
	protected void inlineClawOpenOperation() {
		claw.setPosition(clawOpen);   //打开
		turn.setPosition(turnUp);  //翻转下去
		rotate.setPosition(rotateOn); //保持水平0.1,0.83
	}
}

