package org.firstinspires.ftc.teamcode.actions.packages;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.Actions;
import org.firstinspires.ftc.teamcode.actions.PriorityAction;
import org.firstinspires.ftc.teamcode.actions.utils.PriorityThreadedAction;

import java.util.*;

public class ActionPackage {
	private final List<PriorityAction> actions;

	public ActionPackage(){
		actions=new ArrayList<>();
	}

	public void add(PriorityAction action){
		actions.add(action);
	}
	public void add(Action action){
		add(Actions.asPriority(action));
	}

	private void sort(){
		actions.sort(Comparator.comparing(x->-x.getPriorityCode()));
	}

	/**
	 * 单次运行 Action
	 * @see Action#run()
	 */
	public boolean run(){
		sort();
		final Set<PriorityAction> remove=new HashSet<>();

		for(PriorityAction action:actions){
			if(!action.run()){
				remove.add(action);
			}
		}

		actions.removeAll(remove);
		return !actions.isEmpty();
	}

	/**
	 * 运行所有存储的 {@code Action}, 直到结束, 并清空 {@code Action} 列表
	 */
	public void runTillEnd(){
		Actions.runAction(new PriorityThreadedAction(actions));
		actions.clear();
	}
}
