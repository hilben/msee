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
                <e:hasStringID><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:InstanceIdentifier" /></e:hasStringID>
                <e:issuedAt><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:CreationDateAndTime" /></e:issuedAt>
                <e:hasTypeVersion><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:TypeVersion"/></e:hasTypeVersion>
                <e:hasStandard><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:DocumentIdentification/sh:Standard"/></e:hasStandard>
                <e:reportsConsignment rdf:resource="#{$conid}" />
                <e:Sender>
                    <e:hasStringID><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:Sender/sh:Identifier" /></e:hasStringID>
                    <e:hasAuthority><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:Sender/sh:Identifier/@Authority" /></e:hasAuthority>
                </e:Sender>
                <e:Receiver>
                    <e:hasStringID><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:Receiver/sh:Identifier" /></e:hasStringID>
                    <e:hasAuthority><xsl:value-of select="sh:StandardBusinessDocumentHeader/sh:Receiver/sh:Identifier/@Authority" /></e:hasAuthority>
                </e:Receiver>
            </e:Document>

            <e:Consignment rdf:about="#{$conid}">
                <e:hasStringID><xsl:value-of select="$conid"/></e:hasStringID>
                <e:issuedAt><xsl:value-of select="transportInstruction/creationDateTime"/></e:issuedAt>
                <e:hasStatusCode><xsl:value-of select="transportInstruction/documentStatusCode"/></e:hasStatusCode>
                <e:hasActionCode><xsl:value-of select="transportInstruction/documentActionCode"/></e:hasActionCode>
                <e:hasLogisticServicesBuyer>
                    <e:Party>
                        <e:hasStringID><xsl:value-of select="transportInstruction/logisticServicesBuyer/gln"/></e:hasStringID>
                        <e:hasAdditionalIdentifier><xsl:value-of select="transportInstruction/logisticServicesBuyer/additionalPartyIdentification"/></e:hasAdditionalIdentifier>
                    </e:Party>
                </e:hasLogisticServicesBuyer>
                <e:hasConsignor>
                    <e:LegalEntity>
                        <e:hasStringID><xsl:value-of select="transportInstruction/transportInstructionConsignment/consignor/gln"/></e:hasStringID>
                    </e:LegalEntity>
                </e:hasConsignor>
                <e:hasConsignee>
                    <e:LegalEntity>
                        <e:hasStringID><xsl:value-of select="transportInstruction/transportInstructionConsignment/consignee/gln"/></e:hasStringID>
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
                <!-- logisticServicesBuyer -->
                <e:hasLogisticServicesBuyer>
                    <e:Party>
                        <e:hasStringID><xsl:value-of select="transportInstruction/logisticsServicesBuyer/gln"/></e:hasStringID>
                        <e:hasAdditionalInformation><xsl:value-of select="transportInstruction/logisticsServicesBuyer/additionalPartyIdentification"/></e:hasAdditionalInformation>
                    </e:Party>
                </e:hasLogisticServicesBuyer>

                <e:hasPickUpParty>
                    <e:Party>
                    </e:Party>
                </e:hasPickUpParty>

                <e:hasDropOffParty>
                    <e:Party>
                    </e:Party>
                </e:hasDropOffParty>

                <e:hasExportAgent>
                    <e:Party>
                    </e:Party>
                </e:hasExportAgent>

                <e:hasImportAgent>
                    <e:Party>
                    </e:Party>
                </e:hasImportAgent>

                <e:hasPlannedPickUp>
                    <e:PickupTransportEvent>
                        <e:hasTypeCode>
                            <owl:Thing>
                                <e:hasStringID><xsl:value-of select="transportInstruction/transportInstructionConsignment/plannedPickUp/logisticEventTypeCode" /></e:hasStringID>
                            </owl:Thing>
                        </e:hasTypeCode>
                        <e:hasLocation>
                            <e:Address>
                                <e:hasCountryCode><xsl:value-of select="transportInstruction/transportInstructionConsignment/plannedPickUp/countryCode" /></e:hasCountryCode>
                                <e:hasLocation>
                                    <e:LocationCoordinate><xsl:value-of select="transportInstruction/transportInstructionConsignment/plannedPickUp/geographicalCoordinates" /></e:LocationCoordinate>
                                </e:hasLocation>
                            </e:Address>
                        </e:hasLocation>
                    </e:PickupTransportEvent>
                </e:hasPlannedPickUp>

                <e:hasPlannedDropOff>
                    <e:DropoffTransportEvent>
                        <e:hasTypeCode>
                            <owl:Thing>
                                <e:hasStringID><xsl:value-of select="transportInstruction/transportInstructionConsignment/plannedPickUp/logisticEventTypeCode" /></e:hasStringID>
                            </owl:Thing>
                        </e:hasTypeCode>
                        <e:hasLocation>
                            <e:Address>
                                <e:hasCountryCode><xsl:value-of select="transportInstruction/transportInstructionConsignment/plannedDropOff/countryCode" /></e:hasCountryCode>
                                <e:hasLocation>
                                    <e:LocationCoordinate><xsl:value-of select="transportInstruction/transportInstructionConsignment/plannedDropOff/geographicalCoordinates" /></e:LocationCoordinate>
                                </e:hasLocation>
                            </e:Address>
                        </e:hasLocation>
                    </e:DropoffTransportEvent>
                </e:hasPlannedDropOff>

            </e:Consignment>

            <xsl:for-each select="transportInstruction/transportInstructionConsignment/includedTransportEquipment">
                <e:containsTransportHandlingUnit>
                    <e:TransportEquipment>
                        <e:hasTypeCode>
                            <owl:Thing>
                                <e:hasStringID><xsl:value-of select="transportEquipmentTypeCode"/></e:hasStringID>
                            </owl:Thing>
                        </e:hasTypeCode>
                        <e:hasWeight>
                            <e:Measure>
                                <e:hasValue><xsl:value-of select="transportEquipmentWeight"/></e:hasValue>
                                <e:hasUnit><xsl:value-of select="transportEquipmentWeight/@measurementUnitCode"/></e:hasUnit>
                            </e:Measure>
                        </e:hasWeight>
                        <e:hasDepth>
                            <e:Measure>
                                <e:hasValue><xsl:value-of select="dimension/depth"/></e:hasValue>
                                <e:hasUnit><xsl:value-of select="dimension/depth/@measurementUnitCode"/></e:hasUnit>
                            </e:Measure>
                        </e:hasDepth>
                        <e:hasHeight>
                            <e:Measure>
                                <e:hasValue><xsl:value-of select="dimension/height"/></e:hasValue>
                                <e:hasUnit><xsl:value-of select="dimension/height/@measurementUnitCode"/></e:hasUnit>
                            </e:Measure>
                        </e:hasHeight>
                        <e:hasWidth>
                            <e:Measure>
                                <e:hasValue><xsl:value-of select="dimension/width"/></e:hasValue>
                                <e:hasUnit><xsl:value-of select="dimension/width/@measurementUnitCode"/></e:hasUnit>
                            </e:Measure>
                        </e:hasWidth>
                    </e:TransportEquipment>
                </e:containsTransportHandlingUnit>
            </xsl:for-each>

            <xsl:for-each select="transportInstruction/transportInstructionConsignment/transportInstructionConsignmentItem">
                <e:containsTransportHandlingUnit>
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
                                    <e:hasStringID><xsl:value-of select="sscc"/></e:hasStringID>
                                    <e:hasTypeCode>
                                        <owl:Thing>
                                            <e:hasStringID><xsl:value-of select="packageTypeCode"/></e:hasStringID>
                                        </owl:Thing>
                                    </e:hasTypeCode>
                                </e:TransportHandlingUnit>
                            </e:containsTransportHandlingUnit>
                        </xsl:for-each>
                    </e:TransportHandlingUnit>
                </e:containsTransportHandlingUnit>
            </xsl:for-each>
        </rdf:RDF>
    </xsl:template>

</xsl:stylesheet>
