/**
 * 
 */
package at.sti2.wsmf.core.ranking;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import at.sti2.wsmf.api.data.qos.QoSParamKey;

/**
 * @author Benjamin Hiltpolt
 *
 */
/**
 * @author Benjamin Hiltpolt
 *
 */
public class QoSRankingEngine {
	
	
	protected static Logger logger = Logger.getLogger(QoSRankingEngine.class);
	
	
	
	/**
	 * 
	 * Returns the ordered list for qosTalbe according to a template
	 * Using a scoring matrix to calculate the normalized values
	 * 
	 * @param qosTables
	 * @param qosRankingTemplate
	 * @return
	 */
	public static List<QoSParamsEndpointRankingTable> rankQoSParamsTables(List<QoSParamsEndpointRankingTable> qosTables, QoSRankingPreferencesTemplate qosRankingTemplate) {
		// Do the ordering using a scoring matrix
		for (QoSParamKey s : qosRankingTemplate.getQoSParams()) {
			// Find max
			float max = Float.NEGATIVE_INFINITY;
			for (QoSParamsEndpointRankingTable table : qosTables) {
				if (table.getRankingValueForProperty(s) > max) {
					max = table.getRankingValueForProperty(s);
				}
			}

			// Normalize all the value by dividing through the max value
			// and multiple by preference
			for (QoSParamsEndpointRankingTable table : qosTables) {
				float normalized = table.getRankingValueForProperty(s) / max;
				normalized *= qosRankingTemplate.getPropertyImportance(s);
				try {
					table.setRankingValueForProperty(s, normalized);
				} catch (Exception e1) {
					logger.error(e1.getCause());
				}
			}
		}

		Collections.sort(qosTables);
		
		
		return qosTables;
	}

}
