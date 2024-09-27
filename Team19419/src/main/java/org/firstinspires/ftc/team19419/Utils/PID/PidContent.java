package org.firstinspires.ftc.team19419.Utils.PID;

import static org.firstinspires.ftc.team19419.Params.PIDParams;
import static org.firstinspires.ftc.team19419.Params.PIDParams.kD;
import static org.firstinspires.ftc.team19419.Params.PIDParams.kI;
import static org.firstinspires.ftc.team19419.Params.PIDParams.kP;

import org.firstinspires.ftc.team19419.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.team19419.Utils.Timer;

@UtilFunctions
public class PidContent {
	public final String Tag;
	public final double vP,vI,vD,MAX_I;
	public double P,I,D;
	public double inaccuracies,lastInaccuracies,fulfillment;
	public Timer timer;
	public final int ParamID;

	public PidContent(String tag,double p,double i,double d,double max_i,int paramID){
		Tag=tag;
		vP=p;
		vI=i;
		vD=d;
		MAX_I=max_i;
		timer=new Timer();
		ParamID=paramID;
	}
	public PidContent(String tag,int paramID){
		this(tag, kP[paramID], kI[paramID], kD[paramID], PIDParams.MAX_I[paramID],paramID);
	}

	public double getFulfillment(){
		return P+I+D;
	}
	public double getKp(){
		return kP[ParamID];
	}
	public double getKd(){
		return kD[ParamID];
	}
	public double getKi(){
		return kI[ParamID];
	}

}
