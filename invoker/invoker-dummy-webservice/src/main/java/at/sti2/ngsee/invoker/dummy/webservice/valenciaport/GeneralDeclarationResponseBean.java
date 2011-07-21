/**
 * GeneralDeclarationResponseBean.java - at.sti2.ngsee.invoker.dummy.webservice.valenciaport
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
