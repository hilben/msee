/**
 * 
 */
package at.sti2.ranking.api.data.qos.ranking;


/**
 * @author Benjamin Hiltpolt
 *
 */
public interface IQoSParamsEndpointRankingTable extends Comparable<IQoSParamsEndpointRankingTable>{
	/**
	 * 
	 * Get the ordering value of a QoS
	 * @param property
	 * @return
	 */
	public abstract Float getRankingValueForProperty(String property);

	/**
	 * Sets the ranking value of a QosParam
	 * 
	 * @param property
	 * @param value
	 * @throws Exception
	 */
	public abstract void setRankingValueForProperty(String property,
			Float value) throws Exception;

	/**
	 * 
	 *  Retrieves the Values of its ranked QoSParams of its endpoint
	 */
	public abstract void retrieveQoSParamValues();

	/**
	 * 
	 * Sum up all ordering values of the table
	 * @return
	 */
	public abstract float getSummedUpValues();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public abstract int compareTo(IQoSParamsEndpointRankingTable o);

	/**
	 * @return
	 */
	public abstract String getName();
}
