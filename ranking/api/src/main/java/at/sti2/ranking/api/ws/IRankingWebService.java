package at.sti2.ranking.api.ws;

import java.util.List;

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
