package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.util.Labeler;

public interface TaggedActionPackage extends ActionPackage {
	@Override
	default void add(Action action) {
		add(Labeler.gen().summon(action), action);
	}

	@Override
	default void add(PriorityAction action) {
		add(Labeler.gen().summon(action), action);
	}

	void add(String tag, PriorityAction action);

	void add(String tag, Action action);

	void replace(String tag, PriorityAction action);

	void replace(String tag, Action action);

	void delete(String tag);
}
