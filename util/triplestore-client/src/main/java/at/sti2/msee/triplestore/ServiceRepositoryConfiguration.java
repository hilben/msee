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
		return serverEndpoint;
	}

	public String getRepositoryID() {
		return repositoryId;
	}

	@Override
	public String toString() {
		return "ServiceRepositoryConfiguration [serverEndpoint="
				+ serverEndpoint + ", repositoryId=" + repositoryId
				+ "]";
	}

}
