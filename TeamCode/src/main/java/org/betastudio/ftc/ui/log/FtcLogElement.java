package org.betastudio.ftc.ui.log;

import org.betastudio.ftc.message.LogMessages;
import org.betastudio.ftc.util.Timestamp;

public interface FtcLogElement {
	Timestamp getTimestamp();

	LogElementType getType();

	LogMessages.LogMsg getMessage();

	class ElementImpl implements FtcLogElement {
		private final LogMessages.LogMsg message;
		private final Timestamp          timestamp;
		private final LogElementType         type;

		public ElementImpl(final LogMessages.LogMsg message) {
			this(LogElementType.INFO, message);
		}

		public ElementImpl(final LogElementType type, final LogMessages.LogMsg message) {
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
		public LogMessages.LogMsg getMessage() {
			return message;
		}
	}
}
