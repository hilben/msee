package at.sti2.ngsee.invoker.grounding.engine.xslt;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * <b>Purpose:</b> This URI resolver is called by the xml processor when xml:import or xsl:include need to be resolved<br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Michael Rogger<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */

public class GroundingURIResolver implements URIResolver {

	private static Logger logger = Logger.getLogger(GroundingURIResolver.class);

	/*
	 * (non-Javadoc)
	 * @see javax.xml.transform.URIResolver#resolve(java.lang.String, java.lang.String)
	 */
	@Override
	public Source resolve(String arg0, String arg1) throws TransformerException {
		logger.debug("imported resource: " + arg0);
		logger.debug("father resource:  " + arg1);
		try {
			if (arg0.startsWith("http")) {
				URL url = new URL(arg0);
				URLConnection conn = url.openConnection();
				return new StreamSource(conn.getInputStream());
			}
			if (arg1.startsWith("http")) {
				URL url = new URL(arg1.substring(0, arg1.lastIndexOf("/"))
						+ "/" + arg0);
				URLConnection conn = url.openConnection();
				return new StreamSource(conn.getInputStream());
			}
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}		
		InputStream xslt = this.getClass().getResourceAsStream("/xslt/" + arg0);
		return new StreamSource(xslt);
	}

}
