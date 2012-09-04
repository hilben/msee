/**
 * 
 */
package eu.soa4all.ranking.qos;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class PropertiesRankingValuesTable implements
		Comparable<PropertiesRankingValuesTable> {

	private String endpointName;
	private Map<String, Float> propIdentifiersOrderingValues = new HashMap<String, Float>();

	public PropertiesRankingValuesTable(String name,
			MonitoringRankingPropertySet template) {
		this.endpointName = name;

		HashMap<String, Float> valueTableTemplate = new HashMap<String, Float>();

		for (String s : template.getPropertySet()) {
			this.propIdentifiersOrderingValues.put(s, new Float(Float.NaN));
		}
	}

	public Float getRankingValueForProperty(String property) {
		return this.propIdentifiersOrderingValues.get(property);
	}

	public void setRankingValueForProperty(String property, Float value)
			throws Exception {
		if (this.propIdentifiersOrderingValues.containsKey(property)) {
			this.propIdentifiersOrderingValues.put(property, value);
		} else {
			throw new Exception(); // TODO:
		}
	}

	public float getSummedUpValues() {
		float retVal = 0;
		for (String s : this.propIdentifiersOrderingValues.keySet()) {
			retVal += this.propIdentifiersOrderingValues.get(s);
		}

		return retVal;
	}

	public String toString() {
		String ret = "";

		ret += "#####################################################\n";

		ret += this.endpointName + "\n";

		for (String s : this.propIdentifiersOrderingValues.keySet()) {
			ret += s + ": " + this.getRankingValueForProperty(s) + "\n";
		}
		ret += "\n";

		ret += "Summed up: " + this.getSummedUpValues();

		ret += "#####################################################\n";

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(PropertiesRankingValuesTable o) {

		if (this.getSummedUpValues() <= o.getSummedUpValues()) {
			return 1;
		}
		if (this.getSummedUpValues() > o.getSummedUpValues()) {
			return -1;
		}

		return 0;
	}

	/**
	 * @return
	 */
	public String getName() {
		return this.endpointName;
	}

}
