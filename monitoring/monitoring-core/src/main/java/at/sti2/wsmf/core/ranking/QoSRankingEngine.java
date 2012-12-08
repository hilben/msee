/**
 * 
 */
package at.sti2.wsmf.core.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.ranking.QoSRankingPreferencesTemplate;

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
	 * Returns the ordered list for qosTalbe according to a template Using a
	 * scoring matrix to calculate the normalized values
	 * 
	 * @param qosTables
	 * @param qosRankingTemplate
	 * @return
	 */
	public static List<QoSParamsEndpointRankingTable> rankQoSParamsTables(
			List<QoSParamsEndpointRankingTable> qosTables,
			QoSRankingPreferencesTemplate qosRankingTemplate) {
		// Do the ordering using a scoring matrix
		for (String s : qosRankingTemplate.getQoSParams()) {
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

	
	/**
	 * @param keys
	 * @param preferenceValue
	 * @param endpoints
	 * @return
	 */
	public static List<String> getQoSRankedEndpoints(
			String[] keys, Float[] preferenceValue, String[] endpoints) {

		QoSRankingPreferencesTemplate qosRankingTemplate = new QoSRankingPreferencesTemplate();

		for (int i = 0; i < keys.length; i++) {
			//Case of invalid preferenceValue array
			if (i >= preferenceValue.length) {
				qosRankingTemplate.addPropertyAndImportance(keys[i], 0.0f);
			} else {
				qosRankingTemplate.addPropertyAndImportance(keys[i],
						preferenceValue[i]);
			}
		}

		List<QoSParamsEndpointRankingTable> qosTables = new ArrayList<QoSParamsEndpointRankingTable>();
		for (String ep : endpoints) {
			qosTables.add(new QoSParamsEndpointRankingTable(ep,
					qosRankingTemplate));
		}

		for (QoSParamsEndpointRankingTable t : qosTables) {
			t.retrieveQoSParamValues();
		}

		QoSRankingEngine.rankQoSParamsTables(qosTables, qosRankingTemplate);

		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < endpoints.length; i++) {
			ret.add(qosTables.get(i).getName());
		}

		return ret;
	}
	

}
