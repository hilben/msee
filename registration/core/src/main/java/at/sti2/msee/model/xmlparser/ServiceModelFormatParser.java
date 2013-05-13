package at.sti2.msee.model.xmlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class represents a service model format parser that parses a given XML
 * input String and creates a Set of all elements and attribute values.
 * 
 */
public class ServiceModelFormatParser extends DefaultHandler {

	private String input;
	private Set<String> elements = new HashSet<String>();

	public ServiceModelFormatParser(String input) {
		this.input = input;
	}

	/**
	 * Parses the given input and return the data set of elements and
	 * attributes.
	 * 
	 * @return data set of elements and attributes
	 */
	public Set<String> parseElements() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(new ByteArrayInputStream(input.getBytes()), this);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elements;
	}

	/**
	 * This method is called by the SAX parser and collects all elements with
	 * its contained attributes in the class' main data set.
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		elements.add(qName);
		for (int i = 0; i < attributes.getLength(); i++) {
			elements.add(attributes.getValue(i));
		}
	}
}