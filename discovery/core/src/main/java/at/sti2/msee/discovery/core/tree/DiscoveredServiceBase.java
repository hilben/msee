package at.sti2.msee.discovery.core.tree;

import java.util.HashSet;
import java.util.Set;

public class DiscoveredServiceBase implements DiscoveredService {
	private String name;
	private String endpoint = null;
	private String namespace = null;
	private Set<DiscoveredOperation> operationSet = new HashSet<DiscoveredOperation>();

	public DiscoveredServiceBase() {
		this(new String());
	}

	public DiscoveredServiceBase(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addDiscoveredOperation(DiscoveredOperation discoveredOperation) {
		operationSet.add(discoveredOperation);
	}

	public Set<DiscoveredOperation> getOperationSet() {
		return operationSet;
	}

	public void setOperationSet(Set<DiscoveredOperation> operationSet) {
		this.operationSet = operationSet;
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
		DiscoveredServiceBase other = (DiscoveredServiceBase) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\tService (" + getName() + ")\n" + getOperationSet();
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	@Override
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getNameSpace() {
		return namespace;
	}

	@Override
	public void setNameSpace(String namespace) {
		this.namespace = namespace;
	}
}
