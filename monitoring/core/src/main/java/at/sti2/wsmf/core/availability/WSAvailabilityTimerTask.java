/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.wsmf.core.availability;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.WebServiceEndpoint;
import at.sti2.wsmf.core.data.channel.WSAvailabilityChannelHandler;

/**
 * @author Alex Oberhauser
 */
/**
 * @author Benjamin Hiltpolt
 * 
 */
public class WSAvailabilityTimerTask extends TimerTask {
	private static Logger log = Logger.getLogger(WSAvailabilityTimerTask.class);
	private WebServiceEndpoint webserviceendpoint;

	/*
	 * Update time for logging / monitoring repository issues
	 */
	private long updateTimeMinutes = 0;

	public WSAvailabilityTimerTask(WebServiceEndpoint webserviceendpoint,
			long updateTimeMinuts) {
		this.webserviceendpoint = webserviceendpoint;
		this.updateTimeMinutes = updateTimeMinuts;
	}

	private WSAvailabilityState updateAvailabilityState(
			WebServiceEndpoint _webservice, long updateTimeMinutes)
			throws RepositoryException, RDFParseException,
			FileNotFoundException, IOException {

		this.updateTimeMinutes = updateTimeMinutes;

		WSAvailabilityState oldstate = _webservice.getAvailabilityStatus();
		WSAvailabilityState state = WSAvailabilityState.WSUnavailable;

		URL endpoint = _webservice.getEndpoint();

		if (WSAvailabilityChecker.isWebServiceAvailable(
				endpoint.toExternalForm(), null)) {
			state = WSAvailabilityState.WSAvailable;
		} else {
			state = WSAvailabilityState.WSUnavailable;
		}
		log.info("Availability of '" + endpoint + "' = " + state);

		_webservice.changeAvailabilityStatus(state, this.updateTimeMinutes);

		if (oldstate != state) {
			try {
				WSAvailabilityChannelHandler.getInstance().sendState(
						_webservice.getEndpoint().toExternalForm(), state);
			} catch (QueryEvaluationException e) {
				log.error("Not able to send availability changed message, through exception: "
						+ e.getLocalizedMessage());
			} catch (MalformedQueryException e) {
				log.error("Not able to send availability changed message, through exception: "
						+ e.getLocalizedMessage());
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
		try {

			WebServiceEndpoint currentActiveWS = WebServiceEndpointConfig.getConfig(
					this.webserviceendpoint.getEndpoint()).getWebServiceEndpoint();

			this.updateAvailabilityState(currentActiveWS,
					this.updateTimeMinutes);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RDFParseException e) {
			e.printStackTrace();
		}
	}

}
