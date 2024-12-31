package org.betastudio.ftc.client;

/** @noinspection UnusedReturnValue*/
public interface Client {
	void clear();
	void update();

	Client addData(String key, String val);
	Client addData(String key, Object val);
	Client deleteData(String key);
	Client changeData(String key, String val);
	Client changeData(String key, Object val);
	Client addLine(String key);
	Client addLine(Object key);
	Client deleteLine(String key);
	Client changeLine(String oldData, String newData);
}
