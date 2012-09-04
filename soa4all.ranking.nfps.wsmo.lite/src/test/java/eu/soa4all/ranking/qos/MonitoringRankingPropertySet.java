/**
 * 
 */
package eu.soa4all.ranking.qos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.soa4all.ranking.rules.NFPInfo;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class MonitoringRankingPropertySet {

	private Map<String, Float> propIdentifiersImportance = new HashMap<String,Float>();

	public MonitoringRankingPropertySet() {
		
	}
	
	public MonitoringRankingPropertySet(Map<String, Float> propIdentifiersImportance) {
		this.propIdentifiersImportance = propIdentifiersImportance;
	}
	
	public void addPropertyAndImportance(String property, Float importance) {
		this.propIdentifiersImportance.put(property, importance);
	}
	
	public float getPropertyImportance(String property) {
		return this.propIdentifiersImportance.get(property);
	}
	
	public Set<String> getPropertySet() {
		return this.propIdentifiersImportance.keySet();
	}
	


}
