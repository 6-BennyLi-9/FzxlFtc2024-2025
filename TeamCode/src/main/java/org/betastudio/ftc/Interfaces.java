package org.betastudio.ftc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.message.Message;

public final class Interfaces {
	public interface HardwareController {
		void connect();

		Action getController();

		void writeToInstance();
	}

	@FunctionalInterface
	public interface InitializeRequested {
		void init();
	}

	public interface MessagesProcessRequired<K extends Message> {
		void sendMsg(@NonNull final K message);

		@Nullable
		default K callMsg() {
			return null;
		}
	}

	public interface TagOptionsRequired {
		String getTag();

		void setTag(String tag);
	}

	public interface ThreadEx {
		/// 安全的结束器，例如发送结束信号
		void closeTask();
	}

	@FunctionalInterface
	public interface Updatable {
		void update();
	}

	@FunctionalInterface
	public interface Countable {
		long getCount();
	}

	public interface ProgressMarker {
		long getTotal();
		long getDone();
		default double getProgress(){
			return (double) getDone() / getTotal();
		}

		default String getProgressString() {
			return getProgressString(10);
		}
		default String getProgressString(final int length){
			final StringBuilder builder = new StringBuilder();
			for (int i = 0 ; i < length ; i++) {
				if ((double) i / length <= getProgress()) {
					builder.append('=');
				} else {
					builder.append('-');
				}
			}
			return builder.toString();
		}

		void tick();
	}

	@FunctionalInterface
	public interface ProgressRender {
		default void render(final ProgressMarker marker) {
			render("*unnamed*", marker);
		}

		void render(String name, ProgressMarker marker);
	}

	@FunctionalInterface
	public interface StoreRequired <T> {
		T store();
	}

	@FunctionalInterface
	public interface ValueProduction <T>{
		T getVal();
	}

	public interface Nameable {
		String getName();
		void setName(String name);
	}

	@FunctionalInterface
	public interface ConstNameable extends Nameable {
		@Override
		default void setName(final String name) {
			throw new IllegalStateException("Cannot set name of a constant named object");
		}
	}

	public interface ProgressedTask {
		ProgressMarker getWorkerProgress();
	}
}
