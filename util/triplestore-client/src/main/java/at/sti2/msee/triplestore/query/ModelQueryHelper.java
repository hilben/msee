package at.sti2.msee.triplestore.query;

import java.util.ArrayList;
import java.util.List;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.TriplePattern;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.vocabulary.RDF;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

public class ModelQueryHelper {

	public static List<URI> findServices(Model msmModel) 
	{
		TriplePattern pattern = msmModel.createTriplePattern(Variable.ANY, RDF.type , MSM.Service);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		List<URI> serviceURIs = new ArrayList<URI>();
		
		while(statements.hasNext())
		{
			Statement statement = statements.next();
			Resource serviceResource = statement.getSubject();
			serviceURIs.add(serviceResource.asURI());
		}
		
		return serviceURIs;		
	}
}