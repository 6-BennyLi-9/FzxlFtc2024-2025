package org.betastudio.ftc.ui.log;

public enum LogElementType {
	INFO("INFO"), WARNING("WARNING"), ERROR("ERROR"), EXCEPTION("EXCEPTION");
	final String caption;

	LogElementType(final String caption) {
		this.caption = caption;
	}
}
