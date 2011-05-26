
package at.sti2.wsmf.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ManagementWebServiceService", targetNamespace = "http://webservice.wsmf.sti2.at/", wsdlLocation = "http://localhost:9090/wsmf-webservice/services/management?wsdl")
public class ManagementWebServiceService
    extends Service
{

    private final static URL MANAGEMENTWEBSERVICESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(at.sti2.wsmf.webservice.ManagementWebServiceService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = at.sti2.wsmf.webservice.ManagementWebServiceService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:9090/wsmf-webservice/services/management?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:9090/wsmf-webservice/services/management?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        MANAGEMENTWEBSERVICESERVICE_WSDL_LOCATION = url;
    }

    public ManagementWebServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ManagementWebServiceService() {
        super(MANAGEMENTWEBSERVICESERVICE_WSDL_LOCATION, new QName("http://webservice.wsmf.sti2.at/", "ManagementWebServiceService"));
    }

    /**
     * 
     * @return
     *     returns ManagementWebService
     */
    @WebEndpoint(name = "ManagementWebServicePort")
    public ManagementWebService getManagementWebServicePort() {
        return super.getPort(new QName("http://webservice.wsmf.sti2.at/", "ManagementWebServicePort"), ManagementWebService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ManagementWebService
     */
    @WebEndpoint(name = "ManagementWebServicePort")
    public ManagementWebService getManagementWebServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.wsmf.sti2.at/", "ManagementWebServicePort"), ManagementWebService.class, features);
    }

}
