package org.betastudio.ftc.action.builder;

import static org.betastudio.ftc.action.ActionPolicies.CollectionAbortPolicy;
import static org.betastudio.ftc.action.ActionPolicies.EmptyCollectionResolvePolicy;

import org.betastudio.ftc.Interfaces.StoreRequired;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.ValueContainer;

public interface ActionBuilder extends StoreRequired <Action> {
	ValueContainer <EmptyCollectionResolvePolicy> policy = new ValueContainer <>(new CollectionAbortPolicy());

	static EmptyCollectionResolvePolicy getPolicy() {
		return policy.getV();
	}

	static void setPolicy(EmptyCollectionResolvePolicy p) {
		policy.setV(p);
	}

	void append(Action action);

	void clear();

	void remove(Action action);
}
