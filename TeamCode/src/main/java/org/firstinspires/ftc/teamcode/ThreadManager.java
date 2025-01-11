package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;
import org.firstinspires.ftc.teamcode.events.IntegralThreadExceptionHandler;
import org.betastudio.ftc.interfaces.ThreadAdditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ThreadManager {
	private final Map <String,Thread> mem     =new HashMap <>();
	private final Labeler             labeler =new Labeler();

	public ThreadManager() {
	}

	public void interruptAll(){
		for (final Map.Entry <String, Thread> entry : mem.entrySet()) {
			final Thread e = entry.getValue();
			if(e instanceof ThreadAdditions){
				((ThreadAdditions) e).closeTask();
				new TaskCloseMonitor(e).start();
			}else {
				e.interrupt();
			}
		}
		mem.clear();
	}
	public void interrupt(final String tag){
		Objects.requireNonNull(mem.get(tag)).interrupt();
		mem.remove(tag);
	}

	public void addStarted(final String tag, final Thread startedThread){
		startedThread.setUncaughtExceptionHandler(new IntegralThreadExceptionHandler());
		mem.put(tag,startedThread);
	}
	/**
	 * 会自动运行
	 * */
	public void add(final String tag, @NonNull final Thread unstartedThread){
		unstartedThread.start();
		addStarted(tag,unstartedThread);
	}
	/**
	 * 会自动运行
	 * */
	public void add(@NonNull final Thread unstartedThread){
		add(labeler.summonID(unstartedThread),unstartedThread);
	}
	public void addStarted(final Thread startedThread){
		addStarted(labeler.summonID(startedThread),startedThread);
	}

	public boolean isEmpty(){
		return mem.isEmpty();
	}

	public Map <String,Thread> getMem(){
		return mem;
	}
}
