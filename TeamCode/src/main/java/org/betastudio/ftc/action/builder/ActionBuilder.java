package org.betastudio.ftc.action.builder;

import static org.betastudio.ftc.action.ActionPolicies.*;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.ValueContainer;

public interface ActionBuilder extends Interfaces.StoreRequired <Action> {
	ValueContainer <EmptyCollectionResolvePolicy> policy = new ValueContainer<>(new CollectionAbortPolicy());

	void append(Action action);

	void clear();

	void remove(Action action);

	static void setPolicy(EmptyCollectionResolvePolicy p){
		policy.setV(p);
	}

	static EmptyCollectionResolvePolicy getPolicy(){
		return policy.getV();
	}
}
