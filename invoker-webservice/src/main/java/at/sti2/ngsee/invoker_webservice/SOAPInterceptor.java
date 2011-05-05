package at.sti2.ngsee.invoker_webservice;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

public class SOAPInterceptor extends AbstractSoapInterceptor {
	
	public SOAPInterceptor() {
		super(Phase.RECEIVE);
	}

	@Override
	public void handleMessage(SoapMessage _message) throws Fault {
		SOAPHeaderThreadLocal.set(_message.getHeaders());
	}

}
