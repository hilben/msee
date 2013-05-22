package at.sti2.msee.discovery.core.tree;

public class DiscoveredServiceHrests extends DiscoveredServiceBase {

	public DiscoveredServiceHrests() {
		super();
	}

	public DiscoveredServiceHrests(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "DiscoveredServiceHrests (" + getName() + ")\n[operationSet="
				+ super.getOperationSet() + "\n]";
	}
}
