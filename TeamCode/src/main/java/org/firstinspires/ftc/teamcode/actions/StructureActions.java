package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;

import org.firstinspires.ftc.teamcode.hardwares.Structure;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

public class StructureActions {
	public Structure controller;

	public StructureActions(final Structure controller){
		this.controller=controller;
	}

	protected class OpenFrontClip implements Action {
		@Override
		public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
			StructureActions.this.controller.openFrontClip();
			StructureActions.this.controller.servos.update();
			return StructureActions.this.controller.servos.inPlace();
		}
	}
	protected class CloseFrontClip implements Action {
		@Override
		public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
			StructureActions.this.controller.closeFrontClip();
			StructureActions.this.controller.servos.update();
			return StructureActions.this.controller.servos.inPlace();
		}
	}
	protected class OpenRearClip implements Action {
		@Override
		public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
			StructureActions.this.controller.openRearClip();
			StructureActions.this.controller.servos.update();
			return StructureActions.this.controller.servos.inPlace();
		}
	}
	protected class CloseRearClip implements Action {
		@Override
		public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
			StructureActions.this.controller.closeRearClip();
			StructureActions.this.controller.servos.update();
			return StructureActions.this.controller.servos.inPlace();
		}
	}

	protected static class ServoToPlace implements Action{
		public IntegrationServo servo;
		public final double pose,time;
		public ServoToPlace(@NonNull final IntegrationServo servo, final double pose, final double time){
			this.servo=servo;
			this.pose=pose;
			this.time=time;
			servo.setTargetPoseInTime(pose,time);
		}

		@Override
		public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
			this.servo.update();
			return this.servo.smoothMode;
		}
	}

	@UserRequirementFunctions
	public Action openFrontClip(){return new OpenFrontClip();}
	@UserRequirementFunctions
	public Action openRearClip(){return new OpenRearClip();}
	@UserRequirementFunctions
	public Action closeFrontClip(){return new CloseFrontClip();}
	@UserRequirementFunctions
	public Action closeRearClip(){return new CloseRearClip();}


	@UserRequirementFunctions
	public Action openClips(){return new ParallelAction(this.openFrontClip(), this.openRearClip());}
	@UserRequirementFunctions
	public Action closeClips(){return new ParallelAction(this.closeFrontClip(), this.closeRearClip());}

	@UserRequirementFunctions
	public Action servoToPlace(@NonNull final IntegrationServo servo, final double pose, final double time){return new ServoToPlace(servo, pose, time);}
}
