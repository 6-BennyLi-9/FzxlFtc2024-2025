package org.firstinspires.ftc.teamcode.actions.packages;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.Actions;
import org.firstinspires.ftc.teamcode.actions.PriorityAction;
import org.firstinspires.ftc.teamcode.actions.utils.PriorityThreadedAction;

import java.util.*;

public class TaggedActionPackage extends ActionPackage{
	private final Map<String, PriorityAction> priorityActionMap;
	public TaggedActionPackage(){
		priorityActionMap=new HashMap<>();
	}

	/**@throws UnsupportedOperationException 必须提供Action的标签*/
	@Override
	public void add(Action action) {throw new UnsupportedOperationException("Must Given A Tag For Using This Method");}

	/**@throws UnsupportedOperationException 必须提供Action的标签*/
	@Override
	public void add(PriorityAction action) {throw new UnsupportedOperationException("Must Given A Tag For Using This Method");}

	/**
	 * @param tag 标签
	 * @param action 要加入的 {@code  Action}
	 * @see HashMap#put(Object, Object)
	 */
	public void add(String tag,PriorityAction action){
		priorityActionMap.put(tag, action);
	}

	/**
	 * @see #add(String, PriorityAction)
	 */
	public void add(String tag,Action action){
		add(tag,Actions.asPriority(action));
	}

	/**
	 * @param tag 标签
	 * @param action 如果该 {@code  Action} 不存在于集合中,将自动加入集合
	 */
	public void replace(String tag,PriorityAction action){
		if(priorityActionMap.containsKey(tag)){
			priorityActionMap.replace(tag, action);
		}else{
			add(action);
		}
	}
	/**
	 * @see #replace(String, PriorityAction)
	 */
	public void replace(String tag,Action action){
		replace(tag,Actions.asPriority(action));
	}

	/**
	 * @see HashMap#remove(Object)
	 */
	public void delete(String tag){
		priorityActionMap.remove(tag);
	}


	/**
	 * @see ActionPackage#run()
	 */
	@Override
	public boolean run() {
		final ArrayList<PriorityAction> actions = new ArrayList<>(priorityActionMap.values());
		actions.sort(Comparator.comparing(x->-x.getPriorityCode()));

		final Set<PriorityAction> remove=new HashSet<>();

		for(PriorityAction action:actions){
			if(!action.run()){
				remove.add(action);
			}
		}

		actions.removeAll(remove);
		return !actions.isEmpty();
	}

	/**
	 * @see ActionPackage#runTillEnd()
	 */
	@Override
	public void runTillEnd() {
		Actions.runAction(new PriorityThreadedAction(new ArrayList<>(priorityActionMap.values())));
		priorityActionMap.clear();
	}
}
