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

/**
 * Example Response to a GeneralDeclartion Fal Form 1.
 * 
 * @author Alex Oberhauser
 */
public class GeneralDeclarationResponseBean {
	private String id;
	private String shipID;
	
	public GeneralDeclarationResponseBean() {}
	
	public void setID(String _id) {
		this.id = _id;
	}
	
	public String getID() {
		return this.id;
	}
	
	public void setShipID(String _shipID) {
		this.shipID = _shipID;
	}
	
	public String getShipID() {
		return this.shipID;
	}
	
}
