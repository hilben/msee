package at.sti2.msee.triplestore.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.rdf2go.RepositoryModelFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;

import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.query.ModelQueryHelper;

public class SesameServiceRepositoryImpl implements ServiceRepository {
	Logger LOGGER = Logger.getLogger(this.getClass().getCanonicalName());

	private ServiceRepositoryConfiguration configuration = null;
	private Repository repository = null;

	public SesameServiceRepositoryImpl(
			ServiceRepositoryConfiguration serviceRepositoryConfiguration) {
		this.configuration = serviceRepositoryConfiguration;
	}

	@Override
	public void init() throws RepositoryException {
		if(this.repository!=null){
			return; // repository already initialized
		}
		RDF2Go.register(new RepositoryModelFactory());

		if (configuration.getServerEndpoint() == null) {
			this.repository = new SailRepository(new MSEEMemoryStore());
			LOGGER.info("In-memory respository was set");
		} else {
			this.repository = new HTTPRepository(
					configuration.getServerEndpoint(),
					configuration.getRepositoryID());
			((HTTPRepository) this.repository).setPreferredRDFFormat(RDFFormat.RDFXML);
			((HTTPRepository) this.repository).setPreferredTupleQueryResultFormat(TupleQueryResultFormat.SPARQL);
		}

		this.repository.initialize();
	}

	@Override
	public String insert(String msmRDF, String context)
			throws ModelRuntimeException, IOException {
		Model msmModel = RDF2Go.getModelFactory().createModel();
		msmModel.open();
		StringReader reader = new StringReader(msmRDF);
		msmModel.readFrom(reader);

		URI contextURI = null;

		if (context != null)
			contextURI = msmModel.createURI(context);

		return this.insert(msmModel, contextURI);
	}

	private String insert(Model msmModel, URI contextURI) {
		Model repositoryModel = new RepositoryModel(contextURI, repository);
		repositoryModel.open();
		repositoryModel.addModel(msmModel);		
		this.fillNamespaces(repositoryModel);
		repositoryModel.close();

		List<URI> services = ModelQueryHelper.findServices(msmModel);
		return services.get(0).toString();
	}
	
	public void insertModel(Model model, URI contextURI) {
		Model repositoryModel = new RepositoryModel(contextURI, repository);
		repositoryModel.open();
		repositoryModel.addModel(model);		
		this.fillNamespaces(repositoryModel);
		repositoryModel.close();
	}

	private void fillNamespaces(Model repositoryModel) {
		repositoryModel.setNamespace("msm", "http://cms-wg.sti2.org/ns/minimal-service-model#");
	}

	@Override
	public void shutdown() throws RepositoryException {
		if (this.repository != null)
			this.repository.shutDown();
	}

	@Override
	public ServiceRepositoryConfiguration getServiceRepositoryConfiguration() {
		return this.configuration;
	}

	@Override
	public Model getModel() {
		Model repositoryModel = new RepositoryModel(repository);
		return repositoryModel;
	}

	@Override
	public Model getModel(String context) {
		URI contextURI = createURI(context);

		Model repositoryModel = new RepositoryModel(contextURI, repository);
		return repositoryModel;
	}

	@Override
	public void clear() {
		try {
			RepositoryConnection connection = this.repository.getConnection();
			connection.clear();
			connection.commit();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	private URI createURI(String context) {

		if (context == null)
			return null;

		Model model = RDF2Go.getModelFactory().createModel();
		model.open();
		URI contextURI = model.createURI(context);
		model.close();
		return contextURI;
	}
	
	/**
	 * TODO: added because not supported in rdf2go
	 * @throws RepositoryException
	 * @throws MalformedQueryException 
	 * @throws UpdateExecutionException 
	 */
	public void performSPARQLUpdate(String updateQuery) throws RepositoryException, MalformedQueryException, UpdateExecutionException {
		RepositoryConnection c = this.repository.getConnection();
		c.setAutoCommit(false);
		
		Update update = c.prepareUpdate(QueryLanguage.SPARQL, updateQuery);
	
		update.execute();
		
		c.commit();
		c.close();
	
	}
}
