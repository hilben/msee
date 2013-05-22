/**
 * 
 */
package at.sti2.msee.ranking.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.ranking.api.IQoSParamsEndpointRankingTable;
import at.sti2.msee.ranking.api.QoSRankingPreferencesTemplate;
import at.sti2.msee.ranking.api.exception.RankingException;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 *         A table to store all ranked QoSParams of an endpoint.
 * 
 */
public class QoSParamsEndpointRankingTable implements
		IQoSParamsEndpointRankingTable {

	private static Logger logger = Logger
			.getLogger(QoSParamsEndpointRankingTable.class);

	private String endpointName;
	private Map<String, Float> propIdentifiersOrderingValues = new HashMap<String, Float>();

	/**
	 * 
	 * Creates a QoSOrderingValueTable for an endpoint
	 * 
	 * @param endpointName
	 * @param template
	 */
	public QoSParamsEndpointRankingTable(String endpointName,
			QoSRankingPreferencesTemplate template) {
		this.endpointName = endpointName;

		for (String s : template.getQoSParams()) {
			this.propIdentifiersOrderingValues.put(s, new Float(Float.NaN));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.wsmf.core.ranking.asdfd#getRankingValueForProperty(at.sti2.wsmf
	 * .api.data.qos.QoSParamKey)
	 */
	@Override
	public Float getRankingValueForProperty(String property) {
		return this.propIdentifiersOrderingValues.get(property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.wsmf.core.ranking.asdfd#setRankingValueForProperty(at.sti2.wsmf
	 * .api.data.qos.QoSParamKey, java.lang.Float)
	 */
	@Override
	public void setRankingValueForProperty(String property, Float value)
			throws RankingException {
		if (this.propIdentifiersOrderingValues.containsKey(property)) {
			this.propIdentifiersOrderingValues.put(property, value);
		} else {
			throw new RankingException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.sti2.wsmf.core.ranking.asdfd#retrieveQoSParamValues()
	 */
	@Override
	public void retrieveQoSParamValues() throws RankingException {

		MonitoringComponent monitoring = null;

		try {
			monitoring = MonitoringComponentImpl.getInstance();
		} catch (RepositoryException | IOException e) {
			throw new RankingException(e);
		}

		for (String key : this.getQoSParams()) {

			// handle invalid values or QoS for which no values were determined
			float value;
			try {
				value = Float.parseFloat(monitoring.getQoSParameter(
						new URL(this.endpointName), QoSType.valueOf(key))
						.getValue());

				this.setRankingValueForProperty(key, value);
			} catch (NumberFormatException | MalformedURLException
					| MonitoringException | MonitoringNoDataStoredException
					| RankingException e) {
				throw new RankingException(e);
			}
		}
	}

	/**
	 * Returns all QoSParamKeys
	 * 
	 * @return
	 */
	private Set<String> getQoSParams() {
		return this.propIdentifiersOrderingValues.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.sti2.wsmf.core.ranking.asdfd#getSummedUpValues()
	 */
	@Override
	public float getSummedUpValues() {
		float retVal = 0;
		for (String s : this.propIdentifiersOrderingValues.keySet()) {
			retVal += this.propIdentifiersOrderingValues.get(s);
		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ret = "";

		ret += "#####################################################"
				+ System.getProperty("line.separator");

		ret += this.endpointName + "\n";

		for (String s : this.propIdentifiersOrderingValues.keySet()) {
			ret += s + ": " + this.getRankingValueForProperty(s)
					+ System.getProperty("line.separator");
		}
		ret += "\n";

		ret += "Summed up: " + this.getSummedUpValues()
				+ System.getProperty("line.separator");

		ret += "#####################################################"
				+ System.getProperty("line.separator");

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see at.sti2.wsmf.core.ranking.asdfd#compareTo(at.sti2.wsmf.core.ranking.
	 * QoSParamsEndpointRankingTable)
	 */
	@Override
	public int compareTo(IQoSParamsEndpointRankingTable o) {

		if (this.getSummedUpValues() <= o.getSummedUpValues()) {
			return 1;
		}
		if (this.getSummedUpValues() > o.getSummedUpValues()) {
			return -1;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.sti2.wsmf.core.ranking.asdfd#getName()
	 */
	@Override
	public String getName() {
		return this.endpointName;
	}

}
