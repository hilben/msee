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
 */
public class QoSParamAtTime implements Comparable<QoSParamAtTime>{
	
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
	public void setTime(String time) {
		this.time = time;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(QoSParamAtTime other) {
		DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
		
		Date metime ;
		   Date othertime ;
		
		try {
		   metime =  df.parse(this.time);
		   othertime = df.parse(other.getTime());
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		return metime.compareTo(othertime);
	}
	
	

}
