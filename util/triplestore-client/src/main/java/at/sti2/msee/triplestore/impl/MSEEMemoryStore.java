package at.sti2.msee.triplestore.impl;

import java.util.ArrayList;

import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

/**
 * Extended MemoryStore class that handles problems on shutdown when connections
 * are still open, see: http://www.openrdf.org/issues/browse/SES-673.
 * 
 * @author Christian Mayr
 * 
 */
public class MSEEMemoryStore extends MemoryStore {
	ArrayList<NotifyingSailConnection> activeConnections = new ArrayList<NotifyingSailConnection>();

	@Override
	protected NotifyingSailConnection getConnectionInternal() throws SailException {
		NotifyingSailConnection con = super.getConnectionInternal();
		activeConnections.add(con);
		return con;
	}

	@Override
	public void shutDown() throws SailException {
		for (NotifyingSailConnection con : activeConnections) {
			con.close();
		}
		super.shutDown();
	}
}
