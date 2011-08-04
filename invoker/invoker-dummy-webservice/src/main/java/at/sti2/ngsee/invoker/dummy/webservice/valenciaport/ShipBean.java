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

public class ShipBean {
	
	private CountryBean flagState=null;
	private String callSign="";
	private int grossTonnage=0;
	private int netTonnage=0;
	private int IMONumber=0;
	private String shipName=""; 
	private String shipMasterName="";
	private String shipType="";

	
	
	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	public int getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(int grossTonnage) {
		this.grossTonnage = grossTonnage;
	}

	public int getNetTonnage() {
		return netTonnage;
	}

	public void setNetTonnage(int netTonnage) {
		this.netTonnage = netTonnage;
	}

	public int getIMONumber() {
		return IMONumber;
	}

	public void setIMONumber(int iMONumber) {
		IMONumber = iMONumber;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipMasterName() {
		return shipMasterName;
	}

	public void setShipMasterName(String shipMasterName) {
		this.shipMasterName = shipMasterName;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public CountryBean getFlagState() {
		return flagState;
	}

	public void setFlagState(CountryBean flagState) {
		this.flagState = flagState;
	}

}
