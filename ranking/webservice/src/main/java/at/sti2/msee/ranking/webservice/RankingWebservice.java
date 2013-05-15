package at.sti2.msee.ranking.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import at.sti2.msee.ranking.core.QoSRankingEngine;
import at.sti2.msee.ranking.webservice.IRankingWebService;

public class RankingWebservice implements IRankingWebService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSRankedEndpoint(
	 * QoSRankingPreferencesTemplate, String[])
	 */
	// TODO: put in other class
	// TODO: logging
	// TODO: testing
	@WebMethod
	public List<String> getQoSRankedEndpoints(@WebParam(name="QoSParamKeys")String[] keys,
			@WebParam(name = "preferenceValues")Float[] preferenceValues, @WebParam(name = "endpoints")String[] endpoints) throws Exception {
		return QoSRankingEngine.getQoSRankedEndpoints(keys, preferenceValues,
				endpoints);

	}
}
