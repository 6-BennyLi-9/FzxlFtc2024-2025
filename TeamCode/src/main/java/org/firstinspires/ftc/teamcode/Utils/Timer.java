package org.firstinspires.ftc.teamcode.Utils;

import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

import java.util.HashMap;
import java.util.Map;

public class Timer {
    public double StartTime,EndTime;
    public final Map<String, Double> Tags;
    public final Map<String, Object> TagMeaning;
    public Timer(){
        StartTime=Functions.getCurrentTimeMills();
        Tags=new HashMap<>();
        TagMeaning=new HashMap<>();
    }
    @UserRequirementFunctions
    @ExtractedInterfaces
    public final double getCurrentTime(){
        return Functions.getCurrentTimeMills();
    }

    /**重新定义<code>StartTime</code>*/
    @UserRequirementFunctions
    public final void restart(){
        StartTime=getCurrentTime();
    }
    /**定义<code>EndTime</code>*/
    @UserRequirementFunctions
    public final void stop(){
        EndTime=getCurrentTime();
    }
    /**获取<code>EndTime-StartTime</code>*/
    @UserRequirementFunctions
    public final double getDeltaTime(){
        return EndTime-StartTime;
    }
    /**定义<code>EndTime</code>并获取<code>EndTime-StartTime</code>*/
    @UserRequirementFunctions
    public final double stopAndGetDeltaTime(){
        stop();
        return getDeltaTime();
    }
    /**定义<code>EndTime</code>并获取<code>EndTime-StartTime</code>并重新定义<code>StartTime</code>*/
    @UserRequirementFunctions
    public final double restartAndGetDeltaTime(){
        double res=stopAndGetDeltaTime();
        restart();
        return res;
    }
    /**重新定义<code>StartTime</code>并定义<code>EndTime</code>*/
    @UserRequirementFunctions
    public final void stopAndRestart(){
        stop();
        restart();
    }

    /**自动覆写如果存在相同的tag*/
    @UserRequirementFunctions
    public final void pushTimeTag(String tag){
        if(Tags.containsKey(tag)){
            Tags.replace(tag,getCurrentTime());
        }else {
	        Tags.put(tag,getCurrentTime());
        }
    }
    /**自动覆写如果存在相同的tag*/
    @UserRequirementFunctions
    public final void pushObjectionTimeTag(String tag,Object objection){
        pushTimeTag(tag);
        if(Tags.containsKey(tag)){
            TagMeaning.replace(tag,objection);
        }else {
            TagMeaning.put(tag,objection);
        }
    }

    @UserRequirementFunctions
    public double getTimeTag(String tag){
        Double v = Tags.get(tag);
        return v == null ? 0 : v;
    }
    @UserRequirementFunctions
    public Object getTimeTagObjection(String tag){
        Object v = TagMeaning.get(tag);
        return v == null ? 0 : v;
    }
}
