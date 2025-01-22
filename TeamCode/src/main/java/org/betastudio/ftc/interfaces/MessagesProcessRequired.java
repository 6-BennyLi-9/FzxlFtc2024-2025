package org.betastudio.ftc.interfaces;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.message.Message;

import java.util.concurrent.Callable;

public interface MessagesProcessRequired <K extends Message> extends Callable<K> {
	void sendRequest(@NonNull K message);
	default K call() {return null;}
}
