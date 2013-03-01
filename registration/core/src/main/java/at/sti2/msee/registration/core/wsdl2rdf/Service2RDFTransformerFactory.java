package at.sti2.msee.registration.core.wsdl2rdf;

public class Service2RDFTransformerFactory {

	public static Service2RDFTransformer newInstance(String descriptionFormat) throws Service2RDFTransformerException {		

		if (descriptionFormat.equals(Service2RDFTransformer.SAWSDL))
			return new Sawsdl2RDFTransformerImpl();
		else
			throw new Service2RDFTransformerException("Description format not supported");
	}
}
