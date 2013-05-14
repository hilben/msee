package at.sti2.msee.discovery.core.other;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.ontoware.aifbcommons.collection.ClosableIterable;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Node;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

@RunWith(Parameterized.class)
public class RDF2GODiscoverTest {
	static ServiceRepository serviceRepository = null;
	private String file;
	private String expectedContainedText;
	private boolean passed;

	public RDF2GODiscoverTest(String file, String containedText, boolean passed) {
		this.file = file;
		this.expectedContainedText = containedText;
		this.passed = passed;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { "HelloService.sawsdl", "#wsdl", true },
				{ "HelloService.sawsdl", "#wsdl.service(helloService)", true },
				{ "HelloService.sawsdl", "#wsdl.service(helloService)/xxxxx", false },
				{ "hrests1.html", "asfdasa dfsdaf ", false }, { "hrests1.html", "#svc", true } };
		return Arrays.asList(data);
	}

	@BeforeClass
	public static void setup() throws IOException, RepositoryException,
			ServiceRegistrationException {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();

	}

	@AfterClass
	public static void teardown() throws RepositoryException {
		//serviceRepository.shutdown();
	}

	@After
	public void clear() {
		serviceRepository.getModel().close();
		serviceRepository.clear();
	}

	@Test
	public void testDiscover() throws ServiceRegistrationException {
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);
		String serviceDescriptionURL = RDF2GODiscoverTest.class.getResource(
				"/services/" + this.file).toString();
		registration.register(serviceDescriptionURL);

		Model msmModel = serviceRepository.getModel();
		msmModel.open();
		DiscoveryQueryBuilder queryBuilder = new DiscoveryQueryBuilder();
		String[] _categoryList = { "http://msee.sti2.at/categories#business" };

		ClosableIterable<Statement> statementsss = msmModel.sparqlConstruct(queryBuilder
				.getDiscoverQuery2Args(_categoryList));
		ClosableIterator<Statement> statements = statementsss.iterator();
		boolean isContained = false;
		while (statements.hasNext()) {
			Statement statement = statements.next();
			Node service = statement.getSubject();
			if (service.toString().toLowerCase().contains(expectedContainedText.toLowerCase())) {
				isContained = true;
				break;
			}
		}
		if (passed) {
			assertTrue(isContained);
		} else {
			assertFalse(isContained);
		}
		msmModel.close();
	}
}
