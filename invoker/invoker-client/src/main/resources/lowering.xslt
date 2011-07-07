<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:efreight="http://demo.sti2.at/efreight/ontologies/FalFormsOntology.rdf#">

        <xsl:output method="xml" version="1.0"
                indent="yes"
                standalone="yes"
                omit-xml-declaration="yes"/>

        <xsl:template match="/rdf:RDF">
                <SubmitFALForm xmlns='http://see.sti2.at/dummy'>
                        <shipcode>
                                <xsl:value-of select="./efreight:Ship/efreight:hasCallSign"/>
                        </shipcode>
                        <port>
                                <portcode>
                                        <xsl:value-of select="./efreight:GeneralDeclarationF1/efreight:hasPort//efreight:hasPortCode"/>
                                </portcode>
                                <location>
                                        <xsl:value-of select="./efreight:GeneralDeclarationF1/efreight:hasPort//efreight:hasPortName"/>
                                </location>
                        </port>
                        <arrivalOrDepartureTime>
                                <xsl:value-of select="./efreight:GeneralDeclarationF1/efreight:hasArrivalOrDepartureDateTime"/>
                        </arrivalOrDepartureTime>
                </SubmitFALForm>
        </xsl:template>

</xsl:stylesheet>
