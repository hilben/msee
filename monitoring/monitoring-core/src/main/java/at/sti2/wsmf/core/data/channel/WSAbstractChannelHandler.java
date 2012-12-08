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
package at.sti2.wsmf.core.data.channel;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.common.MonitoringConfig;

/**
 * @author Alex Oberhauser
 * 
 */
public abstract class WSAbstractChannelHandler {
	private PersistentHandler persHandler;
	private MonitoringConfig cfg;
	private String channelURL;
	private final String channelPostfix;
	private String strName;// TODO:?? what is this

	protected WSAbstractChannelHandler(String _channelPostfix, String strName)
			throws IOException {
		this.channelPostfix = _channelPostfix;
		this.persHandler = PersistentHandler.getInstance();
		this.cfg = MonitoringConfig.getConfig();
		this.strName = strName;
		this.channelURL = this.cfg.getInstancePrefix() + "_" + this.strName
				+ "_" + _channelPostfix;
	}

	protected String getContextURL(URL _endpoint) {
		return _endpoint + "_" + this.strName + "_" + this.channelPostfix;
	}

	public Vector<ChannelSubscriber> getSubscriber()
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException {
		return this.persHandler.getSubscriber(this.channelURL);
	}

	public void subscribe(URL _endpoint, String _namespace,
			String _operationName, String _soapAction)
			throws RepositoryException {
		this.persHandler.subscribeChannel(this.channelURL, this.getContextURL(_endpoint), _namespace, _operationName, _soapAction);
	}

	public void unsubsribe(URL _endpoint) throws RepositoryException {
		this.persHandler.deleteContext(this.getContextURL(_endpoint));
		this.persHandler.commit();
	}

}
