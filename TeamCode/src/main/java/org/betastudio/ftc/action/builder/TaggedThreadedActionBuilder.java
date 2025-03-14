package org.betastudio.ftc.action.builder;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaggedThreadedActionBuilder implements TaggedActionBuilder{
	protected Map<String, Action> actions;

	public TaggedThreadedActionBuilder() {
		actions = new HashMap <>();
	}

	@Override
	public void append(String tag, Action action) {
		actions.put(tag, action);
	}

	@Override
	public void remove(String tag) {
		actions.remove(tag);
	}

	@Override
	public void remove(Action action) {
		Set <String> remove = new HashSet <>();

		for (Map.Entry <String, Action> entry : actions.entrySet()) {
			if (entry.getValue() == action){
				remove.add(entry.getKey());
			}
		}

		remove.forEach(actions::remove);
	}

	@Override
	public void clear() {
		actions.clear();
	}

	@Override
	public Action store() {
		return new ThreadedAction(new ArrayList <>(actions.values()));
	}
}
