/**
 * WSAvailabilityTimerTask.java - at.sti2.wsmf.core
 */
package at.sti2.wsmf.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.core.data.WebServiceEndpoint;
import at.sti2.wsmf.core.data.channel.WSAvailabilityChannelHandler;

/**
 * @author Alex Oberhauser
 */
public class WSAvailabilityTimerTask extends TimerTask {
	private Logger log = Logger.getLogger(WSAvailabilityTimerTask.class);
	
	private WSAvailabilityState updateAvailabilityState(WebServiceEndpoint _webservice) throws RepositoryException, RDFParseException, FileNotFoundException, IOException {
		WSAvailabilityState oldstate = _webservice.getAvailabilityStatus();
		URL endpoint = _webservice.getEndpoint();
		WSAvailabilityState state = WSAvailabilityState.WSUnavailable;
		if ( InvocationHandler.isWebServiceAvailable(endpoint.toExternalForm(), null) )
			state = WSAvailabilityState.WSAvailable;
		else
			state = WSAvailabilityState.WSUnavailable;
		log.info("Availability of '" + endpoint + "' = " + state);
		_webservice.changeAvailabilityStatus(state);
		if ( oldstate != state ) {
			try {
				WSAvailabilityChannelHandler.getInstance().sendState(_webservice.getEndpoint().toExternalForm(), state);
			} catch (QueryEvaluationException e) {
				log.error("Not able to send availability changed message, through exception: " + e.getLocalizedMessage());
			} catch (MalformedQueryException e) {
				log.error("Not able to send availability changed message, through exception: " + e.getLocalizedMessage());
			}
		}
		return state;
	}
	
	/**
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		log.info("Starting availability checker...");
		Vector<URL> checkedURL = new Vector<URL>();
		EndpointHandler endpointHandler;
		try {
			endpointHandler = EndpointHandler.getInstance();
			
			WebServiceEndpoint currentActiveWS = endpointHandler.getCurrentActiveWS();
			WSAvailabilityState currentActiveWSState = this.updateAvailabilityState(currentActiveWS);
			checkedURL.add(currentActiveWS.getEndpoint());
			
			boolean masterBackOnline = false;
			
			WebServiceEndpoint masterWS = endpointHandler.getMasterWS();
			URL masterWSURL = masterWS.getEndpoint();
			WSAvailabilityState masterStatus;
			if ( !checkedURL.contains(masterWSURL) ) {
				masterStatus = this.updateAvailabilityState(masterWS);
				if ( masterStatus == WSAvailabilityState.WSAvailable ) {
					endpointHandler.changeCurrentActiveWS(masterWS);
					masterBackOnline = true;
				}
				checkedURL.add(masterWSURL);
			}
			
			Vector<WebServiceEndpoint> availableFallbackWS = new Vector<WebServiceEndpoint>();
			Vector<URL> fallbackWS = endpointHandler.getFallbackWS();
			for ( URL urlEntry : fallbackWS ) {
				if ( !checkedURL.contains(urlEntry) ) {
					WebServiceEndpoint entry = new WebServiceEndpoint(urlEntry, false); 
					WSAvailabilityState status = this.updateAvailabilityState(entry);
					if ( status == WSAvailabilityState.WSAvailable )
						availableFallbackWS.add(entry);
				}
			}

			if ( masterBackOnline && (currentActiveWSState == WSAvailabilityState.WSUnavailable
					|| currentActiveWSState == WSAvailabilityState.WSNotChecked) )
				if ( availableFallbackWS.size() > 0 )
					endpointHandler.changeCurrentActiveWS(availableFallbackWS.get(0));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}
	}

}
