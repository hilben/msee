/**
 * 
 */
package at.sti2.wsmf.core;

import java.util.List;

import at.sti2.wsmf.core.data.qos.QoSParamAtTime;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class PersistenHandlerUtility {
	public static double getAverageValue(List<QoSParamAtTime> vals) {

		double avg = 0;

		for (QoSParamAtTime q : vals) {
			avg += Double.parseDouble(q.getQosParamValue());
		}

		avg = avg / vals.size();

		return avg;
	}

	public static double getSumOfValues(List<QoSParamAtTime> vals) {

		double sum = 0;

		for (QoSParamAtTime q : vals) {
			sum += Double.parseDouble(q.getQosParamValue());
		}

		return sum;
	}

	public static double getMaxOfValues(List<QoSParamAtTime> vals) {
		
		double max =0;
		
		for (QoSParamAtTime q : vals) {
			double v = Double.parseDouble(q.getQosParamValue());
			if (v>max) {
				max = v;
			}
		}
	
		return max;
	}
	
	public static double getMinOfValues(List<QoSParamAtTime> vals) {
		
		double min =0;
		
		for (QoSParamAtTime q : vals) {
			double v = Double.parseDouble(q.getQosParamValue());
			if (v<min) {
				min = v;
			}
		}
	
		return min;
	}
}
