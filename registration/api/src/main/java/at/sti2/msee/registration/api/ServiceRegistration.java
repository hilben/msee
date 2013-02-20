package at.sti2.msee.registration.api;

public interface ServiceRegistration {

	public String register(String serviceDescriptionURL) throws Exception;
	
	public String deregister(String serviceURI) throws Exception;
	
	public String update(String serviceURI, String serviceURL) throws Exception;
	
}
