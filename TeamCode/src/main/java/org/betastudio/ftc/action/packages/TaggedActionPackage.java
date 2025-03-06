package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.utils.PriorityThreadedAction;
import org.betastudio.ftc.util.Labeler;

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

	@Override
	public void add(final Action action) {
		add(Labeler.gen().summon(action), action);
	}

	@Override
	public void add(final PriorityAction action) {
		add(Labeler.gen().summon(action), action);
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
		add(tag, Actions.newMirroredPriority(action));
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
		replace(tag, Actions.newMirroredPriority(action));
	}

	/**
	 * @see HashMap#remove(Object)
	 */
	public void delete(final String tag) {
		priorityActionMap.remove(tag);
	}


	@Override
	public boolean activate() {
		synchronized (actions){
			final ArrayList <PriorityAction> actions = new ArrayList <>(priorityActionMap.values());
			actions.sort(Comparator.comparing(x -> - x.getPriorityCode()));

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
	 * 作为本地方法
	 */
	@Override
	public void activateTillEnd() {
		synchronized (actions){
			new PriorityThreadedAction(new ArrayList <>(priorityActionMap.values())).run();
			priorityActionMap.clear();
		}
	}

	public Map <String, PriorityAction> getActionMap() {
		return priorityActionMap;
	}
}
