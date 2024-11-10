package org.firstinspires.ftc.teamcode.actions.utils;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.actions.Action;

import java.util.Arrays;
import java.util.List;

public final class LinkedAction implements Action{
	private final List<Action> actions;
	private int ptr;

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

	@NonNull
	@Override
	public String paramsString() {
		final StringBuilder res= new StringBuilder("{");
		for(final Action action:actions){
			res.append(action.getClass().getSimpleName()).append(":").append(action.paramsString()).append(",");
		}
		return res.append("}").toString();
	}
}
