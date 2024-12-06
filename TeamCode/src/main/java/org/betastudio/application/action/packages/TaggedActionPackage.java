package org.betastudio.application.action.packages;

import org.betastudio.application.action.Action;
import org.betastudio.application.action.Actions;
import org.betastudio.application.action.PriorityAction;
import org.betastudio.application.action.utils.PriorityThreadedAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 将 {@code Action} 块进一步打包，可以通过数据标签（类型为 {@code String} ）自动处理、替换 {@code Action} 块
 */
public class TaggedActionPackage extends ActionPackage {
	private final Map <String, PriorityAction> priorityActionMap;

	public TaggedActionPackage() {
		priorityActionMap = new HashMap <>();
	}

	/**
	 * @throws UnsupportedOperationException 必须提供Action的标签
	 */
	@Override
	@Deprecated
	public void add(final Action action) {
		throw new UnsupportedOperationException("Must Given A Tag For Using This Method");
	}

	/**
	 * @throws UnsupportedOperationException 必须提供Action的标签
	 */
	@Override
	@Deprecated
	public void add(final PriorityAction action) {
		throw new UnsupportedOperationException("Must Given A Tag For Using This Method");
	}

	/**
	 * @param tag    标签
	 * @param action 要加入的 {@code  Action}
	 * @see HashMap#put(Object, Object)
	 */
	public void add(final String tag, final PriorityAction action) {
		priorityActionMap.put(tag, action);
	}

	/**
	 * @see #add(String, PriorityAction)
	 */
	public void add(final String tag, final Action action) {
		add(tag, Actions.asPriority(action));
	}

	/**
	 * @param tag    标签
	 * @param action 如果该 {@code  Action} 不存在于集合中,将自动加入集合
	 */
	public void replace(final String tag, final PriorityAction action) {
		if (priorityActionMap.containsKey(tag)) {
			priorityActionMap.replace(tag, action);
		} else {
			add(tag, action);
		}
	}

	/**
	 * @see #replace(String, PriorityAction)
	 */
	public void replace(final String tag, final Action action) {
		replace(tag, Actions.asPriority(action));
	}

	/**
	 * @see HashMap#remove(Object)
	 */
	public void delete(final String tag) {
		priorityActionMap.remove(tag);
	}


	@Override
	public boolean run() {
		final ArrayList <PriorityAction> actions = new ArrayList <>(priorityActionMap.values());
		actions.sort(Comparator.comparing(x -> - x.getPriorityCode()));

		final Set <PriorityAction> remove = new HashSet <>();

		for (final PriorityAction action : actions) {
			if (! action.run()) {
				remove.add(action);
			}
		}

		actions.removeAll(remove);
		return ! actions.isEmpty();
	}

	@Override
	public void runTillEnd() {
		Actions.runAction(new PriorityThreadedAction(new ArrayList <>(priorityActionMap.values())));
		priorityActionMap.clear();
	}

	public Map <String, PriorityAction> getActionMap() {
		return priorityActionMap;
	}
}
