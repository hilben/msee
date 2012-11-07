/**
 * 
 */
package at.sti2.wsmf.core.data.qos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Benjamin Hiltpolt
 * 
 *         Stores a qosParamValue and a time point in xsd:datetime
 *         
 *         TODO: conceptional work
 */
public class QoSParamAtTime implements Comparable<QoSParamAtTime> {

	protected String qosParamValue;
	protected String time;

	/**
	 * @param qosParamValue
	 * @param time
	 */
	public QoSParamAtTime(String qosParamValue, String time) {
		super();
		this.qosParamValue = qosParamValue;
		this.time = time;
	}

	public String getQosParamValue() {
		return qosParamValue;
	}

	public void setQosParamValue(String qosParamValue) {
		this.qosParamValue = qosParamValue;
	}

	public String getTime() {
		return time;
	}

	/**
	 * @return
	 */
	public String getTimeForGoogleCharts() {
		// Convert xsd:datetime format e.g:
		// 2012-11-05T17:17:41+0100
		// to: Date(2010,0,15,11,10,23)
		String returnString = "";
		StringBuilder strb = new StringBuilder(this.time);

		returnString += "Date(";
		returnString += strb.substring(0, 4);//years
        returnString += ",";
		returnString += String.valueOf(Integer.parseInt(strb.substring(5, 7))-1);//months - 1 hence google starts months with 0
        returnString += ",";
		returnString += strb.substring(8, 10); // days
        returnString += ",";
		returnString += strb.substring(11, 13); // hours
        returnString += ",";
		returnString += strb.substring(14, 16); // minutes
        returnString += ",";
		returnString += strb.substring(17, 19); // seconds
        returnString += ")";
        
        return returnString;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(QoSParamAtTime other) {
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssZ");

		Date metime;
		Date othertime;

		try {
			metime = df.parse(this.time);
			othertime = df.parse(other.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}

		return metime.compareTo(othertime);
	}

	public static void main(String args[]) {
		
		QoSParamAtTime q = new QoSParamAtTime("SomeTestParam", "2012-11-05T17:17:41+0100");
		q.compareTo(q);
		
		System.out.println(q.getTimeForGoogleCharts());
	}

}
