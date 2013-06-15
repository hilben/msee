package at.sti2.msee.invocation.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class ServiceInvokerImplTest extends TestCase {
	protected static Logger logger = Logger.getLogger(ServiceInvokerImplTest.class);

	private ServiceInvocationImpl invoker;

	@Before
	public final void setUp() {
		invoker = new ServiceInvocationImpl(null);

		assertNotNull(invoker.getMonitoring());
	}

	@Test
	public final void testInvokeTestWebService1() {
		String endpoint = "http://sesa.sti2.at:8080/monitoring-testwebservices/services/big";
		String soapFile = "/test_monitoring.soap";

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(ServiceInvokerImplTest.class.getResource(soapFile).getFile());
		} catch (FileNotFoundException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
		String soapMessage = "";

		Scanner s = new Scanner(fis);
		while (s.hasNext()) {
			soapMessage += s.nextLine() + "\n";
		}

		s.close();
		try {
			fis.close();
		} catch (IOException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}

		try {
			invoker.getMonitoring().enableMonitoring(new URL(endpoint));
			invoker.invokeSOAP(endpoint, soapMessage);
		} catch (MalformedURLException | ServiceInvokerException | MonitoringException e) {
			if (!e.getLocalizedMessage().contains("(404)Not Found"))
				fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public final void testInvokeTestWebService2() {
		String endpoint = "http://msee.sti2.at/discovery-webservice/service";
		String soapFile = "/test_discover.soap";

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(ServiceInvokerImplTest.class.getResource(soapFile).getFile());
		} catch (FileNotFoundException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
		String soapMessage = "";

		Scanner s = new Scanner(fis);
		while (s.hasNext()) {
			soapMessage += s.nextLine() + "\n";
		}

		s.close();
		try {
			fis.close();
		} catch (IOException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}

		try {
			invoker.getMonitoring().enableMonitoring(new URL(endpoint));
			invoker.invokeSOAP(endpoint, soapMessage);
		} catch (MalformedURLException | ServiceInvokerException | MonitoringException e) {
			if (!e.getLocalizedMessage().contains("(404)Not Found"))
				fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public final void testInvokeFailure() {
		String endpoint = "http://www.test.com/noservice";
		String soapFile = "/test_monitoring.soap";

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(ServiceInvokerImplTest.class.getResource(soapFile).getFile());
		} catch (FileNotFoundException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
		String soapMessage = "";

		Scanner s = new Scanner(fis);
		while (s.hasNext()) {
			soapMessage += s.nextLine() + "\n";
		}

		s.close();
		try {
			fis.close();
		} catch (IOException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}

		boolean fails = false;
		try {
			try {
				invoker.getMonitoring().enableMonitoring(new URL(endpoint));
				invoker.invokeSOAP(endpoint, soapMessage);
			} catch (MalformedURLException | MonitoringException e) {
				fail(Arrays.toString(e.getStackTrace()));
			}
		} catch (ServiceInvokerException e) {
			fails = true;
		}

		assertTrue(fails);
	}

}
