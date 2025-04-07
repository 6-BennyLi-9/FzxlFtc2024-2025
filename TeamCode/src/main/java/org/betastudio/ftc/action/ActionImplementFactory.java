package org.betastudio.ftc.action;

import org.betastudio.ftc.ui.log.*;

import java.util.concurrent.*;

import static org.betastudio.ftc.Interfaces.*;

/**
 * 子类只需调用 {@link #setAction(Callable)}并重写 {@link #paramsString()}即可
 */
public abstract class ActionImplementFactory implements Action, ThreadEx, Nameable {
	private Callable <Boolean> action;
	private Callable <String>  params;
	private boolean            isStopRequested;
	private String             name = "[unnamed]";

	protected ActionImplementFactory() {
		this(() -> false,() -> "[unsetted]");
	}

	protected ActionImplementFactory(final Callable<Boolean> action, final Callable<String> params) {
		this.action = action;
		this.params = params;
	}

	@Override
	public boolean activate() {
		if (isStopRequested) {
			return false;
		}
		try {
			return action.call();
		} catch (final Exception e) {
			FtcLogTunnel.MAIN.report(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void closeTask() {
		isStopRequested = true;
	}

	public Callable <Boolean> getAction() {
		return action;
	}

	public void setAction(final Callable <Boolean> action) {
		this.action = action;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		 this.name = name;
	}

	public void setParams(final Callable<String> params){
		this.params = params;
	}

	@Override
	public String paramsString(){
		try{
			return params.call();
		}catch(final Exception e){
			throw new RuntimeException(e);
		}
	}
}
