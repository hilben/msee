package at.sti2.util.triplestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.UnsupportedRDFormatException;

/**
 * @author Alex Oberhauser
 */
public class RepositoryHandler {
	private final String serverEndpoint;
	private final String repositoryID;

	private Repository repository;
	private RepositoryConnection connection = null;
	private ValueFactory valueFactory = null;

	public RepositoryHandler(String _serverEndpoint, String _repositoryID) throws FileNotFoundException, IOException {
		this.serverEndpoint = _serverEndpoint;
		this.repositoryID = _repositoryID;
	}
	
	public ValueFactory getValueFactory() { return this.valueFactory; }
	
	public URI createURI(String _uri) {
		return this.valueFactory.createURI(_uri);
	}
	
	public Statement createStatement(URI _subject, URI _predicate, URI _object) {
		return this.valueFactory.createStatement(_subject, _predicate, _object);
	}
	
	public Statement createStatement(URI _subject, URI _predicate, Literal _object) {
		return this.valueFactory.createStatement(_subject, _predicate, _object);
	}
	
	public Literal createLiteral(String _literal) {
		return this.valueFactory.createLiteral(_literal);
	}

	private void init() throws RepositoryException {
		this.repository = new HTTPRepository(this.serverEndpoint, this.repositoryID);
		this.connection = this.repository.getConnection();
		this.valueFactory = this.connection.getValueFactory();
	}

	public TupleQueryResult selectSPARQL(String _query) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		if ( this.connection == null )
			this.init();
		TupleQuery tupleQuery = this.connection.prepareTupleQuery(QueryLanguage.SPARQL, _query);
		return tupleQuery.evaluate();
	}
	
	public void selectSPARQL(String _query, TupleQueryResultHandler _resultHandler) throws QueryEvaluationException, RepositoryException, MalformedQueryException, TupleQueryResultHandlerException {
		if ( this.connection == null )
			this.init();
		TupleQuery tupleQuery = this.connection.prepareTupleQuery(QueryLanguage.SPARQL, _query);
		tupleQuery.evaluate(_resultHandler);
	}

	public boolean askSPARQL(String _query) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		if ( this.connection == null )
			this.init();
		BooleanQuery booleanQuery = this.connection.prepareBooleanQuery(QueryLanguage.SPARQL, _query);
		return booleanQuery.evaluate();
	}
	
	/**
	 * @param _query
	 * @return
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 * @throws QueryEvaluationException 
	 * @throws IOException 
	 * @throws UnsupportedRDFormatException 
	 * @throws RDFHandlerException 
	 */
	public String describeQuery(String _query, RDFFormat _rdfFormat) throws RepositoryException, MalformedQueryException, QueryEvaluationException, RDFHandlerException, UnsupportedRDFormatException, IOException {
		if ( this.connection == null )
			this.init();
		GraphQuery graphQuery = this.connection.prepareGraphQuery(QueryLanguage.SPARQL, _query);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(graphQuery.evaluate(), _rdfFormat, out);
		return out.toString();
	}

	public String describeEntity(String _droneID, RDFFormat _rdfFormat) throws RepositoryException, MalformedQueryException, QueryEvaluationException, RDFHandlerException, UnsupportedRDFormatException, IOException {
		if ( this.connection == null )
			this.init();
		String query = "DESCRIBE <" + _droneID + ">";
		GraphQuery graphQuery = this.connection.prepareGraphQuery(QueryLanguage.SPARQL, query);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(graphQuery.evaluate(), _rdfFormat, out);
		return out.toString();
	}
	
	public String describeEntityProperty(String _droneID, RDFFormat _rdfFormat, String _property) throws RDFHandlerException, UnsupportedRDFormatException, QueryEvaluationException, IOException, RepositoryException, MalformedQueryException {
		if ( this.connection == null )
			this.init();
		String query = QueryHelper.getNamespacePrefix() + " DESCRIBE ?x WHERE { <" + _droneID + "> " + _property + " ?x . }";
		GraphQuery graphQuery = this.connection.prepareGraphQuery(QueryLanguage.SPARQL, query);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(graphQuery.evaluate(), _rdfFormat, out);
		return out.toString();
	}

	public void storeEntity(String _data, String _baseURI, RDFFormat _format, String _contextID) throws RepositoryException, RDFParseException, IOException {
		if ( this.connection == null )
			this.init();
		URI contextURI = this.valueFactory.createURI(_contextID);
		this.connection.add(new ByteArrayInputStream(_data.getBytes()), _baseURI, _format, (Resource)contextURI);
	}
	
	public void add(Statement _stmt, String _contextID) throws RepositoryException {
		this.connection.add(_stmt, this.valueFactory.createURI(_contextID));
	}

//	public void updateEntity(String _subject, String _data, String _baseURI, RDFFormat _format, String _contextID) throws RepositoryException, RDFParseException, IOException {
//		if ( this.connection == null )
//			this.init();
//		this.delete(_subject);
//		this.storeEntity(_data, _baseURI, _format, _contextID);
//	}

	public void addLiteralTriple(String _subject, String _property, String _value) throws RepositoryException {
		if ( this.connection == null )
			this.init();
		Statement statement = this.valueFactory.createStatement(this.valueFactory.createURI(_subject),
				this.valueFactory.createURI(_property), this.valueFactory.createLiteral(_value));
		this.connection.add(statement);
	}
	
	public void addResourceTriple(String _subject, String _property, String _value, String _context) throws RepositoryException {
		if ( this.connection == null )
			this.init();
		Statement statement = this.valueFactory.createStatement(this.valueFactory.createURI(_subject),
				this.valueFactory.createURI(_property), this.valueFactory.createURI(_value),
				this.valueFactory.createURI(_context));
		this.connection.add(statement);
	}

	public void updateProperty(String _subject, String _property, String _value) throws RepositoryException {
		if ( this.connection == null )
			this.init();
		this.delete(_subject, _property);
		this.addLiteralTriple(_subject, _property, _value);
	}

	public void delete(String _subject, String _predicate) throws RepositoryException {
		if ( this.connection == null )
			this.init();
		this.connection.remove(this.valueFactory.createURI(_subject), this.valueFactory.createURI(_predicate), null);
	}
//
//	public void delete(String _subject) throws RepositoryException {
//		if ( this.connection == null )
//			this.init();
//		this.connection.remove(this.valueFactory.createURI(_subject), null, null);
//	}
	
	/**
	 * Deletes all statements in the context.
	 * 
	 * @param _contextID The context URI.
	 * @throws RepositoryException
	 */
	public void deleteContext(String _contextID) throws RepositoryException {
		if ( this.connection == null )
			this.init();
		URI contextURI = this.valueFactory.createURI(_contextID);
		this.connection.clear(contextURI);
	}

	public void commit() {
			try {
				if ( this.connection != null )
					this.connection.commit();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void shutdown() {
		if ( this.connection != null && this.repository != null ) {
			try {
				this.connection.commit();
				this.connection.close();
				this.repository.shutDown();
			} catch (RepositoryException e) {
				e.printStackTrace();
			} finally {
				this.connection = null;
				this.repository = null;
			}
		}
	}
	
}
