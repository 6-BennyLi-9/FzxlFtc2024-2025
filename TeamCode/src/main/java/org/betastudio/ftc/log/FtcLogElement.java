package org.betastudio.ftc.log;

import org.betastudio.ftc.message.Message;

public interface FtcLogElement {
	double getTimestamp();
	LogElementType getType();
	Message getMessage();

	class ElementImpl implements FtcLogElement {
		private final Message message;
		private final double timestamp;
		private final LogElementType type;

		public ElementImpl(final Message message) {
			this(LogElementType.INFO, message);
		}
		public ElementImpl(final LogElementType type, final Message message) {
			this.message = message;
			this.timestamp = System.currentTimeMillis() / 1000.0;
			this.type = type;
		}

		@Override
		public double getTimestamp() {
			return timestamp;
		}

		@Override
		public LogElementType getType() {
			return type;
		}

		@Override
		public Message getMessage() {
			return message;
		}
	}
}
