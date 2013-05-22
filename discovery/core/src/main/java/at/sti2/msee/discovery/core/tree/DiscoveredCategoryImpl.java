package at.sti2.msee.discovery.core.tree;

import java.util.HashSet;
import java.util.Set;

public class DiscoveredCategoryImpl implements DiscoveredCategory {

	private String name;
	private Set<DiscoveredService> serviceSet = new HashSet<DiscoveredService>();

	public DiscoveredCategoryImpl() {
	}

	public DiscoveredCategoryImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void addDiscoveredService(DiscoveredService discoveredService) {
		serviceSet.add(discoveredService);
	}

	public Set<DiscoveredService> getServiceSet() {
		return serviceSet;
	}

	public void setServiceSet(Set<DiscoveredService> serviceSet) {
		this.serviceSet = serviceSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscoveredCategoryImpl other = (DiscoveredCategoryImpl) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Category (" + name + ")\n" + serviceSet;
	}
}
