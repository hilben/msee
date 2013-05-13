package at.sti2.msee.model.transformer;

import at.sti2.msee.model.ServiceModelFormat;
import at.sti2.msee.model.transformer.hrests.Hrests2RDFTransformerImpl;
import at.sti2.msee.model.transformer.sawsdl.Sawsdl2RDFTransformerImpl;

public class Service2RDFTransformerFactory {

	public static Service2RDFTransformer newInstance(ServiceModelFormat format)
			throws Service2RDFTransformerException {

		switch (format) {
		case SAWSDL: {
			return new Sawsdl2RDFTransformerImpl();
		}
		case HRESTS: {
			return new Hrests2RDFTransformerImpl();
		}
		default:{
			throw new Service2RDFTransformerException(
					"Description format not supported");			
		}
		}
	}
}
