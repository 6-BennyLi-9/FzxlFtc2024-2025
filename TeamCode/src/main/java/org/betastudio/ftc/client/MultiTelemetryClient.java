package org.betastudio.ftc.client;

import org.firstinspires.ftc.teamcode.util.Labeler;

import java.util.HashMap;
import java.util.Map;

public class MultiTelemetryClient implements Client{
	private final Map < String , Client> clients =new HashMap <>();
	private final Labeler labeler=new Labeler();

	public MultiTelemetryClient(Client... clients) {
		for (Client client : clients) {
			this.clients.put(labeler.summonID(client),client);
		}
	}

	public void add(Client client){
		add(labeler.summonID(client),client);
	}
	public void add(String tag,Client client){
		clients.put(tag,client);
	}

	@Override
	public void clear() {
		for (Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().clear();
		}
	}

	@Override
	public Client addData(String key, String val) {
		for (Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().addData(key,val);
		}
		return this;
	}

	@Override
	public Client addData(String key, Object val) {
		return this.addData(key,String.valueOf(val));
	}

	@Override
	public Client deleteData(String key) {
		return null;
	}

	@Override
	public Client changeData(String key, String val) {
		return null;
	}

	@Override
	public Client changeData(String key, Object val) {
		return null;
	}

	@Override
	public Client addLine(String key) {
		return null;
	}

	@Override
	public Client addLine(Object key) {
		return null;
	}

	@Override
	public Client deleteLine(String key) {
		return null;
	}

	@Override
	public Client changeLine(String oldData, String newData) {
		return null;
	}

	@Override
	public void update() {

	}
}
