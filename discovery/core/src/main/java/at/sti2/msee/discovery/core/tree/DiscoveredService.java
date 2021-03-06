package at.sti2.msee.discovery.core.tree;

import java.util.Set;

public interface DiscoveredService {
	public String getName();

	public void setName(String name);

	public Set<DiscoveredOperation> getOperationSet();

	public void addDiscoveredOperation(DiscoveredOperation operation);

	public String getEndpoint();

	public void setEndpoint(String endpoint);

	public String getNameSpace();

	void setNameSpace(String namespace);
}
