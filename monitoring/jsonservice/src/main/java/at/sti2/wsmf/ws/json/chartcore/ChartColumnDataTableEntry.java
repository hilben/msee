package at.sti2.wsmf.ws.json.chartcore;
/**
 * 
 */

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class ChartColumnDataTableEntry {

	private String id;
	private String label;
	private String type;

	/**
	 * @param id
	 * @param label
	 * @param type
	 */
	public ChartColumnDataTableEntry(String id, String label, String type) {
		super();
		this.id = id;
		this.label = label;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ChartColumnDataTableEntry [id=" + id + ", label=" + label
				+ ", type=" + type + "]";
	}

	public String toJSON() {
		return "{ id: \"" + id + "\" , label: \"" + label + "\" , type: \"" + type
		+ "\" }";
//		return "{ \"id\": \"" + id + "\" , \"label\": \"" + label + "\" , \"type\": \"" + type
//				+ "\" }";
	}

}
