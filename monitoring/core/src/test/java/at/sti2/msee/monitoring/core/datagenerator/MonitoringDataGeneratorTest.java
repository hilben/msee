package at.sti2.msee.monitoring.core.datagenerator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.MonitoringParameterStoreHandler;

import static org.junit.Assert.*;

/**
 * JUnit Parameterized Test
 * 
 * 
 */
@RunWith(value = Parameterized.class)
public class MonitoringDataGeneratorTest {

	private MonitoringDataGenerator generator;

	public MonitoringDataGeneratorTest(MonitoringDataGeneratorParameters gen) {
		this.generator = new MonitoringDataGenerator(gen);
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = null;
		try {
			data = new Object[][] {
//					{ new MonitoringDataGeneratorParameters(new URL(
//							"http://www.example.com/testdataws1"), 25, 50,
//							1200, 50, 1200, 100, 1500, 0.05, new Date(), 10000) },
//					{ new MonitoringDataGeneratorParameters(new URL(
//							"http://www.example.com/testdataws2"), 25, 50,
//							1200, 50, 1200, 100, 1500, 0.05, new Date(), 10000) },
					{ new MonitoringDataGeneratorParameters(
							new URL(
									"http:///msee.sti2.at/services/7c52d54f-f0e1-4535-846d-3ba52c594847#getHotelName"),
							5, 50, 1200, 50, 1200, 100, 1500, 0.05,
							new Date(), 10000) }

			};
		} catch (MalformedURLException e) {
			fail();
		}
		return Arrays.asList(data);
	}

	@Test
	public void createDataTest() {

		try {
			this.generator.generateData(true);
			
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			e.printStackTrace();
			fail();
		}
	}

}