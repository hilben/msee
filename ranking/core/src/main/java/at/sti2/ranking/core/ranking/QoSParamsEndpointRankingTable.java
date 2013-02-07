/**
 * 
 */
package at.sti2.ranking.core.ranking;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.ranking.api.data.qos.ranking.IQoSParamsEndpointRankingTable;
import at.sti2.ranking.api.data.qos.ranking.QoSRankingPreferencesTemplate;
import at.sti2.wsmf.core.PersistentHandler;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 * A table to store all ranked QoSParams of an endpoint.
 * 
 */
public class QoSParamsEndpointRankingTable implements IQoSParamsEndpointRankingTable{

	
	private static Logger logger = Logger.getLogger(QoSParamsEndpointRankingTable.class);
	
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
			QoSRankingPreferencesTemplate template)  {
		this.endpointName = endpointName;

		for (String s : template.getQoSParams()) {
			this.propIdentifiersOrderingValues.put(s, new Float(Float.NaN));
		}
	}


	/* (non-Javadoc)
	 * @see at.sti2.wsmf.core.ranking.asdfd#getRankingValueForProperty(at.sti2.wsmf.api.data.qos.QoSParamKey)
	 */
	@Override
	public Float getRankingValueForProperty(String property) {
		return this.propIdentifiersOrderingValues.get(property);
	}

	/* (non-Javadoc)
	 * @see at.sti2.wsmf.core.ranking.asdfd#setRankingValueForProperty(at.sti2.wsmf.api.data.qos.QoSParamKey, java.lang.Float)
	 */
	@Override
	public void setRankingValueForProperty(String property, Float value)
			throws Exception {
		if (this.propIdentifiersOrderingValues.containsKey(property)) {
			this.propIdentifiersOrderingValues.put(property, value);
		} else {
			throw new Exception(); // TODO:
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see at.sti2.wsmf.core.ranking.asdfd#retrieveQoSParamValues()
	 */
	@Override
	public void retrieveQoSParamValues() {
		
		PersistentHandler persistentHandler = null;
		try {
			persistentHandler = PersistentHandler.getInstance();
		} catch (FileNotFoundException e2) {
			logger.error(e2.getCause());
		} catch (IOException e2) {
			logger.error(e2.getCause());
		}
		
		for (String key : this.getQoSParams()) {
			try {
				
				//handle invalid values or QoS for which no values were determined
				float value = Float
						.parseFloat(persistentHandler.getQoSValue(this.endpointName,
								key));
				
				this.setRankingValueForProperty(key, value);
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
	private Set<String> getQoSParams() {
		return this.propIdentifiersOrderingValues.keySet();
	}


	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ret = "";

		ret += "#####################################################"+System.getProperty("line.separator");

		ret += this.endpointName + "\n";

		for (String s : this.propIdentifiersOrderingValues.keySet()) {
			ret += s + ": " + this.getRankingValueForProperty(s) +System.getProperty("line.separator");
		}
		ret += "\n";

		ret += "Summed up: " + this.getSummedUpValues() +System.getProperty("line.separator");

		ret += "#####################################################"+System.getProperty("line.separator");

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/* (non-Javadoc)
	 * @see at.sti2.wsmf.core.ranking.asdfd#compareTo(at.sti2.wsmf.core.ranking.QoSParamsEndpointRankingTable)
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

	/* (non-Javadoc)
	 * @see at.sti2.wsmf.core.ranking.asdfd#getName()
	 */
	@Override
	public String getName() {
		return this.endpointName;
	}



}
