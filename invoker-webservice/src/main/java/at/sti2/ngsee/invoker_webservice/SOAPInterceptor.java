package at.sti2.ngsee.invoker_webservice;

import java.util.List;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

public class SOAPInterceptor extends AbstractSoapInterceptor {
	
	public SOAPInterceptor() {
		super(Phase.RECEIVE);
	}

	@Override
	public void handleMessage(SoapMessage _message) throws Fault {
		System.out.println("******** READING OUT HEADER **********");
		List<Header> headers = _message.getHeaders();
		for ( Header header : headers ) {
			System.out.println(header.getName());
		}
		System.out.println("**************************************");

		_message.put("testing", "interceptor value");
	}

}
