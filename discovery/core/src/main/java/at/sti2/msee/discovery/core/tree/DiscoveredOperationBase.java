package at.sti2.msee.discovery.core.tree;

import java.util.HashSet;
import java.util.Set;

public class DiscoveredOperationBase implements DiscoveredOperation {
	private String name;
	private Set<String> inputSet = new HashSet<String>();
	private Set<String> outputSet = new HashSet<String>();
	private Set<String> inputVaultSet = new HashSet<String>();
	private Set<String> outputVaultSet = new HashSet<String>();
	private Set<String> addressSet = new HashSet<String>();
	private Set<String> methodSet = new HashSet<String>();

	public DiscoveredOperationBase() {
		this(new String());
	}

	public DiscoveredOperationBase(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addInput(String input) {
		inputSet.add(input);
	}

	public void addOutput(String output) {
		outputSet.add(output);
	}

	public void addInputVault(String inputVault) {
		this.inputVaultSet.add(inputVault);
	}

	public void addOutputVault(String outputVault) {
		this.outputVaultSet.add(outputVault);
	}

	public void addAddress(String address) {
		this.addressSet.add(address);
	}

	public void addMethod(String method) {
		this.methodSet.add(method);
	}

	public String getAddress() {
		if (this.addressSet.iterator().hasNext()) {
			return this.addressSet.iterator().next();
		} else {
			return null;
		}
	}

	public String getMethod() {
		if (this.methodSet.iterator().hasNext()) {
			return this.methodSet.iterator().next();
		} else {
			return null;
		}
	}

	public Set<String> getInputSet() {
		return inputSet;
	}

	public void setInputSet(Set<String> inputSet) {
		this.inputSet = inputSet;
	}

	public Set<String> getOutputSet() {
		return outputSet;
	}

	public void setOutputSet(Set<String> outputSet) {
		this.outputSet = outputSet;
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
		DiscoveredOperationBase other = (DiscoveredOperationBase) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\t\t (" + getName() + ")\n\t\t\t" + inputSet + "\n\t\t\t" + outputSet + "\n";
	}

}
