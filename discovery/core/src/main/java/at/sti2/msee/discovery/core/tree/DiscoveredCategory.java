package at.sti2.msee.discovery.core.tree;

import java.util.Set;

public interface DiscoveredCategory {
	public String getName();

	public void setName(String name);

	public Set<DiscoveredService> getServiceSet();

	public void addDiscoveredService(DiscoveredService discoveredService);
}
