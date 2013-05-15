package at.sti2.msee.ranking.webservice;

import java.util.List;

//TODO:extract into ws module
public interface IRankingWebService {
	
	/**
	 * @param qosRankingTemplate
	 * @param endpoints
	 * @return
	 * @throws Exception
	 */
	public List<String> getQoSRankedEndpoints(String[] key,
			Float[] preferenceValue, String[] endpoints) throws Exception;
}
