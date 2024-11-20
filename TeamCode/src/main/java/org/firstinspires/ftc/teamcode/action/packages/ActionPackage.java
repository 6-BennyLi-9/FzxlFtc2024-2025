package org.firstinspires.ftc.teamcode.action.packages;

import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.action.Actions;
import org.firstinspires.ftc.teamcode.action.PriorityAction;
import org.firstinspires.ftc.teamcode.action.utils.PriorityThreadedAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 将 {@code Action} 块打包，可以代替部分使用集合、队列等数据结构维护 {@code Action} 块的方法
 */
public class ActionPackage {
	private final List<PriorityAction> actions;

	public ActionPackage(){
		actions=new ArrayList<>();
	}

	public void add(final PriorityAction action){
		actions.add(action);
	}
	public void add(final Action action){
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

		for(final PriorityAction action:actions){
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
