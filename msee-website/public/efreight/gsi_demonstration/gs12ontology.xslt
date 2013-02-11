<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:ns1="urn:gs1:ecom:transport_instruction:xsd:3"
    xmlns:sh="http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader"
    xmlns:e="http://efreight.sti2.at/ontology/crs#">

    <xsl:output method="xml" version="1.0" indent="yes" omit-xml-declaration="no" />

    <xsl:variable name="docid">
        <xsl:value-of select="/ns1:transportInstructionMessage/sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:InstanceIdentifier" />
    </xsl:variable>
    <xsl:variable name="conid">
        <xsl:value-of select="/ns1:transportInstructionMessage/transportInstruction/transportInstructionConsignment/ginc" />
    </xsl:variable>

    <xsl:template match="/ns1:transportInstructionMessage">
        <rdf:RDF>
            <e:Document rdf:about="#{$docid}">
                <e:hasIdentifier><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:InstanceIdentifier" /></e:hasIdentifier>
                <e:issuedAt><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:CreationDateAndTime" /></e:issuedAt>
                <e:reportsConsignment rdf:resource="#{$conid}" />
            </e:Document>

            <e:Consignment rdf:about="#{$conid}">
                <e:hasIdentifier><xsl:value-of select="$conid"/></e:hasIdentifier>
                <e:hasConsignor>
                    <e:LegalEntity>
                        <e:hasIdentifier><xsl:value-of select="transportInstruction/transportInstructionConsignment/consignor/gln"/></e:hasIdentifier>
                    </e:LegalEntity>
                </e:hasConsignor>
                <e:hasConsignee>
                    <e:LegalEntity>
                        <e:hasIdentifier><xsl:value-of select="transportInstruction/transportInstructionConsignment/consignee/gln"/></e:hasIdentifier>
                    </e:LegalEntity>
                </e:hasConsignee>
                <e:hasCargoType>
                    <owl:Thing>
                        <e:hasStringID><xsl:value-of select="transportInstruction/transportInstructionConsignment/transportCargoCharacteristics/cargoTypeCode" /></e:hasStringID>
                        <e:hasDescription><xsl:value-of select="transportInstruction/transportInstructionConsignment/transportCargoCharacteristics/cargoTypeDescription" /></e:hasDescription>
                    </owl:Thing>
                </e:hasCargoType>
                <e:hasGrossVolume>
                    <owl:Thing>
                        <e:hasValue><xsl:value-of select="transportInstruction/transportInstructionConsignment/transportCargoCharacteristics/totalGrossVolume" /></e:hasValue>
                        <e:hasUnit><xsl:value-of select="transportInstruction/transportInstructionConsignment/transportCargoCharacteristics/totalGrossVolume/@measurementUnitCode" /></e:hasUnit>
                    </owl:Thing>
                </e:hasGrossVolume>
                <e:hasGrossWeight>
                    <owl:Thing>
                        <e:hasValue><xsl:value-of select="transportInstruction/transportInstructionConsignment/transportCargoCharacteristics/totalGrossWeight" /></e:hasValue>
                        <e:hasUnit><xsl:value-of select="transportInstruction/transportInstructionConsignment/transportCargoCharacteristics/totalGrossWeight/@measurementUnitCode" /></e:hasUnit>
                    </owl:Thing>
                </e:hasGrossWeight>
                <xsl:for-each select="transportInstruction/transportInstructionConsignment/packageTotal">
                    <e:containsTransportHandlingUnit>
                        <e:Package>
                            <e:hasTypeCode><xsl:value-of select="packageTypeCode"/></e:hasTypeCode>
                            <e:hasTotalPackageQuantity><xsl:value-of select="totalPackageQuantity"/></e:hasTotalPackageQuantity>
                        </e:Package>
                    </e:containsTransportHandlingUnit>
                </xsl:for-each>
            </e:Consignment>

            <xsl:for-each select="transportInstruction/transportInstructionConsignment/transportInstructionConsignmentItem">
                <e:TransportHandlingUnit>
                    <e:isContainedInConsignment rdf:resource="{$conid}"/>
                    <e:hasSequenceNumber>
                        <e:Identifier>
                            <e:hasNumericalID><xsl:value-of select="lineItemNumber"/></e:hasNumericalID>
                        </e:Identifier>
                    </e:hasSequenceNumber>
                    <xsl:for-each select="logisticUnit">
                        <e:containsTransportHandlingUnit>
                            <e:TransportHandlingUnit>
                                <e:hasIdentifier><xsl:value-of select="sscc"/></e:hasIdentifier>
                                <e:hasTypeCode>
                                    <owl:Thing>
                                        <e:hasStringID><xsl:value-of select="packageTypeCode"/></e:hasStringID>
                                    </owl:Thing>
                                </e:hasTypeCode>
                            </e:TransportHandlingUnit>
                        </e:containsTransportHandlingUnit>
                    </xsl:for-each>
                </e:TransportHandlingUnit>
            </xsl:for-each>
        </rdf:RDF>
    </xsl:template>

</xsl:stylesheet>
