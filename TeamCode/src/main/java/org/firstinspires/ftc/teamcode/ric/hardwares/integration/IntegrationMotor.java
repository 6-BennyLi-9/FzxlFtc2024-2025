package org.firstinspires.ftc.teamcode.ric.hardwares.integration;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.Beta;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.ric.utils.Functions;
import org.firstinspires.ftc.teamcode.ric.utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.ric.utils.PID.PidProcessor;

public class IntegrationMotor extends IntegrationDevice{
	private boolean PID_ENABLED =true;

	public final DcMotorEx motor;
	private final PidProcessor pidProcessor;
	private double power=0,lastPower;
	private final IntegrationHardwareMap lazyIntegrationHardwareMap;
	public double minPowerToOvercomeKineticFriction=0;
	public double minPowerToOvercomeStaticFriction=0;

	public IntegrationMotor(@NonNull DcMotorEx motor, @NonNull HardwareDeviceTypes deviceType, PidProcessor pidProcessor,
	                        @NonNull IntegrationHardwareMap integrationHardwareMap){
		super(deviceType.deviceName);
		this.motor= motor;
		this.pidProcessor=pidProcessor;
		lazyIntegrationHardwareMap = integrationHardwareMap;
	}

	@UserRequirementFunctions
	public void initPID(int ParamID){
		if(PID_ENABLED){
			pidTag=this.getClass().getName()+"-"+motor.getDeviceName();
			pidProcessor.loadContent(new PidContent(pidTag,ParamID));
		}
	}

	public void setPower(double power){
		if(lastPower==0){
			timer.pushTimeTag("LastZeroTime");
		}
		power= Functions.intervalClip(power,-1,1);

		this.power = power;
		updated=false;

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			update();
		}
	}

	@Beta
	public void setTargetPowerSmooth(double power) {
		double k = 0.7;

		if (lastPower == 0){
			timer.pushTimeTag("LastZeroTime");
		}
		if (power == 0) {
			this.power = 0;
			return;
		}
		power = Functions.intervalClip(power, -1, 1);
		double m = (System.currentTimeMillis() > Params.switchFromStaticToKinetic + timer.getTimeTag("LastZeroTime") ?
				   minPowerToOvercomeKineticFriction : minPowerToOvercomeStaticFriction)
				* (13.5/ lazyIntegrationHardwareMap.getVoltage());
		power *= 1-m;
		power = power + m * Math.signum(power);
		// 0.5
		this.power = power* k + this.lastPower*(1- k);
	}

	@Override
	public void update() {
		if(updated)return;
		updated=true;
		if(PID_ENABLED&&pidProcessor!=null){
			pidProcessor.registerInaccuracies(pidTag,power-motor.getPower());
			pidProcessor.ModifyPidByTag(pidTag);
			motor.setPower(motor.getPower()+pidProcessor.getFulfillment(pidTag));
		}else{
			motor.setPower(power);
			PID_ENABLED=false;
		}
		timer.pushTimeTag("LastUpdateTime");
		lastPower=power;

		if(!PID_ENABLED&&pidProcessor!=null) {
			PID_ENABLED=true;
		}
	}

	@Override
	public double getPosition() {
		return motor.getCurrentPosition();
	}

	@Override
	public double getPower() {
		return motor.getPower();
	}

	@UserRequirementFunctions
	public void ConfigPidEnable(boolean val) {
		PID_ENABLED = val;
	}
}
