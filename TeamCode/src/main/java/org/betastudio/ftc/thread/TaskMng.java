package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.ValueProvider;

import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.robotcore.external.Func;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskMng {
	public static final class TaskFuture implements ValueProvider<String>, Func <Future<?>> {

		private final String str;
		private final Future <?> future;

		public TaskFuture(String str, Future <?> future) {
			this.str = str;
			this.future = future;
		}

		@Override
		public String get() {
			return str;
		}

		/**
		 * @param value 不会干任何事
		 */
		@Override
		public void set(String value) {}

		@Override
		public Future <?> value() {
			return future;
		}


	}
	@NonNull
	@Contract(value = "_ -> new", pure = true)
	public static TaskFuture newTaskFuture(Future <?> future) {
		return newTaskFuture(Labeler.generate().summonID(future), future);
	}

	@NonNull
	@Contract(value = "_, _ -> new", pure = true)
	public static TaskFuture newTaskFuture(String str, Future <?> future) {
		return new TaskFuture(str, future);
	}

	private ExecutorService service;

	private final Set <TaskFuture> tasks;

	public TaskMng(ExecutorService service) {
		this.service = service;
		tasks = new TreeSet <>(Comparator.comparing(TaskFuture::get));
	}

	public List <Runnable> shutdown(){
		return service.shutdownNow();
	}

	public List <Runnable> reboot(ExecutorService newService){
		List <Runnable> res = shutdown();
		service=newService;
		return res;
	}

	public Future <?> execute(Runnable task) {
		Future <?> submit = service.submit(task);
		tasks.add(newTaskFuture(submit));
		return submit;
	}

	public Future <?> execute(String name, Runnable task){
		Future <?> submit = service.submit(task);
		tasks.add(newTaskFuture(name, submit));
		return submit;
	}

	public <T> Future <T> execute(Callable<T> task){
		Future <T> submit = service.submit(task);
		tasks.add(newTaskFuture(submit));
		return submit;
	}

	public <T> Future <T> execute(String name,Callable<T> task){
		Future <T> submit = service.submit(task);
		tasks.add(newTaskFuture(name, submit));
		return submit;
	}

	public Set <TaskFuture> getTasks() {
		return tasks;
	}
}
