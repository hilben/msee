<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:ns1="urn:oasis:names:specification:ubl:schema:xsd:TransportExecutionPlan-2"
    xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
    xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
    xmlns:e="http://efreight.sti2.at/ontology/crs#">

    <xsl:output method="xml" version="1.0" indent="yes" omit-xml-declaration="no" />

    <xsl:variable name="id">
        <xsl:value-of select="/ns1:TransportExecutionPlanRequest/cbc:ID" />
    </xsl:variable>

    <xsl:template match="/ns1:TransportExecutionPlanRequest">
        <rdf:RDF>
            <e:Document rdf:about="#{$id}">
                <e:hasIdentifier><xsl:value-of select="cbc:ID" /></e:hasIdentifier>
                <e:issuedAt><xsl:value-of select="cbc:IssueDate"/>T<xsl:value-of select="cbc:IssueTime"/></e:issuedAt>
                <e:hasConsignor>
                    <e:Party>
                        <e:hasName><xsl:value-of select="cac:SenderParty/cac:PartyName/cbc:Name"/></e:hasName>
                    </e:Party>
                </e:hasConsignor>
                <e:hasConsignee>
                    <e:Party>
                        <e:hasName><xsl:value-of select="cac:ReceiverParty/cac:PartyName/cbc:Name"/></e:hasName>
                    </e:Party>
                </e:hasConsignee>
                <e:containsConsignment>
                    <e:Consignment>
                        <e:hasIdentifier><xsl:value-of select="cac:Consignment/cbc:ID" /></e:hasIdentifier>
                        <e:hasDescription><xsl:value-of select="cac:Consignment/cbc:Description"/></e:hasDescription>
                        <e:hasNetWeight><xsl:value-of select="cac:Consignment/cbc:NetWeightMeasure"/></e:hasNetWeight>
                        <e:hasTotalTransportHandlingUnitQuantity><xsl:value-of select="cac:Consignment/cbc:Quantity"/></e:hasTotalTransportHandlingUnitQuantity>
                        <e:hasHazardousRisk><xsl:value-of select="cac:Consignment/cbc:HazardousRiskIndicator"/></e:hasHazardousRisk>
                        <xsl:for-each select="cac:Consignment/cac:TransportHandlingUnit">
                            <e:containsTransportHandlingUnit>
                                <e:Package>
                                    <e:hasIdentifier><xsl:value-of select="cbc:ID"/></e:hasIdentifier>
                                </e:Package>
                            </e:containsTransportHandlingUnit>
                        </xsl:for-each>
                    </e:Consignment>
                </e:containsConsignment>
            </e:Document>
        </rdf:RDF>
    </xsl:template>

</xsl:stylesheet>
