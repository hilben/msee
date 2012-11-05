/**
 * 
 */
package at.sti2.wsmf.ws.json.chartcore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class GoogleChartJSONDataTableCreator {
	
	
	protected List<ChartColumnDataTableEntry> columns = new ArrayList<ChartColumnDataTableEntry>();
	protected List<List<String>> rows = new ArrayList<List<String>>();
	
	public GoogleChartJSONDataTableCreator() {
		
	}
	
	public void addColum(ChartColumnDataTableEntry column) {
		this.columns.add(column);
	}
	

	
	public void addRow(String ... rowentries) {
		
		ArrayList<String> s = new ArrayList<String>();
		
		for (String r : rowentries) {
			s.add(r);
		}
		
		this.rows.add(s);
	}
	
	
	
	public String toJSON() {
		String ret = "";
//		ret += " { \"cols\":  [";
		ret += " { cols:  [";
		
		Iterator<ChartColumnDataTableEntry> cit = this.columns.iterator();
		while (cit.hasNext()) {
			ret += cit.next().toJSON();
			if (cit.hasNext()) {
				ret+=",";
			}
		}
		
		ret += "],";
		
//		ret += "\"rows\": [ ";		
		ret += "rows: [ ";
		
		
		Iterator<List<String>> rowsit = rows.iterator();
		while (rowsit.hasNext()) {
			
			ret += "{ c: [";
//			ret += "{ \"c\": [";
			
			Iterator<String> rowit = rowsit.next().iterator();
			
			while (rowit.hasNext()) {
				ret += " {v:"+rowit.next()+" }";
//				ret += " {\"v\":"+rowit.next()+" }";
				if (rowit.hasNext()) {
					ret+=",";
				}
			}

			
			ret += "]}";
			
			if (rowsit.hasNext()) {
				
				ret += ",";
			}
			
		}
		
		ret += "] } ";
		
		return ret;
	}

}
