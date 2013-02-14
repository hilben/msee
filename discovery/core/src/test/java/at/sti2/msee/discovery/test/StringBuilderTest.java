package at.sti2.msee.discovery.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.msee.discovery.core.ServiceDiscovery;

public class StringBuilderTest {
	@Test
	@Ignore
	public void teststringtemplate() throws URISyntaxException,
			FileNotFoundException, QueryEvaluationException,
			RepositoryException, MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException, IOException {
		List<URI> uris = new ArrayList<URI>();
		uris.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#Maritime"));
		ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
		System.out.println(serviceDiscovery.discover(uris, RDFFormat.N3));
	}
}
