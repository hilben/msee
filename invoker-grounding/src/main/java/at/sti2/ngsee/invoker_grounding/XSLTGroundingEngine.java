package at.sti2.ngsee.invoker_grounding;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker_api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker_api.grounding.exception.GroundingException;

/**
 * <b>Purpose:</b> Grounding engine based on XSLT transformations<br>
 * <b>Description:</b> <br>
 * <b>Copyright:</b> Copyright (c) 2011 STI<br>
 * <b>Company:</b> STI Innsbruck<br>
 * 
 * @author Michael Rogger, Alex Oberhauser, Corneliu Valentin Stanciu<br>
 * @version $Id$<br>
 *          Date of creation: 13.04.2011<br>
 *          File: $Source$<br>
 *          Modifier: $Author$<br>
 *          Revision: $Revision$<br>
 *          State: $State$<br>
 */
public class XSLTGroundingEngine implements IGroundingEngine {

	protected static Logger logger = Logger
			.getLogger(XSLTGroundingEngine.class);

	private URL loweringSchemaURL;
	private URL liftingSchemaURL;

	public XSLTGroundingEngine(URL _loweringSchemaURL, URL _liftingSchemaURL) {
		this.loweringSchemaURL = _loweringSchemaURL;
		this.liftingSchemaURL = _liftingSchemaURL;
	}

	/*
	 * (non-Javadoc)
	 * @see at.sti2.ngsee.invoker_api.grounding.IGroundingEngine#lower(java.lang.String)
	 */
	@Override
	public String lower(String rdfInputData) throws GroundingException {

		logger.debug("lowering schema URL: " + loweringSchemaURL);
		logger.debug("rdf input data:\n" + rdfInputData);

		//instantiate transformer factory
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setURIResolver(new GroundingURIResolver());
		Transformer transformer;
		
		//results will be stored in writer
		StringWriter result = new StringWriter();
		try {
			//set lowering schema url
			transformer = tf.newTransformer(new StreamSource(loweringSchemaURL.toString()));
			
			//indent xml output
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			// transform rdf to xml using lowering xslt
			transformer.transform(new StreamSource(new StringReader(rdfInputData)), new StreamResult(result));
		
		} catch (TransformerConfigurationException e) {
			throw new GroundingException(
					"Lowering Transformer not configured correctly",
					e.getCause());
		} catch (TransformerException e) {
			throw new GroundingException(
					"Lowering transformation produced errors", e.getCause());
		}
		
		String strResult = result.toString();
		logger.debug("lowered output:\n" + strResult);
		
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see at.sti2.ngsee.invoker_api.grounding.IGroundingEngine#lift(java.lang.String)
	 */
	@Override
	public String lift(String xmlInputData) throws GroundingException {
		
		logger.debug("lifting schema URL: " + liftingSchemaURL);
		logger.debug("xml input data:\n" + xmlInputData);
		
		//instantiate transformer factory
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setURIResolver(new GroundingURIResolver());
		Transformer transformer;
		
		
		//results will be stored in writer
		StringWriter result = new StringWriter();
		try {
			//set lowering schema url
			transformer = tf.newTransformer(new StreamSource(liftingSchemaURL.toString()));
			
			//indent xml output
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			// transform rdf to xml using lowering xslt
			transformer.transform(new StreamSource(new StringReader(xmlInputData)), new StreamResult(result));
		
		} catch (TransformerConfigurationException e) {
			throw new GroundingException(
					"Lifting Transformer not configured correctly",
					e.getCause());
		} catch (TransformerException e) {
			throw new GroundingException(
					"Lifting transformation produced errors", e.getCause());
		}
		
		String strResult = result.toString();
		logger.debug("lifted output:\n" + strResult);
		
		return result.toString();
	}

}
