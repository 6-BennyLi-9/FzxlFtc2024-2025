package org.firstinspires.ftc.teamcode.actions.utils;


import org.firstinspires.ftc.teamcode.actions.Action;

import java.util.Arrays;
import java.util.List;

public final class LinkedAction implements Action{
	private final List<Action> actions;
	private int ptr=0;

	public LinkedAction(final List<Action> actions){
		this.actions=actions;
	}
	public LinkedAction(final Action... actions){
		this(Arrays.asList(actions));
	}

	@Override
	public boolean run() {
		if(actions.get(ptr).run()){
			return true;
		}else{
			ptr++;
			return ptr<actions.size();
		}
	}
}
