/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.util.triplestore;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

/**
 * @author Alex Oberhauser, Benjamin Hiltpolt
 * 
 */
public class RepositoryHandler {
	private final String serverEndpoint;
	private final String repositoryID;
	private Repository repository;
	private RepositoryConnection connection = null;
	private ValueFactory valueFactory = null;
	private boolean autocommit = false;

	public RepositoryHandler(String _serverEndpoint, String _repositoryID,
			boolean autocommit) throws FileNotFoundException, IOException {
		this.serverEndpoint = _serverEndpoint;
		this.repositoryID = _repositoryID;
		this.autocommit = autocommit;
	}

	private synchronized void init() throws RepositoryException {
		this.repository = new HTTPRepository(this.serverEndpoint,
				this.repositoryID);
		this.connection = this.repository.getConnection();
		this.valueFactory = this.connection.getValueFactory();
		if (this.autocommit) {
			this.connection.setAutoCommit(true);
		} else {
			this.connection.setAutoCommit(false);
		}
	}

	public synchronized void updateResourceTriple(String _subject,
			String _predicate, String _object, String _context)
			throws RepositoryException {
		this.delete(_subject, _predicate);
		this.addResourceTriple(_subject, _predicate, _object, _context);
	}

	public synchronized void updateLiteralTriple(String _subject,
			String _predicate, String _literalObject, String _context)
			throws RepositoryException {
		this.delete(_subject, _predicate);
		this.addLiteralTriple(_subject, _predicate, _literalObject, _context);
	}

	public synchronized TupleQueryResult selectSPARQL(String _query)
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException {
		if (this.connection == null)
			this.init();
		TupleQuery tupleQuery = this.connection.prepareTupleQuery(
				QueryLanguage.SPARQL, _query);
		return tupleQuery.evaluate();
	}

	public synchronized GraphQueryResult constructSPARQL(String _query)
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException {
		if (this.connection == null)
			this.init();
		GraphQuery tupleQuery = this.connection.prepareGraphQuery(
				QueryLanguage.SPARQL, _query);
		return tupleQuery.evaluate();
	}

	public synchronized void storeEntity(String _data, String _baseURI,
			RDFFormat _format, String _contextID) throws RepositoryException,
			RDFParseException, IOException {
		if (this.connection == null)
			this.init();
		URI contextURI = this.valueFactory.createURI(_contextID);
		this.connection.add(new ByteArrayInputStream(_data.getBytes()),
				_baseURI, _format, (Resource) contextURI);
	}

	public synchronized void addLiteralTriple(String _subject,
			String _property, String _value, String _context)
			throws RepositoryException {
		if (this.connection == null)
			this.init();
		Statement statement = this.valueFactory.createStatement(
				this.valueFactory.createURI(_subject),
				this.valueFactory.createURI(_property),
				this.valueFactory.createLiteral(_value),
				this.valueFactory.createURI(_context));
		this.connection.add(statement);
	}

	public void addResourceTriple(String _subject, String _property,
			String _value, String _context) throws RepositoryException {
		if (this.connection == null) {
			this.init();
		}
		Statement statement = this.valueFactory.createStatement(
				this.valueFactory.createURI(_subject),
				this.valueFactory.createURI(_property),
				this.valueFactory.createURI(_value),
				this.valueFactory.createURI(_context));
		this.connection.add(statement);
	}

	public void delete(String _subject, String _predicate)
			throws RepositoryException {
		if (this.connection == null) {
			this.init();
		}

		this.connection.remove(this.valueFactory.createURI(_subject),
				this.valueFactory.createURI(_predicate), null);
	}

	/**
	 * Deletes all statements in the context.
	 * 
	 * @param _contextID
	 *            The context URI.
	 * @throws RepositoryException
	 */
	public void deleteContext(String _contextID) throws RepositoryException {
		if (this.connection == null)
			this.init();
		URI contextURI = this.valueFactory.createURI(_contextID);
		this.connection.clear(contextURI);
	}
	
	public synchronized void clearContextAll() throws RepositoryException {
		if (this.connection == null)
			this.init();
		this.connection.clear();  // clears the complete context
	}

	public void commit() throws RepositoryException {
		if (this.connection != null) {
			this.connection.commit();
		}
	}
	
	public void rollback() throws RepositoryException {
		if (this.connection != null) {
			this.connection.rollback();
		}
	}

	/**
	 * Commit and shutdown connection
	 */
	public void shutdown() {
		if (this.connection != null && this.repository != null) {
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
