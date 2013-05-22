package at.sti2.msee.ranking.repository;

import java.net.URL;

import at.sti2.msee.ranking.api.exception.RankingException;

public class RankingRepositoryHandler {
	
	
	private static RankingRepositoryHandler handler;
	
	private RankingRepositoryHandler() {
		
	}
	
	public static RankingRepositoryHandler getInstance() {
		if (handler == null) {
			handler = new RankingRepositoryHandler();
		}
		
		return handler;
	}
	
	
	public void setRulesForWebservice(URL webservice, String rules) throws RankingException {
		throw new RankingException("not implemented");
	}
	
	public String getRulesForWebservice(URL webservice) throws RankingException {
		throw new RankingException("not implemented");
	}

	
	
}
