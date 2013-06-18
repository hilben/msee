package at.sti2.msee.invocation.core.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class InvokerREST extends InvokerBase {

	public InvokerREST(MonitoringComponent monitoring) {
		super(monitoring);
	}

	public String invokeREST(final URL serviceID, String address, final String method,
			final List<Parameter> parameters) throws ServiceInvokerException {
		// monitoring
		long startTime = System.currentTimeMillis();
		initMonitoring(serviceID);
		long requestMessageSize = getParameterSize(parameters);

		// REST
		final String charset = "UTF-8";
		NameValuePair[] data = new NameValuePair[parameters.size()];
		int i = 0;
		for (Pair<String, String> parameter : parameters) {
			try {
				address = address.replace("{" + parameter.name() + "}",
						URLEncoder.encode(parameter.value(), charset));
			} catch (UnsupportedEncodingException e) {
				throw new ServiceInvokerException(e);
			}
			NameValuePair tmpPair = new NameValuePair(parameter.name(), parameter.value());
			data[i++] = tmpPair;
		}
		if (address.contains("{") && address.contains("}")) {
			String parameter = address.substring(address.indexOf("{") + 1, address.indexOf("}"));
			throw new ServiceInvokerException("Parameter \"" + parameter + "\" is missing.");
		}
		logger.info("Invocation of: " + address);

		HttpClient client = new HttpClient();
		String output = "";

		try {
			startMonitoring();
		} catch (MonitoringException e) {
			logger.error(e);
		}

		switch (method.toLowerCase()) {
		case "get":
			GetMethod getHandler = new GetMethod(address);

			InputStream getResponse = new ByteArrayInputStream("".getBytes());
			try {
				checkStatus(client.executeMethod(getHandler));
				getResponse = getHandler.getResponseBodyAsStream();
				output = convertStreamToString(getResponse);
			} catch (IOException e) {
				monitorFailedService();
				throw new ServiceInvokerException(e);
			} finally {
				getHandler.releaseConnection();
			}
			break;
		case "post":
			PostMethod postHandler = new PostMethod(address);

			InputStream postResponse = new ByteArrayInputStream("".getBytes());
			try {
				postHandler.setQueryString(data); // TODO check: are both
													// needed?
				postHandler.setRequestBody(data);
				checkStatus(client.executeMethod(postHandler));
				postResponse = postHandler.getResponseBodyAsStream();
				output = convertStreamToString(postResponse);
			} catch (IOException e) {
				monitorFailedService();
				throw new ServiceInvokerException(e);
			} finally {
				postHandler.releaseConnection();
			}
			break;
		case "put":
			PutMethod putHandler = new PutMethod(address);
			InputStream putResponse = new ByteArrayInputStream("".getBytes());
			try {
				putHandler.setRequestEntity(new StringRequestEntity(getDataValue(parameters),
						"application/json", charset));
				checkStatus(client.executeMethod(putHandler));
				putResponse = putHandler.getResponseBodyAsStream();
				output = convertStreamToString(putResponse);
			} catch (IOException e) {
				monitorFailedService();
				throw new ServiceInvokerException(e);
			} finally {
				putHandler.releaseConnection();
			}
			break;
		case "delete":
			DeleteMethod deleteHandler = new DeleteMethod(address);
			InputStream deleteResponse = new ByteArrayInputStream("".getBytes());
			try {
				client.executeMethod(deleteHandler);
				deleteResponse = deleteHandler.getResponseBodyAsStream();
				output = convertStreamToString(deleteResponse);
			} catch (IOException e) {
				monitorFailedService();
				throw new ServiceInvokerException(e);
			} finally {
				deleteHandler.releaseConnection();
			}
			break;
		default:
			throw new ServiceInvokerException("method not supported");
		}

		successfulInvocation(startTime, requestMessageSize, output);

		return output;
	}

	/**
	 * Returns the value for the parameter "data".
	 * 
	 * @param parameters
	 */
	private String getDataValue(List<Parameter> parameters) {
		return getParameterValue(parameters, "data");
	}

	/**
	 * Returns the value of the given parameter name. If the name is not found
	 * it returns null.
	 * 
	 * @param parameters
	 * @param name
	 */
	private String getParameterValue(List<Parameter> parameters, String name) {
		for (Pair<String, String> parameter : parameters)
			if (parameter.name().equals(name))
				return parameter.value();
		return null;
	}

	/**
	 * Checks the server status message of the invocation. If the status is not
	 * of type 2xx an exception is thrown.
	 * 
	 * @param status
	 * @throws ServiceInvokerException
	 */
	private int checkStatus(int status) throws ServiceInvokerException {
		if (status < 200 || status > 299) {
			throw new ServiceInvokerException("Invocation failed with status " + status);
		}
		return status;
	}

	/**
	 * Converts the given {@link InputStream} into a string.
	 * 
	 * @param inputStream
	 */
	private static String convertStreamToString(java.io.InputStream inputStream) {
		java.util.Scanner s = new java.util.Scanner(inputStream);
		s.useDelimiter("\\A");
		String retval = s.hasNext() ? s.next() : "";
		s.close();
		return retval;
	}
}
