<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:ns1="urn:gs1:ecom:transport_instruction:xsd:3"
    xmlns:sh="http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader"
    xmlns:e="http://efreight.sti2.at/ontology/crs#">

    <xsl:output method="xml" version="1.0" indent="yes" omit-xml-declaration="no" />

    <xsl:variable name="sender_authority">
        <xsl:value-of select="/rdf:RDF/e:Document/e:Sender/e:hasAuthority" />
    </xsl:variable>

    <xsl:variable name="receiver_authority">
        <xsl:value-of select="/rdf:RDF/e:Document/e:Receiver/e:hasAuthority" />
    </xsl:variable>

    <xsl:template match="/rdf:RDF">
        <ns1:transportInstructionMessage>
            <sh:StandardBusinessDocumentHeader>
                <sh:HeaderVersion>1.0</sh:HeaderVersion>
                <sh:Sender>
                    <sh:Identifier Authority="{$sender_authority}"><xsl:value-of select="e:Document/e:Sender/e:hasStringID" /></sh:Identifier>
                </sh:Sender>
                <sh:Receiver>
                    <sh:Identifier Authority="{$receiver_authority}"><xsl:value-of select="e:Document/e:Receiver/e:hasStringID" /></sh:Identifier>
                </sh:Receiver>
                <sh:DocumentIdentification>
                    <sh:Standard><xsl:value-of select="e:Document/e:hasStandard"/></sh:Standard>
                    <sh:TypeVersion><xsl:value-of select="e:Document/e:hasTypeVersion"/></sh:TypeVersion>
                    <sh:InstanceIdentifier><xsl:value-of select="e:Document/e:hasStringID"/></sh:InstanceIdentifier>
                    <sh:CreationDateAndTime><xsl:value-of select="e:Document/e:issuedAt"/></sh:CreationDateAndTime>
                </sh:DocumentIdentification>
            </sh:StandardBusinessDocumentHeader>
            <transportInstruction>
                <creationDateTime><xsl:value-of select="e:Consignment/e:issuedAt" /></creationDateTime>
                <documentStatusCode><xsl:value-of select="e:Consignment/e:hasStatusCode" /></documentStatusCode>
                <documentActionCode><xsl:value-of select="e:Consignment/e:hasActionCode" /></documentActionCode>
                <logisticServicesBuyer>
                    <gln></gln>
                    <additionalPartyIdentification></additionalPartyIdentification>
                    <organisationDetails/>
                </logisticServicesBuyer>
                <transportInstructionConsignment>
                    <ginc><xsl:value-of select="e:Consignment/e:hasStringID"/></ginc>
                    <consignor>
                        <ginc><xsl:value-of select="e:Consignment/e:hasConsignor//e:hasStringID"/></ginc>
                    </consignor>
                    <consignee>
                        <ginc><xsl:value-of select="e:Consignment/e:hasConsignee//e:hasStringID"/></ginc>
                    </consignee>
                    <pickUpParty>
                        <organisationDetails/>
                    </pickUpParty>
                    <dropOffParty>
                        <organisationDetails/>
                    </dropOffParty>
                    <dropOffParty>
                        <organisationDetails/>
                    </dropOffParty>
                    <exportAgent>
                        <organisationDetails/>
                    </exportAgent>
                    <importAgent>
                        <organisationDetails/>
                    </importAgent>
                    <transportCargoCharacteristics>
                        <cargoTypeCode><xsl:value-of select="e:Consignment/e:hasCargoType//e:hasStringID"/></cargoTypeCode>
                        <cargoTypeDescription><xsl:value-of select="e:Consignment/e:hasCargoType//e:hasDescription"/></cargoTypeDescription>
                        <totalGrossWeight measurmentUnitCode="{e:Consignment/e:hasGrossWeight//e:hasUnit}">
                            <xsl:value-of select="e:Consignment/e:hasGrossWeight//e:hasValue"/>
                        </totalGrossWeight>
                        <totalGrossVolume measurmentUnitCode="{e:Consignment/e:hasGrossVolume//e:hasUnit}">
                            <xsl:value-of select="e:Consignment/e:hasGrossVolume//e:hasValue"/>
                        </totalGrossVolume>
                    </transportCargoCharacteristics>
                    <xsl:for-each select="e:Consignment/e:containsTransportHandlingUnit">
                        <packageTotal>
                            <packageTypeCode><xsl:value-of select="//e:hasTypeCode//e:hasStringID"/></packageTypeCode>
                            <totalPackageQuantity><xsl:value-of select="//e:hasTotalPackageQuantity"/></totalPackageQuantity>
                        </packageTotal>
                    </xsl:for-each>
                    <transportInstructionConsignmentItem>
                        <lineItemNumber><xsl:value-of select="e:TransportHandlingUnit/e:hasSequenceNumber//e:hasNumericalID"/></lineItemNumber>
                        <xsl:for-each select="e:TransportHandlingUnit/e:containsTransportHandlingUnit">
                            <logisticUnit>
                                <sscc><xsl:value-of select=".//e:hasStringID"/></sscc>
                                <packageTypeCode><xsl:value-of select=".//e:hasTypeCode//e:hasStringID"/></packageTypeCode>
                            </logisticUnit>
                        </xsl:for-each>
                    </transportInstructionConsignmentItem>
                </transportInstructionConsignment>
            </transportInstruction>
        </ns1:transportInstructionMessage>
    </xsl:template>

</xsl:stylesheet>
