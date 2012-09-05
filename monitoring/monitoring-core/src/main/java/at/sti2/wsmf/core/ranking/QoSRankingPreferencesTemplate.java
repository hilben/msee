/**
 * 
 */
package at.sti2.wsmf.core.ranking;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.sti2.wsmf.api.data.qos.QoSParamKey;

/**
 * @author Benjamin Hiltpolt
 * 
 *         This class stores a set of QoS Params for the ranking
 * 
 */
public class QoSRankingPreferencesTemplate {

	protected Map<QoSParamKey, Float> propIdentifiersImportance = new HashMap<QoSParamKey, Float>();

	/**
	 * 
	 */
	public QoSRankingPreferencesTemplate() {
	}

	/**
	 * 
	 * Creates a QosRankingPreferencesTemplate with a default Set
	 * @param propIdentifiersImportance
	 */
	public QoSRankingPreferencesTemplate(
			Map<QoSParamKey, Float> propIdentifiersImportance) {
		this.propIdentifiersImportance = propIdentifiersImportance;
	}

	/**
	 * Adds a QoS Param with is related importance for the ranking algorithm
	 * 
	 * @param qosParam
	 * @param importance
	 */
	public void addPropertyAndImportance(QoSParamKey qosParam, Float importance) {
		this.propIdentifiersImportance.put(qosParam, importance);
	}

	/**
	 * 
	 * returns the importance of a QoS
	 * @param qosParam
	 * @return
	 */
	public float getPropertyImportance(QoSParamKey qosParam) {
		return this.propIdentifiersImportance.get(qosParam);
	}

	
	/**
	 * returns all QoS of this template
	 * @return
	 */
	public Set<QoSParamKey> getQoSParams() {
		return this.propIdentifiersImportance.keySet();
	}

}
