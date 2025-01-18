package org.betastudio.ftc;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** @noinspection deprecation*/
public class MultiMotor implements DcMotorEx {
	private final Set<DcMotorEx> motors;

	public MultiMotor(DcMotorEx... motors){
		this.motors=new HashSet <>(Arrays.asList(motors));
	}

	public void addMotor(DcMotorEx motor){
		motors.add(motor);
	}

	public void removeMotor(DcMotorEx motor){
		motors.remove(motor);
	}

	@Override
	public void setMotorEnable() {
		motors.forEach(DcMotorEx::setMotorEnable);
	}

	@Override
	public void setMotorDisable() {
		motors.forEach(DcMotorEx::setMotorDisable);
	}

	@Override
	public boolean isMotorEnabled() {
		return motors.stream().iterator().next().isMotorEnabled();
	}

	@Override
	public void setVelocity(double angularRate) {
		motors.forEach(m -> m.setVelocity(angularRate));
	}

	@Override
	public void setVelocity(double angularRate, AngleUnit unit) {
		motors.forEach(m -> m.setVelocity(angularRate, unit));
	}

	@Override
	public double getVelocity() {
		return motors.stream().iterator().next().getVelocity();
	}

	@Override
	public double getVelocity(AngleUnit unit) {
		return motors.stream().iterator().next().getVelocity(unit);
	}

	@Override
	public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
		motors.forEach(m -> m.setPIDCoefficients(mode, pidCoefficients));
	}

	@Override
	public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
		motors.forEach(m -> m.setPIDFCoefficients(mode, pidfCoefficients));
	}

	@Override
	public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
		motors.forEach(m -> m.setVelocityPIDFCoefficients(p, i, d, f));
	}

	@Override
	public void setPositionPIDFCoefficients(double p) {
		motors.forEach(m -> m.setPositionPIDFCoefficients(p));
	}

	@Override
	public PIDCoefficients getPIDCoefficients(RunMode mode) {
		return motors.stream().iterator().next().getPIDCoefficients(mode);
	}

	@Override
	public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
		return motors.stream().iterator().next().getPIDFCoefficients(mode);
	}

	@Override
	public void setTargetPositionTolerance(int tolerance) {
		motors.forEach(m -> m.setTargetPositionTolerance(tolerance));
	}

	@Override
	public int getTargetPositionTolerance() {
		return motors.stream().iterator().next().getTargetPositionTolerance();
	}

	@Override
	public double getCurrent(CurrentUnit unit) {
		return motors.stream().iterator().next().getCurrent(unit);
	}

	@Override
	public double getCurrentAlert(CurrentUnit unit) {
		return motors.stream().iterator().next().getCurrentAlert(unit);
	}

	@Override
	public void setCurrentAlert(double current, CurrentUnit unit) {
		motors.forEach(m -> m.setCurrentAlert(current, unit));
	}

	@Override
	public boolean isOverCurrent() {
		return motors.stream().iterator().next().isOverCurrent();
	}

	@Override
	public MotorConfigurationType getMotorType() {
		return motors.stream().iterator().next().getMotorType();
	}

	@Override
	public void setMotorType(MotorConfigurationType motorType) {
		motors.forEach(m -> m.setMotorType(motorType));
	}

	@Override
	public DcMotorController getController() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPortNumber() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
		motors.forEach(m -> m.setZeroPowerBehavior(zeroPowerBehavior));
	}

	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() {
		return motors.stream().iterator().next().getZeroPowerBehavior();
	}

	@Override
	public void setPowerFloat() {
		motors.forEach(DcMotor::setPowerFloat);
	}

	@Override
	public boolean getPowerFloat() {
		return motors.stream().iterator().next().getPowerFloat();
	}

	@Override
	public void setTargetPosition(int position) {
		motors.forEach(m -> m.setTargetPosition(position));
	}

	@Override
	public int getTargetPosition() {
		return motors.stream().iterator().next().getTargetPosition();
	}

	@Override
	public boolean isBusy() {
		return motors.stream().iterator().next().isBusy();
	}

	@Override
	public int getCurrentPosition() {
		return motors.stream().iterator().next().getCurrentPosition();
	}

	@Override
	public void setMode(RunMode mode) {
		motors.forEach(m -> m.setMode(mode));
	}

	@Override
	public RunMode getMode() {
		return motors.stream().iterator().next().getMode();
	}

	@Override
	public void setDirection(Direction direction) {
		motors.forEach(m -> m.setDirection(direction));
	}

	@Override
	public Direction getDirection() {
		return motors.stream().iterator().next().getDirection();
	}

	@Override
	public void setPower(double power) {
		motors.forEach(m -> m.setPower(power));
	}

	@Override
	public double getPower() {
		return motors.stream().iterator().next().getPower();
	}

	@Override
	public Manufacturer getManufacturer() {
		return motors.stream().iterator().next().getManufacturer();
	}

	@Override
	public String getDeviceName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getConnectionInfo() {
		StringBuilder str=new StringBuilder();
		motors.forEach(m -> str.append(m.getConnectionInfo()));
		return str.toString();
	}

	@Override
	public int getVersion() {
		return motors.stream().iterator().next().getVersion();
	}

	@Override
	public void resetDeviceConfigurationForOpMode() {
		motors.forEach(DcMotorEx::resetDeviceConfigurationForOpMode);
	}

	@Override
	public void close() {
		motors.forEach(DcMotorEx::close);
	}
}
