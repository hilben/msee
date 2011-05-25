
package at.sti2.see;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * SESA Invoker Component.
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "InvokerWebService", targetNamespace = "http://see.sti2.at/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface InvokerWebService {


    /**
     * 
     * @param operation
     * @param serviceID
     * @param inputData
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "invoke", targetNamespace = "http://see.sti2.at/", className = "at.sti2.see.Invoke")
    @ResponseWrapper(localName = "invokeResponse", targetNamespace = "http://see.sti2.at/", className = "at.sti2.see.InvokeResponse")
    public String invoke(
        @WebParam(name = "serviceID", targetNamespace = "")
        String serviceID,
        @WebParam(name = "operation", targetNamespace = "")
        String operation,
        @WebParam(name = "inputData", targetNamespace = "")
        String inputData)
        throws Exception_Exception
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://see.sti2.at/", className = "at.sti2.see.GetVersion")
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://see.sti2.at/", className = "at.sti2.see.GetVersionResponse")
    public String getVersion();

}
