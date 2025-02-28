package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.utils.PriorityThreadedAction;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * 将 {@code Action} 块打包，可以代替部分使用集合、队列等数据结构维护 {@code Action} 块的方法
 */
public class ActionPackage {
	protected final Set <PriorityAction> actions;

	public ActionPackage() {
		actions = new TreeSet <>(Comparator.comparing(PriorityAction::getPriorityCode).reversed());
	}

	public void add(final PriorityAction action) {
		actions.add(action);
	}

	public void add(final Action action) {
		add(Actions.newMirroredPriority(action));
	}

	/**
	 * 单次运行 Action
	 *
	 * @see Action#activate()
	 */
	public boolean activate() {
		synchronized (actions){
			final Set <PriorityAction> remove = new HashSet <>();

			for (final PriorityAction action : actions) {
				if (! action.activate()) {
					remove.add(action);
				}
			}

			actions.removeAll(remove);
			return ! actions.isEmpty();
		}
	}

	/**
	 * 运行所有存储的 {@code Action}, 直到结束, 并清空 {@code Action} 列表
	 */
	public void activateTillEnd() {
		synchronized (actions){
			new PriorityThreadedAction(new LinkedList <>(actions)).run();
			actions.clear();
		}
	}
}
