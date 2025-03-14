package org.betastudio.ftc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.message.Message;

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
		default String getProgressString(){
			StringBuilder builder = new StringBuilder();
			for (int i = 0 ; i < 10 ; i++) {
				if (i / 10.0 <= getProgress()) {
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
	public interface JobProgressRender {
		default void render(ProgressMarker marker) {
			render("*", marker);
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
}
