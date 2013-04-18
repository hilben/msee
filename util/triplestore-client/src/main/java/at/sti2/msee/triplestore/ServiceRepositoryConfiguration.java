package at.sti2.msee.triplestore;

public class ServiceRepositoryConfiguration {

	private String serverEndpoint = null;
	private String repositoryId = null;

	public void setServerEndpoint(String serverEndpoint) {
		this.serverEndpoint = serverEndpoint;
	}

	public void setRepositoryID(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getServerEndpoint() {
		if(serverEndpoint==null){
			throw new IllegalArgumentException("Server Endpoint is NULL");
		}
		return serverEndpoint;
	}

	public String getRepositoryID() {
		if(repositoryId==null){
			throw new IllegalArgumentException("Repository ID is NULL");
		}
		return repositoryId;
	}

}
