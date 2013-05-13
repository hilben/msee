package at.sti2.msee.monitoring.core.chart;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 *         TODO: clean up, refactoring...
 */
public class GoogleChart {

	public String getSomeStuff(List<String> endpoints, List<String> qosParamKeys)
			throws FileNotFoundException, IOException, Exception {

		return "ajaxCallSucceed("
				+ new GoogleChart().asJson(endpoints, qosParamKeys) + ")";
	}

	/**
	 * @return
	 * 
	 */
	public String asJson(List<String> endpoints, List<String> qosParamKeys)
			throws MonitoringException {

		MonitoringComponent m = null;
		try {
			m = new MonitoringComponentImpl();
		} catch (RepositoryException | IOException e) {
			throw new MonitoringException(e);
		}

		GoogleChartJSONDataTableCreator j = new GoogleChartJSONDataTableCreator();

		for (String endpoint : endpoints) {
			System.out.println("Add endpoint to json: " + endpoint);
			j.addColum(new GoogleChartColumnDataTableEntry("endpoint",
					endpoint, "datetime"));
		}
		for (String qosParamKey : qosParamKeys) {
			j.addColum(new GoogleChartColumnDataTableEntry(qosParamKey,
					qosParamKey, "number"));
		}

		// Iterate over all endpoints
		for (String endpoint : endpoints) {

			int countQoSParams = 1;

			// For all qosParamKeys receive them
			for (String qosParamKey : qosParamKeys) {

				List<QoSParameter> data = new ArrayList<QoSParameter>();

				// Receive data of this QoSParam

				QoSType k = QoSType.valueOf(qosParamKey);

				try {
					data = m.getQoSParametersInTimeframe(new URL(endpoint), k,
							null, null);
				} catch (MalformedURLException | MonitoringException
						| MonitoringNoDataStoredException e) {
					throw new MonitoringException(e);
				}

				for (QoSParameter p : data) {

					String row[] = new String[qosParamKeys.size() + 1];

					row[0] = "\"" + p.getTime() + "\"";
					// row[0] = "\"" + p.getTime() + "\"";
					for (int i = 1; i < row.length; i++) {
						row[i] = "null";
					}
					row[countQoSParams] = p.getValue();
					j.addRow(row);
				}

				countQoSParams++;
			}
		}

		System.out.println(j.toJSON());

		String ret = "";
		ret = new String(j.toJSON());

		return ret;

	}

	public static void main(String args[]) throws Exception {

		long time = System.currentTimeMillis();

		ArrayList<String> endpoints = new ArrayList<String>();
		ArrayList<String> qosParamKeys = new ArrayList<String>();

		MonitoringComponent m = new MonitoringComponentImpl();

		String ws1 = "http://www.example.com/chartws1";
		String ws2 = "http://www.example.com/chartws2";
		String ws3 = "http://www.example.com/chartws3";

		// for (int i = 0; i < 3; i++) {
		// m.addSuccessfulInvocationData(new URL(ws1), Math.random() * 1000,
		// Math.random() * 1000, Math.random() * 1000);
		// m.addSuccessfulInvocationData(new URL(ws2), Math.random() * 1000,
		// Math.random() * 1000, Math.random() * 1000);
		// m.addSuccessfulInvocationData(new URL(ws2), Math.random() * 1000,
		// Math.random() * 1000, Math.random() * 1000);
		// }

		endpoints.add(ws1);
		endpoints.add(ws2);
		endpoints.add(ws3);
		qosParamKeys.add("ResponseTime");
		qosParamKeys.add("PayloadSizeResponse");
		qosParamKeys.add("PayloadSizeRequest");
		// qosParamKeys.add("AvailableTime");
		System.out.println(new GoogleChart().asJson(endpoints, qosParamKeys));
		System.out.println("Done in " + (System.currentTimeMillis() - time));
	}
}