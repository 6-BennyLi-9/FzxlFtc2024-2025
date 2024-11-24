package org.firstinspires.ftc.teamcode.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * 一个非常好用的计时器
 */
@SuppressWarnings("unused")
public final class Timer {
    public double StartTime,EndTime;
    public final Map<String, Double> Tags;
    public final Map<String, Object> TagMeaning;
    public final Map<String, Vector<Double>> MileageTags;

    public Timer(){
        StartTime=getCurrentTime();
        Tags=new HashMap<>();
        TagMeaning=new HashMap<>();
        MileageTags=new HashMap<>();
    }
    public static double getCurrentTime(){
        return System.nanoTime()/ 1.0e6;
    }

    /**重新定义{@code StartTime}*/
    public void restart(){
        StartTime=getCurrentTime();
    }
    /**定义{@code EndTime}*/
    public void stop(){
        EndTime=getCurrentTime();
    }
    /**获取{@code EndTime-StartTime}*/
    public double getDeltaTime(){
        return EndTime-StartTime;
    }
    /**定义{@code EndTime}并获取{@code EndTime-StartTime}*/
    public double stopAndGetDeltaTime(){
        stop();
        return getDeltaTime();
    }
    /**定义{@code EndTime}并获取{@code EndTime-StartTime}并重新定义{@code StartTime}*/
    public double restartAndGetDeltaTime(){
        final double res=stopAndGetDeltaTime();
        restart();
        return res;
    }
    /**重新定义{@code StartTime}并定义{@code EndTime}*/
    public void stopAndRestart(){
        stop();
        restart();
    }

    /**自动覆写如果存在相同的tag*/
    public void pushTimeTag(final String tag){
        if(Tags.containsKey(tag)){
            Tags.replace(tag,getCurrentTime());
        }else {
	        Tags.put(tag,getCurrentTime());
        }
    }
    /**自动覆写如果存在相同的tag*/
    public void pushObjectionTimeTag(final String tag, final Object objection){
        pushTimeTag(tag);
        if(Tags.containsKey(tag)){
            TagMeaning.replace(tag,objection);
        }else {
            TagMeaning.put(tag,objection);
        }
    }

    /**
     * @return 未申明時返回0
     */
    public double getTimeTag(final String tag){
        final Double v = Tags.get(tag);
        return null == v ? 0 : v;
    }
    /**
     * @return 未申明時返回0
     */
    public Object getTimeTagObjection(final String tag){
        final Object v = TagMeaning.get(tag);
        return null == v ? 0 : v;
    }

    public void pushMileageTimeTag(final String tag){
        pushTimeTag(tag);
        if (MileageTags.containsKey(tag)){
            Objects.requireNonNull(MileageTags.get(tag)).add(getCurrentTime());
        }else{
            final Vector<Double> cache=new Vector<>();
            cache.add(getCurrentTime());
            MileageTags.put(tag,cache);
        }
    }
    public Vector<Double> getMileageTimeTag(final String tag){
        return MileageTags.get(tag);
    }
}
