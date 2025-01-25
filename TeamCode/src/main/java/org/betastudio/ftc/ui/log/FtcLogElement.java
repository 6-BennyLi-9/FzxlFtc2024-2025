package org.betastudio.ftc.ui.log;

import org.betastudio.ftc.util.message.Message;
import org.betastudio.ftc.util.time.Timestamp;

public interface FtcLogElement {
	Timestamp getTimestamp();
	LogElementType getType();
	Message getMessage();

	class ElementImpl implements FtcLogElement {
		private final Message        message;
		private final Timestamp      timestamp;
		private final LogElementType type;

		public ElementImpl(final Message message) {
			this(LogElementType.INFO, message);
		}

		public ElementImpl(final LogElementType type, final Message message) {
			this.message = message;
			this.timestamp = new Timestamp();
			this.type = type;
		}

		@Override
		public Timestamp getTimestamp() {
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
