package at.sti2.msee.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This global configuration singleton instance loads the configuration from the
 * properties file. For Junit tests in other modules place the file from this
 * module's test resources folder named "default.properties" into
 * src/test/resources of the specific module.
 * 
 * @author Christian Mayr
 * 
 */
public enum Config {
	INSTANCE;
	private Properties properties = new Properties();
	private String repositoryEndpoint;
	private String repositoryID;
	private String monitoringRepositoryEndpoint;
	private String monitoringRepositoryID;
	private String monitoringInstancePrefix;
	private String rankingRepositoryEndpoint;
	private String rankingRepositoryID;
	private String rankingInstancePrefix;

	private Config() {
		try (InputStream in = Config.class.getResourceAsStream("/default.properties")) {
			properties.load(in);
			repositoryEndpoint = properties.getProperty("repository.sesame.endpoint");
			repositoryID = properties.getProperty("repository.sesame.reposid");
			monitoringRepositoryEndpoint = properties
					.getProperty("monitoring.triplestore.endpoint");
			monitoringRepositoryID = properties.getProperty("monitoring.triplestore.reposid");
			monitoringInstancePrefix = properties.getProperty("monitoring.instance.prefixuri");
			rankingRepositoryEndpoint = properties.getProperty("ranking.triplestore.endpoint");
			rankingRepositoryID = properties.getProperty("ranking.triplestore.reposid");
			rankingInstancePrefix = properties.getProperty("ranking.instance.prefixuri");
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getRepositoryEndpoint() {
		return repositoryEndpoint;
	}

	public String getRepositoryID() {
		return repositoryID;
	}

	public String getMonitoringRepositoryEndpoint() {
		return monitoringRepositoryEndpoint;
	}

	public String getMonitoringRepositoryID() {
		return monitoringRepositoryID;
	}

	public String getMonitoringInstancePrefix() {
		return monitoringInstancePrefix;
	}

	public String getRankingRepositoryEndpoint() {
		return rankingRepositoryEndpoint;
	}

	public String getRankingRepositoryID() {
		return rankingRepositoryID;
	}

	public String getRankingInstancePrefix() {
		return rankingInstancePrefix;
	}

	public static void main(String[] a) {
		Config config = Config.INSTANCE;
		config.getRepositoryEndpoint();
		config.getRepositoryID();
	}

}