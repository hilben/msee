/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
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
