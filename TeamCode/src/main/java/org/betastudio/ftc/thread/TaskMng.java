package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.Labeler;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/** @noinspection UnusedReturnValue*/
public class TaskMng {
	private final Set <TaskFuture> tasks;
	private ExecutorService service;

	public TaskMng(final ExecutorService service) {
		this.service = service;
		tasks = new TreeSet <>(Comparator.comparing(TaskFuture::get));
	}

	@NonNull
	@Contract(value = "_ -> new", pure = true)
	public static TaskFuture newTaskFuture(final Future <?> future) {
		return newTaskFuture(Labeler.gen().summon(future), future);
	}

	@NonNull
	@Contract(value = "_, _ -> new", pure = true)
	public static TaskFuture newTaskFuture(final String str, final Future <?> future) {
		return new TaskFuture(str, future);
	}

	public List <Runnable> shutdown() {
		return service.shutdownNow();
	}

	public List <Runnable> reboot(final ExecutorService newService) {
		final List <Runnable> res = shutdown();
		service = newService;
		return res;
	}

	public Future <?> execute(final Runnable task) {
		final Future <?> submit = service.submit(task);
		tasks.add(newTaskFuture(submit));
		return submit;
	}

	public Future <?> execute(final String name, final Runnable task) {
		final Future <?> submit = service.submit(task);
		tasks.add(newTaskFuture(name, submit));
		return submit;
	}

	public <T> Future <T> execute(final Callable <T> task) {
		final Future <T> submit = service.submit(task);
		tasks.add(newTaskFuture(submit));
		return submit;
	}

	public <T> Future <T> execute(final String name, final Callable <T> task) {
		final Future <T> submit = service.submit(task);
		tasks.add(newTaskFuture(name, submit));
		return submit;
	}

	public Set <TaskFuture> getTasks() {
		return tasks;
	}
}
