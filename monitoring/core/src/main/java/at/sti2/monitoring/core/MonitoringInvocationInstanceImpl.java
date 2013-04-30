package at.sti2.monitoring.core;

import java.net.URL;
import java.util.UUID;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class MonitoringInvocationInstanceImpl implements
		MonitoringInvocationInstance {

	private MonitoringComponent monitoringComponent;
	private UUID uuid;
	private URL webService;
	private MonitoringInvocationState state;
	
	public MonitoringInvocationInstanceImpl(URL webService, MonitoringComponent monitoringComponent, MonitoringInvocationState state, String uuid) {
		this.monitoringComponent = monitoringComponent;
		this.webService = webService;
		this.state = state;
		this.uuid = UUID.fromString(uuid);
	}
	
	public MonitoringInvocationInstanceImpl(URL webService, MonitoringComponent monitoringComponent) {
		this.webService = webService;
		this.uuid = UUID.randomUUID();
		this.monitoringComponent =  monitoringComponent;
		this.state = MonitoringInvocationState.None;
	}
	
	
	@Override
	public void updateInvocationState(MonitoringInvocationState state)
			throws MonitoringException {
		this.state = state;
		this.monitoringComponent.updateInvocationInstance(this);
	}

	@Override
	public void updateAndCloseSuccessfullInvocation(double payloadSizeResponse,
			double payloadSizeRequest, double responseTime)
			throws MonitoringException {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateAndCloseUnsuccessfullInvocation()
			throws MonitoringException {
		// TODO Auto-generated method stub

	}


	@Override
	public URL getWebService() {
		return this.webService;
	}


	@Override
	public UUID getUUID() {
		return this.uuid;
	}


	@Override
	public MonitoringInvocationState getState() {
		return this.state;
	}


	@Override
	public void setState(MonitoringInvocationState state) throws MonitoringException {
		this.state = state;
		this.monitoringComponent.updateInvocationInstance(this);
	}

	@Override
	public String toString() {
		return "MonitoringInvocationInstanceImpl [monitoringComponent="
				+ monitoringComponent + ", uuid=" + uuid + ", webService="
				+ webService + ", state=" + state + "]";
	}

}
