package org.firstinspires.ftc.teamcode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveControls.MecanumDrive;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrderBuilder;
import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriverProgram;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Sensors;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.Servos;
import org.firstinspires.ftc.teamcode.Hardwares.Chassis;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationHardwareMap;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.Hardwares.Structure;
import org.firstinspires.ftc.teamcode.Hardwares.Webcam;
import org.firstinspires.ftc.teamcode.Utils.ActionBox;
import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Hardwares.Basic.ClipPosition;
import org.firstinspires.ftc.teamcode.Utils.Enums.RunningMode;
import org.firstinspires.ftc.teamcode.Utils.Enums.RobotState;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.UnKnownErrorsException;
import org.firstinspires.ftc.teamcode.Utils.PID.PidProcessor;
import org.firstinspires.ftc.teamcode.Utils.Timer;

public class Robot {
	public IntegrationHardwareMap lazyIntegratedDevices;

	public final Motors motors;
	public final Sensors sensors;
	public final Servos servos;

	public Chassis chassis;
	public Structure structure;
	public Webcam webcam;

	public Client client;
	public PidProcessor pidProcessor;

	public RobotState robotState;
	public RunningMode runningState;
	public IntegrationGamepad gamepad=null;
	public final ActionBox actionBox;
	private DriverProgram drive=null;

	public Timer timer;

	public ParamsController paramsController =new VoidParamsController();
	public KeyMapController keyMapController =new VoidKeyMapController();

	public Robot(@NonNull HardwareMap hardwareMap, @NonNull RunningMode state, @NonNull Telemetry telemetry){
		this(hardwareMap,state,new Client(telemetry));
	}
	public Robot(@NonNull HardwareMap hardwareMap, @NonNull RunningMode state, @NonNull Client client){
		lazyIntegratedDevices=new IntegrationHardwareMap(hardwareMap,pidProcessor);

		motors=new Motors(lazyIntegratedDevices);
		sensors=new Sensors(lazyIntegratedDevices);
		servos=new Servos(lazyIntegratedDevices);

		chassis =new Chassis(motors,sensors);
		structure=new Structure(motors,servos);
		webcam=new Webcam(hardwareMap);

		this.client=client;
		pidProcessor=new PidProcessor();


		//TODO:如果需要，在这里修改 Params.Config 中的值
		switch (state) {
			case Autonomous:
				InitInAutonomous();
				break;
			case ManualDrive:
				Params.Configs.runUpdateWhenAnyNewOptionsAdded=true;
				Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower=false;

				InitInManualDrive();
				break;
			case Debug:case Sample:case TestOrTune:
				break;
			default:
				throw new UnKnownErrorsException("Unexpected runningState value:"+state.name());
		}

		runningState = state;
		actionBox = new ActionBox();
		timer=new Timer();
		client.addData("RobotState","UnKnow");
	}

	@UserRequirementFunctions
	public void setParamsOverride(ParamsController controller){
		this.paramsController =controller;
		this.paramsController.PramsOverride();
	}
	@UserRequirementFunctions
	public void setKeyMapController(KeyMapController controller){
		keyMapController=controller;
		keyMapController.KeyMapOverride(gamepad.keyMap);
	}

	/**
	 * 自动初始化SimpleMecanumDrive
	 * @return 返回定义好的SimpleMecanumDrive
	 */
	public DriverProgram InitMecanumDrive(Pose2d RobotPosition){
		drive=new SimpleMecanumDrive(this,RobotPosition);
		if(runningState != RunningMode.Autonomous) {
			Log.w("Robot.java","Initialized Driving Program in Manual Driving RobotState.");
		}
		return drive;
	}

	private void InitInAutonomous(){
		structure.clipOption(ClipPosition.Close);
		robotState = RobotState.IDLE;
		SetGlobalBufPower(0.9f);
	}
	private void InitInManualDrive(){
		structure.clipOption(ClipPosition.Open);
		robotState = RobotState.ManualDriving;
		SetGlobalBufPower(0.9f);
	}

	public void registerGamepad(Gamepad gamepad1,Gamepad gamepad2){
		gamepad=new IntegrationGamepad(gamepad1,gamepad2);
	}

	public void update()  {
		if(timer.stopAndGetDeltaTime()>=90000){
			robotState = RobotState.FinalState;
		}

		sensors.update();
		servos.update();

		if(Params.Configs.driverUsingAxisPowerInsteadOfCurrentPower) {
			motors.update(sensors.robotAngle());
		}else{
			motors.update();
		}

		Actions.runBlocking(actionBox.output());
		client.changeData("RobotState", robotState.name());
		while(Params.Configs.waitForServoUntilThePositionIsInPlace && servos.inPlace()){
			servos.update();
			//当前最方便的Sleep方案
			Actions.runBlocking(new SleepAction(0.1));
		}
	}

	/**
	 * 不会自动 update()
	 */
	@UserRequirementFunctions
	@ExtractedInterfaces
	public void operateThroughGamePad() {
		chassis.operateThroughGamePad(gamepad);
		structure.operateThroughGamePad(gamepad);
	}
	public DriveOrderBuilder DrivingOrderBuilder(){
		if(drive instanceof SimpleMecanumDrive)
			return ((SimpleMecanumDrive) drive).drivingCommandsBuilder();
		else if(drive instanceof MecanumDrive)
			return ((MecanumDrive) drive).drivingCommandsBuilder();
		return null;
	}

	/**
	 * 在该节点让机器旋转指定角度
	 * @param angle 要转的角度[-180,180)
	 */
	public void turnAngle(double angle){
		if(runningState == RunningMode.ManualDrive)return;
		drive.runOrderPackage(DrivingOrderBuilder().TurnAngle(angle).END());
	}
	public void strafeTo(Vector2d pose){
		if(runningState == RunningMode.ManualDrive)return;
		drive.runOrderPackage(DrivingOrderBuilder().StrafeTo(pose).END());
	}

	/**
	 * 会自动update()
	 */
	public Pose2d pose(){
		drive.update();
		return drive.getCurrentPose();
	}

	/**
	 * 将会把BufPower全部分配给电机
	 * @param BufPower 提供的电机力度因数
	 */
	public void SetGlobalBufPower(double BufPower){
		if(drive!=null) {
			drive.runOrderPackage(DrivingOrderBuilder().SetPower(BufPower).END());//考虑是否删去此代码片段
		}
		motors.setBufPower(BufPower);
	}

	@ExtractedInterfaces@UserRequirementFunctions
	public void addData(String key, String val){client.addData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addData(String key,Object val){client.addData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void deleteDate(String key){try{client.deleteData(key);}catch (Exception ignored){}}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeData(String key, String val){client.changeData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeData(String key,Object val){client.changeData(key, val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addLine(String val){client.addLine(val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void addLine(Object val){client.addLine(val);}
	@ExtractedInterfaces@UserRequirementFunctions
	public void changeLine(@NonNull Object key, @NonNull Object val){client.changeLine(key.toString(),val.toString());}
	@ExtractedInterfaces@UserRequirementFunctions
	public void deleteLine(String key){try{client.deleteLine(key);}catch (Exception ignored){}}
}
