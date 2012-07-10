package at.sti2.ngsee.management.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

import at.sti2.ngsee.management.api.exception.ManagementException;
import at.sti2.ngsee.management.common.Config;
import at.sti2.util.triplestore.RepositoryHandler;

public abstract class ServiceManagement {	
	private static RepositoryHandler reposHandler;
	
	public static String add(String _ontologyURL) throws ManagementException {
		// Initialising the repository
		ServiceManagement.initRepo();
		
		//Read data from URL into string buffer
		URL url;
		try {
			url = new URL(_ontologyURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			StringBuffer content = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				content.append(inputLine);
			in.close();		
			
			//Store the data into repository and it shutdown.
			reposHandler.storeEntity(content.toString(), _ontologyURL, RDFFormat.RDFXML, _ontologyURL);
			reposHandler.shutdown();			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RDFParseException e) {
			e.printStackTrace();
		}		
		return _ontologyURL;
	}
	
	public static boolean delete(String _ontologyURL) throws ManagementException{
		// Initialising the repository
		ServiceManagement.initRepo();
		try {
			//Delete the graph from repository and it shutdown
			reposHandler.deleteContext(_ontologyURL);
			reposHandler.shutdown();
			
			return true;			
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String update(String _oldOntologyURL, String _newOntologyURL) throws ManagementException {
		// Initialising the repository
		ServiceManagement.initRepo();
		
		//Update through delete and add functionality
		delete(_oldOntologyURL);
		add(_newOntologyURL);
		return _newOntologyURL;
	}
	
	/**
	 * Initialising the repository needed for all the above methods.
	 * @throws ManagementException
	 */
	private static void initRepo() throws ManagementException {
		try {
			Config cfg = new Config();
			reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		} catch (IOException e) {
			throw new ManagementException("The repository endpoint ID or the WSDL file could NOT be found.", e.getCause());
		}
	}
}
