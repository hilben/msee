<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:ns1="urn:oasis:names:specification:ubl:schema:xsd:TransportExecutionPlan-2"
    xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
    xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
    xmlns:e="http://efreight.sti2.at/ontology/crs#">

    <xsl:output method="xml" version="1.0" indent="yes" omit-xml-declaration="no" />

    <xsl:variable name="date" select="substring-before(/rdf:RDF/e:Document/e:issuedAt, 'T')" />
    <xsl:variable name="time" select="substring-after(/rdf:RDF/e:Document/e:issuedAt, 'T')" />

    <xsl:template match="/">
        <ns1:TransportExecutionPlanRequest>
            <cbc:ID><xsl:value-of select="e:hasIdentifier"/></cbc:ID>
            <cbc:IssueDate><xsl:value-of select="$date" /></cbc:IssueDate>
            <cbc:IssueTime><xsl:value-of select="$time" /></cbc:IssueTime>
            <cac:SenderParty>
                <cac:PartyName>
                    <cbc:hasIdentifier><xsl:value-of select="//e:hasConsignor//e:hasIdentifier" /></cbc:hasIdentifier>
                </cac:PartyName>
            </cac:SenderParty>
            <cac:ReceiverParty>
                <cac:PartyName>
                    <cbc:hasIdentifier><xsl:value-of select="//e:hasConsignee//e:hasIdentifier" /></cbc:hasIdentifier>
                </cac:PartyName>
            </cac:ReceiverParty>
            <cac:ReferencedGoodsItem>
                <cac:ID><xsl:value-of select="//e:Consignment/e:hasIdentifier" /></cac:ID>
                <cac:Description><xsl:value-of select="//e:Consignment/e:hasDescription" /></cac:Description>
                <cbc:GrossWeightMeasure>
                        <xsl:attribute name="unitCode">
                                <xsl:value-of select="//e:Consignment/e:hasGrossWeight//e:hasUnit" />
                        </xsl:attribute>
                        <xsl:value-of select="//e:Consignment/e:hasGrossWeight//e:hasValue" />
                </cbc:GrossWeightMeasure>
                <cbc:GrossVolumeMeasure>
                        <xsl:attribute name="unitCode">
                                <xsl:value-of select="//e:Consignment/e:hasGrossVolume//e:hasUnit" />
                        </xsl:attribute>
                        <xsl:value-of select="//e:Consignment/e:hasGrossVolume//e:hasValue" />
                </cbc:GrossVolumeMeasure>
                <cac:Quantity><xsl:value-of select="//e:Consignment//e:hasTotalTransportHandlingUnitQuantity" /></cac:Quantity>
                <cac:HazardousRiskIndicator><xsl:value-of select="//e:Consignment/e:hasHazardousRisk" /></cac:HazardousRiskIndicator>
                <xsl:for-each select="//e:Consignment/e:containsTransportHandlingUnit/e:Package">
                <cac:Item>
                    <cbc:hasIdentifier><xsl:value-of select="e:hasIdentifier" /></cbc:hasIdentifier>
                    <cac:hasTypeCode><xsl:value-of select="e:hasTypeCode" /></cac:hasTypeCode>
                    <cbc:Quantity><xsl:value-of select="e:hasTotalPackageQuantity" /></cbc:Quantity>
                </cac:Item>
                </xsl:for-each>
        </cac:ReferencedGoodsItem>
        <xsl:for-each select="//e:TransportHandlingUnit/e:hasIdentifier">
                <cac:TransportHandlingUnit>
                        <cbc:ID><xsl:value-of select="../e:hasIdentifier"/></cbc:ID>
                        <cac:hasTypeCode><xsl:value-of select="../e:hasTypeCode//e:hasStringID"/></cac:hasTypeCode>
                </cac:TransportHandlingUnit>
        </xsl:for-each>
        </ns1:TransportExecutionPlanRequest>
    </xsl:template>

</xsl:stylesheet>
