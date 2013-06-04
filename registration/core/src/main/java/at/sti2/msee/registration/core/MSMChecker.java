package at.sti2.msee.registration.core;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.TriplePattern;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.openrdf.rdf2go.RepositoryModelFactory;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;

/**
 * This class checks a {@link MSM} model for necessary nodes.
 * 
 * @author Christian Mayr
 * 
 */
public class MSMChecker {

	private Logger LOGGER = Logger.getLogger(this.getClass().getCanonicalName());

	private String msm;
	private Model msmModel;
	private List<Resource> services = new ArrayList<Resource>();
	private List<Resource> operations = null;

	public MSMChecker() {
	}

	public MSMChecker(String msm) {
		this.msm = msm;
	}

	public void setMSM(String msm) {
		this.msm = msm;
	}

	public String check() throws ServiceRegistrationException {
		if (msm == null || msm.length() == 0) {
			throw new ServiceRegistrationException();
		}
		setup();
		checkModel();
		tearDown();
		return msm;
	}

	/**
	 * Public method iterates through the model in MSM format.
	 * 
	 * @throws ServiceRegistrationException
	 */
	private void checkModel() throws ServiceRegistrationException {
		checkForServices();
		checkForCategories();
		checkForOperations();
		// TODO extend
	}

	private void checkForServices() throws ServiceRegistrationException {
		TriplePattern pattern = msmModel.createTriplePattern(Variable.ANY, RDF.type, MSM.Service);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		if (statements.hasNext()) {
			while (statements.hasNext()) {
				Statement statement = statements.next();
				services.add(statement.getSubject());
			}
		} else { // if no service => also no operation
			if (operations == null) {
				checkForOperations();
			}
			if (operations.size() > 0) {
				throwIt("No service found in the model");
			}
		}
	}

	private void checkForCategories() throws ServiceRegistrationException {
		for (Resource service : services) {
			TriplePattern pattern = msmModel.createTriplePattern(service, MSM.modelReference,
					Variable.ANY);
			ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
			if (statements.hasNext()) {
				while (statements.hasNext()) {
					Statement statement = statements.next();
					statement.getObject().asResource();
				}
			} else {
				if (operations == null) {
					checkForOperations();
				}
				if (operations.size() != 0) {
					throwIt("No category found in the model");
				}
			}
		}
	}

	private void checkForOperations() throws ServiceRegistrationException {
		if (operations != null) {
			return; // method already called
		}
		operations = new ArrayList<Resource>();
		TriplePattern pattern = msmModel.createTriplePattern(Variable.ANY, RDF.type, MSM.Operation);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		if (statements.hasNext()) {
			while (statements.hasNext()) {
				Statement statement = statements.next();
				operations.add(statement.getSubject());
			}
		} else {
			throwIt("No operation found in the model");
		}

	}

	private void setup() {
		Syntax syntax = Syntax.RdfXml;
		StringReader reader = new StringReader(msm);
		RDF2Go.register(new RepositoryModelFactory());
		msmModel = RDF2Go.getModelFactory().createModel();
		msmModel.open();
		try {
			msmModel.readFrom(reader, syntax);
		} catch (ModelRuntimeException | IOException e) {
			LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	private void tearDown() {
		msmModel.close();
	}

	private void throwIt(String errorMessage) throws ServiceRegistrationException {
		LOGGER.warning(errorMessage);
		throw new ServiceRegistrationException(errorMessage);
	}

}
