package at.sti2.msee.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import at.sti2.msee.model.xmlparser.ServiceModelFormatParser;

/**
 * This class detects the service model format of a given file or URL.
 * 
 */
public class ServiceModelFormatDetector {

	/**
	 * URI that holds the file to be tested
	 */
	private URI serviceDescriptionURI = null;

	/**
	 * The implementation of the SAX parser
	 */
	private ServiceModelFormatParser modelParser = null;

	/**
	 * Set of elements returned from the SAX parser
	 */
	private Set<String> elementSet = null;

	/**
	 * 
	 */

	/**
	 * Main method that checks the given URI for a valid service model format
	 * and returns the format, e.g., MSM, SAWSDL, HRESTS.
	 * 
	 * @param serviceDescriptionURI
	 * @return service model format
	 */
	public ServiceModelFormat detect(URI serviceDescriptionURI) {
		if (serviceDescriptionURI == null) {
			throw new NullPointerException("Service description is NULL");
		}
		this.serviceDescriptionURI = serviceDescriptionURI;
		return detectInternal();
	}

	public ServiceModelFormat detect(URL serviceDescriptionURL) {
		URI uri = null;
		try {
			uri = serviceDescriptionURL.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return detect(uri);
	}

	/**
	 * Helper method for public visible main method that prepares the parser and
	 * loads the parsed XML elements into a data set.
	 */
	private ServiceModelFormat detectInternal() {
		String input = "";
		try (InputStream inputStream = prepareInput(serviceDescriptionURI)) {
			input = inputStreamToString(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		modelParser = new ServiceModelFormatParser(input);
		elementSet = modelParser.parseElements();
		if (isHrests()) {
			return ServiceModelFormat.HRESTS;
		} else if (isMsm()) {
			return ServiceModelFormat.MSM;
		} else if (isSawsdl()) {
			return ServiceModelFormat.SAWSDL;
		}
		return ServiceModelFormat.UNKNOWN;
	}

	/**
	 * Method that checks for the format SAWSDL
	 * 
	 * @return true if file is SAWSDL
	 */
	private boolean isSawsdl() {
		// wsdl 2.0
		String[] contains = { "http://www.w3.org/ns/wsdl", "http://www.w3.org/ns/sawsdl" };
		if (containsAll(elementSet.toString(), contains)) {
			return true;
		} else {
			// maybe old wsdl
			String[] contains2 = { "http://schemas.xmlsoap.org/wsdl/",
					"http://www.w3.org/ns/sawsdl" };
			return containsAll(elementSet.toString(), contains2);
		}
	}

	/**
	 * Method that checks for the format HRESTS
	 * 
	 * @return true if file is HRESTS
	 */
	private boolean isHrests() {
		String[] possibleList = { "div", "code", "span" };
		String[] invalidList = { "wsdl", "script",
				"http://cms-wg.sti2.org/ns/minimal-service-model", "http://www.w3.org/ns/wsdl",
				"http://www.w3.org/ns/sawsdl" };
		boolean isHrests = false;

		Iterator<String> ite = elementSet.iterator();
		while (ite.hasNext()) {
			String element = ite.next().toLowerCase();
			if (Arrays.asList(possibleList).contains(element)) {
				isHrests = true;
			}
			if (Arrays.asList(invalidList).contains(element)) {
				return false;
			}
		}
		return isHrests;
	}

	/**
	 * Method that checks for the format MSM
	 * 
	 * @return true if file is MSM
	 */
	private boolean isMsm() {
		String[] contains = { "http://cms-wg.sti2.org/ns/minimal-service-model" };
		return containsAll(elementSet.toString(), contains);
	}

	/**
	 * @param serviceDescriptionURL
	 * @return InputStream
	 */
	private InputStream prepareInput(URI serviceDescriptionURL) {
		try {
			return serviceDescriptionURL.toURL().openStream();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("URL not correct for " + serviceDescriptionURL);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Not a valid Host: " + serviceDescriptionURL);
		}
	}

	/**
	 * Helper method that checks whether the given input string contains all of
	 * the string fragments in the String array or not.
	 * 
	 * @param input
	 *            string that is checked
	 * @param array
	 *            of String values
	 * @return true if all String fragments are contained
	 */
	private boolean containsAll(String input, String[] containedArray) {
		String loweredInput = input.toLowerCase();
		for (String contained : containedArray) {
			if (!loweredInput.contains(contained.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public static String inputStreamToString(InputStream inputStream) {
		final int pkgSize = 1024;
		byte[] data = new byte[pkgSize];
		StringBuffer buffer = new StringBuffer(pkgSize * 10);
		int size;
		try {
			size = inputStream.read(data, 0, data.length);
			while (size > 0) {
				String str = new String(data, 0, size);
				buffer.append(str);
				size = inputStream.read(data, 0, data.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

}
