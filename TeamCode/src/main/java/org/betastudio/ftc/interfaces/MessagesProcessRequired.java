package org.betastudio.ftc.interfaces;

public interface MessagesProcessRequired <K> {
	void send(K message);
	default K send() {return null;}
}
