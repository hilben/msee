package at.sti2.msee.discovery.core.tree;

public class DiscoveredOperationHrests extends DiscoveredOperationBase {
	private String method;
	private String address;

	public DiscoveredOperationHrests() {
		super();
	}

	public DiscoveredOperationHrests(String name) {
		super(name);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "\nDiscoveredOperationHrests (" + getName() + ") \n  [inputSet="
				+ super.getInputSet() + "\n  outputSet=" + super.getOutputSet() + "\n  method="
				+ method + "\n  address=" + address + " ]";
	}

}
