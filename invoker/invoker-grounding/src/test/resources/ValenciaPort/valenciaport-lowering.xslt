<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:e="http://demo.sti2.at/efreight/ontologies/FalFormsOntology.rdf#">

        <xsl:output method="xml" version="1.0"
                indent="yes"
                standalone="yes"
                omit-xml-declaration="no"/>

        <xsl:template match="/rdf:RDF">
                <dum:submitFALForm xmlns:dum='http://sesa.sti2.at/services/dummy/'>
                        <falForm>
                                <arravialOrDepartureTime>
                                        <xsl:value-of select="./e:GeneralDeclarationF1/e:hasArrivalOrDeparture/@rdf:resource"/>
                                </arravialOrDepartureTime>
                                <generalDeclarationId>
                                        <xsl:value-of select="./e:GeneralDeclarationF1/@rdf:about"/>
                                </generalDeclarationId>
                                <lastPortOfCall>
                                        <portCode>
                                                <xsl:value-of select="./e:GeneralDeclarationF1/e:hasLastPortOfCall//e:hasPortCode"/>
                                        </portCode>
                                        <portName>
                                                <xsl:value-of select="./e:GeneralDeclarationF1/e:hasLastPortOfCall//e:hasPortName"/>
                                        </portName>
                                </lastPortOfCall>
                                <numberOfCrew>
                                        <xsl:value-of select="./e:GeneralDeclarationF1/e:hasNumberOfCrew"/>
                                </numberOfCrew>
                                <numberOfPassengers>
                                        <xsl:value-of select="./e:GeneralDeclarationF1/e:hasNumberOfPassengers"/>
                                </numberOfPassengers>
                                <ports>
                                        <portCode>
                                                <xsl:value-of select="./e:GeneralDeclarationF1/e:hasPort//e:hasPortCode"/>
                                        </portCode>
                                        <portName>
                                                <xsl:value-of select="./e:GeneralDeclarationF1/e:hasPort//e:hasPortName"/>
                                        </portName>
                                </ports>
                                <ship>
                                        <callSign>
                                                <xsl:value-of select="./e:Ship/e:hasCallSign"/>
                                        </callSign>
                                        <flagState>
                                                <countryCode>
                                                        <xsl:value-of select="./e:Ship/e:hasFlagState//e:hasCountryCode"/>
                                                </countryCode>
                                                <countryName>
                                                        <xsl:value-of select="./e:Ship/e:hasFlagState//e:hasCountryName"/>
                                                </countryName>
                                        </flagState>
                                        <grossTonnage>
                                                <xsl:value-of select="./e:Ship/e:hasGrossTonnage"/>
                                        </grossTonnage>
                                        <IMONumber>
                                                <xsl:value-of select="./e:Ship/e:hasIMONumber"/>
                                        </IMONumber>
                                        <netTonnage>
                                                <xsl:value-of select="./e:Ship/e:hasNetTonnage"/>
                                        </netTonnage>
                                        <shipMasterName>
                                                <xsl:value-of select="./e:Ship/e:hasShipMasterName"/>
                                        </shipMasterName>
                                        <shipName>
                                                <xsl:value-of select="./e:Ship/e:hasShipName"/>
                                        </shipName>
                                        <shipType>
                                                <xsl:value-of select="./e:Ship/e:hasType"/>
                                        </shipType>
                                </ship>
                        </falForm>
                </dum:submitFALForm>
        </xsl:template>

</xsl:stylesheet>
