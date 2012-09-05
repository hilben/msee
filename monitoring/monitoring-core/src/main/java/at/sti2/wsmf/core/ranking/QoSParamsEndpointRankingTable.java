/**
 * 
 */
package at.sti2.wsmf.core.ranking;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.core.PersistentHandler;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 * A table to store all ranked QoSParams of an endpoint.
 * 
 */
public class QoSParamsEndpointRankingTable implements
		Comparable<QoSParamsEndpointRankingTable> {

	
	private static Logger logger = Logger.getLogger(QoSParamsEndpointRankingTable.class);
	
	private String endpointName;
	private Map<QoSParamKey, Float> propIdentifiersOrderingValues = new HashMap<QoSParamKey, Float>();

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

		for (QoSParamKey s : template.getQoSParams()) {
			this.propIdentifiersOrderingValues.put(s, new Float(Float.NaN));
		}
	}


	/**
	 * 
	 * Get the ordering value of a QoS
	 * @param property
	 * @return
	 */
	public Float getRankingValueForProperty(QoSParamKey property) {
		return this.propIdentifiersOrderingValues.get(property);
	}

	/**
	 * Sets the ranking value of a QosParam
	 * 
	 * @param property
	 * @param value
	 * @throws Exception
	 */
	public void setRankingValueForProperty(QoSParamKey property, Float value)
			throws Exception {
		if (this.propIdentifiersOrderingValues.containsKey(property)) {
			this.propIdentifiersOrderingValues.put(property, value);
		} else {
			throw new Exception(); // TODO:
		}
	}
	
	
	
	/**
	 * 
	 *  Retrieves the Values of its ranked QoSParams of its endpoint
	 */
	public void retrieveQoSParamValues() {
		
		PersistentHandler persistentHandler = null;
		try {
			persistentHandler = PersistentHandler.getInstance();
		} catch (FileNotFoundException e2) {
			logger.error(e2.getCause());
		} catch (IOException e2) {
			logger.error(e2.getCause());
		}
		
		for (QoSParamKey key : this.getQoSParams()) {
			try {
				this.setRankingValueForProperty(key, Float
						.parseFloat(persistentHandler.getQoSValue(this.endpointName,
								key)));
			} catch (NumberFormatException e1) {
				logger.error(e1.getCause());
			} catch (QueryEvaluationException e1) {
				logger.error(e1.getCause());
			} catch (RepositoryException e1) {
				logger.error(e1.getCause());
			} catch (MalformedQueryException e1) {
				logger.error(e1.getCause());
			} catch (MalformedURLException e1) {
				logger.error(e1.getCause());
			} catch (Exception e) {
				logger.error(e.getCause());
			}
		}
	}

	/**
	 * Returns all QoSParamKeys
	 * @return
	 */
	private Set<QoSParamKey> getQoSParams() {
		return this.propIdentifiersOrderingValues.keySet();
	}


	/**
	 * 
	 * Sum up all ordering values of the table
	 * @return
	 */
	public float getSummedUpValues() {
		float retVal = 0;
		for (QoSParamKey s : this.propIdentifiersOrderingValues.keySet()) {
			retVal += this.propIdentifiersOrderingValues.get(s);
		}

		return retVal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ret = "";

		ret += "#####################################################\n";

		ret += this.endpointName + "\n";

		for (QoSParamKey s : this.propIdentifiersOrderingValues.keySet()) {
			ret += s + ": " + this.getRankingValueForProperty(s) + "\n";
		}
		ret += "\n";

		ret += "Summed up: " + this.getSummedUpValues() + "\n";

		ret += "#####################################################\n";

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(QoSParamsEndpointRankingTable o) {

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
