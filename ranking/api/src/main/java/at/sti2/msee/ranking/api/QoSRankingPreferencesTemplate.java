/**
 * 
 */
package at.sti2.msee.ranking.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Benjamin Hiltpolt
 * 
 *         This class stores a set of QoS Params for the ranking
 * 
 */
public class QoSRankingPreferencesTemplate {

	protected Map<String, Float> propIdentifiersImportance = new HashMap<String, Float>();

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
			Map<String, Float> propIdentifiersImportance) {
		this.propIdentifiersImportance = propIdentifiersImportance;
	}

	/**
	 * Adds a QoS Param with is related importance for the ranking algorithm
	 * 
	 * @param qosParam
	 * @param importance
	 */
	public void addPropertyAndImportance(String qosParam, Float importance) {
		this.propIdentifiersImportance.put(qosParam, importance);
	}

	/**
	 * 
	 * returns the importance of a QoS
	 * @param qosParam
	 * @return
	 */
	public float getPropertyImportance(String qosParam) {
		return this.propIdentifiersImportance.get(qosParam);
	}

	
	/**
	 * returns all QoS of this template
	 * @return
	 */
	public Set<String> getQoSParams() {
		return this.propIdentifiersImportance.keySet();
	}

}
