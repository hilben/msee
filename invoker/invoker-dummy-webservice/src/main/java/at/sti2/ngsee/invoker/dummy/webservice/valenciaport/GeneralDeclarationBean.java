package at.sti2.ngsee.invoker.dummy.webservice.valenciaport;

public class GeneralDeclarationBean {
	private String arravialOrDepartureTime="";
	private String generalDeclarationId="";
	private String numberOfCrew="";
	private String numberOfPassengers="";
	private PortBean[] ports=null;
	private PortBean[] lastPortOfCall=null;
	
	private ShipBean ship=null;

	public GeneralDeclarationBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String getArravialOrDepartureTime() {
		return arravialOrDepartureTime;
	}
	public void setArravialOrDepartureTime(String arravialOrDepartureTime) {
		this.arravialOrDepartureTime = arravialOrDepartureTime;
	}

	public String getGeneralDeclarationId() {
		return generalDeclarationId;
	}

	public void setGeneralDeclarationId(String generalDeclarationId) {
		this.generalDeclarationId = generalDeclarationId;
	}

	public String getNumberOfCrew() {
		return numberOfCrew;
	}

	public void setNumberOfCrew(String numberOfCrew) {
		this.numberOfCrew = numberOfCrew;
	}

	public String getNumberOfPassengers() {
		return numberOfPassengers;
	}

	public void setNumberOfPassengers(String numberOfPassengers) {
		this.numberOfPassengers = numberOfPassengers;
	}

	public PortBean[] getPorts() {
		return ports;
	}

	public void setPorts(PortBean[] ports) {
		this.ports = ports;
	}

	public PortBean[] getLastPortOfCall() {
		return lastPortOfCall;
	}

	public void setLastPortOfCall(PortBean[] lastPortOfCall) {
		this.lastPortOfCall = lastPortOfCall;
	}

	public ShipBean getShip() {
		return ship;
	}

	public void setShip(ShipBean ship) {
		this.ship = ship;
	}
	
	

}
