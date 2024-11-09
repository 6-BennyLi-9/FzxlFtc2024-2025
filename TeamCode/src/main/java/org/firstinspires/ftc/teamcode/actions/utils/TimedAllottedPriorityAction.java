package org.firstinspires.ftc.teamcode.actions.utils;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.PriorityAction;

import java.util.*;

/**
 * 根据 {@code PriorityAction} 的优先级排序后进行执行操作，如果超时后将强制退出执行链
 */
public class TimedAllottedPriorityAction implements Action {
	public final List<PriorityAction> actions;
	private final long allottedMilliseconds;
	private boolean initialized=false;

	public TimedAllottedPriorityAction(final long allottedMilliseconds,final List<PriorityAction> actions){
		this.actions=new ArrayList<>();
		this.actions.addAll(actions);
		this.actions.sort(Comparator.comparingLong(x -> -x.getPriorityCode()));
		this.allottedMilliseconds=allottedMilliseconds;
	}
	public TimedAllottedPriorityAction(final long allottedMilliseconds,final PriorityAction... actions){
		this(allottedMilliseconds,Arrays.asList(actions));
	}

	private double startTime;

	@Override
	public boolean run() {
		if(!initialized){
			startTime=System.nanoTime()/1e6;
			initialized=true;
		}
		final Set<PriorityAction> removes=new HashSet<>();

		for(final PriorityAction action:actions){
			if(!action.run()){
				removes.add(action);
			}
			if(System.nanoTime()/1e6-startTime>=allottedMilliseconds){
				break;
			}
		}

		actions.removeAll(removes);
		return !actions.isEmpty();
	}
}
