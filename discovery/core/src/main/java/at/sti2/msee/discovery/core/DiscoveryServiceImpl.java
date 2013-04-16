package at.sti2.msee.discovery.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.wsdl.Output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ontoware.aifbcommons.collection.ClosableIterable;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
import at.sti2.msee.triplestore.ServiceRepository;

public class DiscoveryServiceImpl implements Discovery {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());
	private ServiceRepository serviceRepository = null;
	private DiscoveryQueryBuilder discoveryQueryBuilder = null;

	public DiscoveryServiceImpl(ServiceRepository serviceRepository) {
		if(serviceRepository == null) {
			throw new IllegalArgumentException("Repository cannot be null");
		}
		this.serviceRepository = serviceRepository;
		discoveryQueryBuilder = new DiscoveryQueryBuilder();
	}

	@Override
	public String discover(String[] categoryList) throws DiscoveryException {
		LOGGER.debug("Starting discover()");

		checkCategoryList(categoryList);

		String query = discoveryQueryBuilder.getDiscoverQuery2Args(categoryList);

		Model rdfModel = serviceRepository.getModel();
		if(!rdfModel.isOpen()) {
			rdfModel.open();
		}
		ClosableIterable<Statement> resultTable = rdfModel.sparqlConstruct(query);
		ClosableIterator<Statement> results = resultTable.iterator();

		String rdfxml = convertQueryResult2RDFXML(results);
		rdfModel.close();
		return rdfxml;
	}

	private String convertQueryResult2RDFXML(ClosableIterator<Statement> results) {
		OutputStream out = new ByteArrayOutputStream();
		RDFHandler rdfxmlWriter = new RDFXMLWriter(out);

		try {
			rdfxmlWriter.startRDF();
			while (results.hasNext()) {
				Statement statement = results.next();
				Resource s = new BNodeImpl(statement.getSubject().toString());
				URI p = new URIImpl(statement.getPredicate().toString());
				Resource o = new BNodeImpl(statement.getObject().toString());
				rdfxmlWriter.handleStatement(new StatementImpl(s, p, o));
			}
			rdfxmlWriter.endRDF();
		} catch (RDFHandlerException e) {
			try {
				throw new DiscoveryException("Converting had some error");
			} catch (DiscoveryException e1) {
			}
		}
		return out.toString();
	}

	private void checkCategoryList(String[] categoryList) {
		if (categoryList == null) {
			LOGGER.error("Category list is null");
			throw new NullPointerException("Category list is null");
		}
		if (categoryList.length < 1) {
			LOGGER.error("Category list is empty");
			throw new IllegalArgumentException("Category list is empty");
		}
	}

	@Override
	public String discoverAdvanced(String[] categoryList,
			String[] inputParamList, String[] outputParamList)
			throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	@Override
	public String lookup(String namespace, String operationName)
			throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	@Override
	public String getIServeModel(String serviceID) throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

}
