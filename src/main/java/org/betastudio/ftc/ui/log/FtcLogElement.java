package org.betastudio.ftc.ui.log;

import org.betastudio.ftc.util.message.LogMessage;
import org.betastudio.ftc.time.Timestamp;

public interface FtcLogElement {
	Timestamp getTimestamp();

	LogElementType getType();

	LogMessage getMessage();

	class ElementImpl implements FtcLogElement {
		private final LogMessage     message;
		private final Timestamp      timestamp;
		private final LogElementType type;

		public ElementImpl(final LogMessage message) {
			this(LogElementType.INFO, message);
		}

		public ElementImpl(final LogElementType type, final LogMessage message) {
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
		public LogMessage getMessage() {
			return message;
		}
	}
}
