/**
 * DebugResponse.java - at.sti2.ngsee.grounding.api.data
 */
package at.sti2.ngsee.grounding.api.data;

/**
 * A object of this class is returned by the transformation_debug operation.
 * It contains more information as needed in production use. For example it
 * includes also the intermediate message (an instance of the ontology) and 
 * simple performance mechanism.
 * 
 * @author Alex Oberhauser
 */
public class DebugResponse {
	private String xsltToOntologyURL;
	private String xsltToOutputURL;
	
	private String inputMessage;
	private String intermediateMessage;
	private String outputMessage;
	
	/**
	 * Exectuion Time
	 */
	private double totalExecutionTime;
	private double input2OntologyExecutionTime;
	private double ontology2OutputExecutionTime;
	
	/**
	 * Needed for serialization to/from SOAP message
	 */
	public DebugResponse() {}
	
	public DebugResponse(String _inputMessage, String _xsltToOntologyURL, String _xsltToOutputURL) {
		this.inputMessage = _inputMessage;
		this.xsltToOntologyURL = _xsltToOntologyURL;
		this.xsltToOutputURL = _xsltToOutputURL;
	}
	
	/**
	 * @param _inputMessage The XML input message.
	 */
	public void setInputMessage(String _inputMessage) {
		this.inputMessage = _inputMessage;
	}
	
	/**
	 * @param _intermediateMessage The instance of the ontology that is produced as intermediate transformation step.
	 */
	public void setIntermediateMessage(String _intermediateMessage) {
		this.intermediateMessage = _intermediateMessage;
	}
	
	public void setXsltToOntologyURL(String _xsltToOntologyURL) {
		this.xsltToOntologyURL = _xsltToOntologyURL;
	}
	
	public void setXsltToOutputURL(String _xsltToOutputURL) {
		this.xsltToOutputURL = _xsltToOutputURL;
	}
	
	public String getXsltToOntologyURL() {
		return this.xsltToOntologyURL;
	}
	
	public String getXsltToOutputURL() {
		return this.xsltToOutputURL;
	}
	
	/**
	 * @param _outputMessage The XML output message
	 */
	public void setOutputMessage(String _outputMessage) {
		this.outputMessage = _outputMessage;
	}
	
	public String getInputMessage() {
		return this.inputMessage;
	}
	
	public String getIntermediateMessage() {
		return this.intermediateMessage;
	}
	
	public String getOutputMessage() {
		return this.outputMessage;
	}
	
	
	public void setTotalExecutionTime(double _totalExecutionTime) {
		this.totalExecutionTime = _totalExecutionTime;
	}
	
	public void setInput2OntologyExecutionTime(double _input2OntologyExecutionTime) {
		this.input2OntologyExecutionTime = _input2OntologyExecutionTime;
	}
	
	public void setOntology2OutputExecutionTime(double _ontology2OutputExecutionTime) {
		this.ontology2OutputExecutionTime = _ontology2OutputExecutionTime;
	}
	
	public double getTotalExecutionTime() {
		return this.totalExecutionTime;
	}
	
	public double getInput2OntologyExecutionTime() {
		return this.input2OntologyExecutionTime;
	}
	
	public double getOntology2OutputExecutionTime() {
		return this.ontology2OutputExecutionTime;
	}
	
}
