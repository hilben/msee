package at.sti2.msee.invocation.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.common.Parameter;

/**
 * This class parses parameters of an XML string into {@link Map}.
 * 
 * @author Christian Mayr
 * 
 */
public class ParameterParser {
	protected Logger logger = Logger.getLogger(this.getClass());
	private String parameterInputData = null;

	public ParameterParser(String inputData) {
		this.parameterInputData = inputData;
	}

	/**
	 * Parses the input parameters in the format <parameters><id>1</id>
	 * <name>john</name> </parameters> into a {@link List<Parameter>}.
	 * 
	 * @param inputDataAsXML
	 * @return
	 * @throws ServiceInvokerException
	 */
	public List<Parameter> parse() throws ServiceInvokerException {
		if (parameterInputData == null) {
			throw new ServiceInvokerException("Input data is not set");
		}
		List<Parameter> parameters = new ArrayList<>();
		InputSource inputSource = new InputSource(new ByteArrayInputStream(
				parameterInputData.getBytes()));
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
			Document doc = parser.getDocument();

			NodeList root = doc.getChildNodes();
			if (root.getLength() == 0) {
				logger.warn("no parameters set - are they not needed? - "
						+ "Hint: Format: <parameters><id>1</id> <name>john</name> </parameters> ");
				return parameters;
			}

			Node comp = getNode("parameters", root);
			if (comp != null) {
				NodeList params = comp.getChildNodes();
				for (int i = 0; i < params.getLength(); i++) {
					Node param = params.item(i);
					parameters.add(new Parameter(param.getNodeName(), param.getTextContent()));
				}
			} else {
				logger.warn("no parameters set - are they not needed? - "
						+ "Hint: Format: <parameters><id>1</id> <name>john</name> </parameters> ");
			}
			return parameters;
		} catch (SAXException | IOException | NullPointerException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceInvokerException("Parsing of parameters only possible for this "
					+ "Format: <parameters><id>1</id> <name>john</name> </parameters> "
					+ ", but service was invoked by " + parameterInputData);
		}
	}

	/**
	 * Retrieves the {@link Node} of the given nodes that belongs to the given
	 * tag name.
	 * 
	 * @param tagName
	 * @param nodes
	 * @return {@link Node}
	 */
	private Node getNode(String tagName, NodeList nodes) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().equals(tagName)) {
				return node;
			}
		}
		return null;
	}

}
