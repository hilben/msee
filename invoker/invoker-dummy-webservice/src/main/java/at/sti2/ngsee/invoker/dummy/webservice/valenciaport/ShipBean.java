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
