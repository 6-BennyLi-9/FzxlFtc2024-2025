package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ThreadManager {
	private final Map <String,Thread> mem =new HashMap <>();

	public ThreadManager() {
	}

	public void interruptAll(){
		mem.forEach((t,e)->e.interrupt());
		mem.clear();
	}
	public void interrupt(String tag){
		Objects.requireNonNull(mem.get(tag)).interrupt();
		mem.remove(tag);
	}

	public void add(String tag, @NonNull Thread unstartedThread){
		unstartedThread.start();
		mem.put(tag,unstartedThread);
	}
	public void addStarted(String tag,Thread startedThread){
		mem.put(tag,startedThread);
	}

	public boolean isEmpty(){
		return mem.isEmpty();
	}
}
