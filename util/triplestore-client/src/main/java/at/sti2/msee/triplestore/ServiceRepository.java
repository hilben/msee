package at.sti2.msee.triplestore;

import java.io.IOException;

import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.repository.RepositoryException;

public interface ServiceRepository {
	
	String insert(String msmRDF, String contextURI) throws ModelRuntimeException, IOException;	
	
	ServiceRepositoryConfiguration getServiceRepositoryConfiguration();
	
	void init() throws RepositoryException;
	void shutdown() throws RepositoryException;
	void clear();

	void insertModel(Model model, URI contextURI); //todo: check
	
	Model getModel();
	Model getModel(String contextURI);
	
}
