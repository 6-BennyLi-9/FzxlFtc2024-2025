package org.firstinspires.ftc.teamcode.actions;

public interface Action {
	/**
	 * @return 该 {@code Action} 块是否结束. 为真时,该 {@code Action} 未结束,反之亦然.
	 */
	boolean run();
	default Action next(){return new Actions.FinalNodeAction();}

	default String paramsString(){return String.valueOf(this.hashCode());}
}
