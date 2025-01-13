package org.betastudio.ftc.interfaces;

public interface MessageProcessAble <M>{
	void sendMessage(M message);
	M callbackMessage();
}
