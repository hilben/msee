<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:ns1="urn:gs1:ecom:transport_instruction:xsd:3"
    xmlns:sh="http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader"
    xmlns:time="http://www.w3.org/TR/owl-time/#"
    xmlns:e="http://efreight.sti2.at/ontology/crs#">

    <xsl:output method="xml" version="1.0" indent="yes" omit-xml-declaration="no" />


    <xsl:template match="/rdf:RDF">
        <ns1:transportInstructionMessage>
            <sh:StandardBusinessDocumentHeader>
                <sh:DocumentIdentification>
                    <sh:InstanceIdentifier><xsl:value-of select="e:Document/e:hasIdentifier"/></sh:InstanceIdentifier>
                    <sh:CreationDateAndTime><xsl:value-of select="e:Document/e:issuedAt"/></sh:CreationDateAndTime>
                </sh:DocumentIdentification>
            </sh:StandardBusinessDocumentHeader>
            <transportInstruction>
                <transportInstructionConsignment>
                    <ginc><xsl:value-of select="e:Consignment/e:hasIdentifier"/></ginc>
                    <consignor>
                        <ginc><xsl:value-of select="e:Consignment/e:hasConsignor//e:hasIdentifier"/></ginc>
                    </consignor>
                    <consignee>
                        <ginc><xsl:value-of select="e:Consignment/e:hasConsignee//e:hasIdentifier"/></ginc>
                    </consignee>
                    <transportCargoCharacteristics>
                        <cargoTypeCode><xsl:value-of select="e:Consignment/e:hasCargoType//e:hasStringID"/></cargoTypeCode>
                        <cargoTypeDescription><xsl:value-of select="e:Consignment/e:hasCargoType//e:hasDescription"/></cargoTypeDescription>
                        <totalGrossWeight measurmentUnitCode="{e:Consignment/e:hasGrossWeight//e:hasUnit}">
                            <xsl:value-of select="e:Consignment/e:hasGrossWeight//e:hasValue"/>
                        </totalGrossWeight>
                        <totalGrossVolume measurmentUnitCode="{e:Consignment/e:hasGrossVolume//e:hasUnit}">
                            <xsl:value-of select="e:Consignment/e:hasGrossVolume//e:hasValue"/>
                        </totalGrossVolume>
                        <totalPackageQuantity>
                            <xsl:value-of select="e:Consignment/e:hasTotalPackageQuantity//e:hasValue"/>
                        </totalPackageQuantity>
                    </transportCargoCharacteristics>
                    <transportInstructionTransportMovement>
                    	<sequenceNumber>
                    		<xsl:value-of select="e:Consignment/e:hasTransportEvent/e:DeliveryTransportEvent/e:hasSequenceNumber/e:Identifier/e:hasNumericalID" />
                    	</sequenceNumber>
                    	<transportModeTypeCode>
                    		<xsl:value-of select="e:Consignment/e:hasTransportEvent/e:DeliveryTransportEvent/e:hasTypeCode/owl:Thing/e:hasStringID" />
                    	</transportModeTypeCode>
                    	<plannedDeparture>
                    		<logisticLocation>
                    			<address>
                    				<city>
                    					<xsl:value-of select="e:Consignment/e:hasTransportEvent/e:DeliveryTransportEvent/e:hasTransportEvent/e:DepartureTransportEvent/e:hasLocation//e:hasCityName" />
                    				</city>
                    			</address>
                    		</logisticLocation>
                    		<logisticEventDateTime>
                    			<date>
                    				<xsl:value-of select="e:Consignment/e:hasTransportEvent/e:DeliveryTransportEvent/e:hasTransportEvent/e:DepartureTransportEvent/e:hasTime/time:TemporalEntity" />
                    			</date>
                    		</logisticEventDateTime>
                    	</plannedDeparture>
                    	<plannedArrival>
                    		<logisticLocation>
                    			<address>
                    				<city>
                    					<xsl:value-of select="e:Consignment/e:hasTransportEvent/e:ArrivalTransportEvent/e:hasLocation//e:hasCityName" />
                    				</city>
                    			</address>
                    		</logisticLocation>
                    	</plannedArrival>
                    </transportInstructionTransportMovement>	
                    
                    <xsl:for-each select="e:Consignment/e:containsTransportHandlingUnit">
                        <packageTotal>
                            <packageTypeCode><xsl:value-of select="//e:hasTypeCode"/></packageTypeCode>
                            <totalPackageQuantity><xsl:value-of select="//e:hasTotalPackageQuantity"/></totalPackageQuantity>
                        </packageTotal>
                    </xsl:for-each>
                    <transportInstructionConsignmentItem>
                        <lineItemNumber><xsl:value-of select="e:TransportHandlingUnit/e:hasSequenceNumber//e:hasNumericalID"/></lineItemNumber>
                        <xsl:for-each select="e:TransportHandlingUnit/e:containsTransportHandlingUnit">
                            <logisticUnit>
                                <sscc><xsl:value-of select=".//e:hasIdentifier"/></sscc>
                                <packageTypeCode><xsl:value-of select=".//e:hasTypeCode//e:hasStringID"/></packageTypeCode>
                            </logisticUnit>
                        </xsl:for-each>
                    </transportInstructionConsignmentItem>
                </transportInstructionConsignment>
            </transportInstruction>
        </ns1:transportInstructionMessage>
    </xsl:template>

</xsl:stylesheet>
