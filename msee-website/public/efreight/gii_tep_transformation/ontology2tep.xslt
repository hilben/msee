<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:ns1="urn:oasis:names:specification:ubl:schema:xsd:TransportExecutionPlan-2"
    xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
    xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
    xmlns:e="http://efreight.sti2.at/ontology/crs#">

    <xsl:output method="xml" version="1.0" indent="yes" omit-xml-declaration="no" />

    <xsl:variable name="date" select="substring-before(/rdf:RDF/e:Document/e:issuedAt, 'T')" />
    <xsl:variable name="time" select="substring-after(/rdf:RDF/e:Document/e:issuedAt, 'T')" />

    <xsl:template match="/rdf:RDF/e:Document">
        <ns1:TransportExecutionPlanRequest>
            <cbc:ID><xsl:value-of select="e:hasIdentifier"/></cbc:ID>
            <cbc:IssueDate><xsl:value-of select="$date" /></cbc:IssueDate>
            <cbc:IssueTime><xsl:value-of select="$time" /></cbc:IssueTime>
            <cac:SenderParty>
                <cac:PartyName>
                    <cbc:Name><xsl:value-of select="e:hasConsignor/e:Party/e:hasName" /></cbc:Name>
                </cac:PartyName>
            </cac:SenderParty>
            <cac:ReceiverParty>
                <cac:PartyName>
                    <cbc:Name><xsl:value-of select="e:hasConsignee/e:Party/e:hasName" /></cbc:Name>
                </cac:PartyName>
            </cac:ReceiverParty>
            <cac:ReferencedGoodsItem>
                <cac:ID><xsl:value-of select="e:containsConsignment/e:Consignment/e:hasIdentifier" /></cac:ID>
                <cac:Description><xsl:value-of select="e:containsConsignment/e:Consignment/e:hasDescription" /></cac:Description>
                <cac:NetWeightMeasure><xsl:value-of select="e:containsConsignment/e:Consignment/e:hasNetWeight" /></cac:NetWeightMeasure>
                <cac:Quantity><xsl:value-of select="e:containsConsignment/e:Consignment/e:hasTotalTransportHandlingUnitQuantity" /></cac:Quantity>
                <cac:HazardousRiskIndicator><xsl:value-of select="e:containsConsignment/e:Consignment/e:hasHazardousRisk" /></cac:HazardousRiskIndicator>
                <cac:Item>
                    <cbc:hasIdentifier><xsl:value-of select="e:containsConsignment/e:Consignment/e:containsTransportHandlingUnit/e:Package/e:hasIdentifier" /></cbc:hasIdentifier>
                </cac:Item>
            </cac:ReferencedGoodsItem>
        </ns1:TransportExecutionPlanRequest>
    </xsl:template>

</xsl:stylesheet>
