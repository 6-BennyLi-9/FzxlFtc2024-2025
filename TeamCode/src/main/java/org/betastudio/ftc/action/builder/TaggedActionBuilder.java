package org.betastudio.ftc.action.builder;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;

public interface TaggedActionBuilder extends ActionBuilder {
	@Override
	default void append(final Action action) {
		append(Labeler.summon(action), action);
	}

	void append(String tag, Action action);

	void remove(String tag);
}
