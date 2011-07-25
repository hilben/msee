<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:dum="http://sesa.sti2.at/services/dummy/"
  xmlns:e="http://demo.sti2.at/efreight/ontologies/FalFormsOntology.rdf#">

  <xsl:output method="xml" version="1.0" indent="yes"
    omit-xml-declaration="no" />

  <xsl:variable name="id">
    <xsl:value-of
      select="/dum:submitFALFormResponse/GeneralDeclarationResponse/ID" />
  </xsl:variable>

  <xsl:variable name="shipid">
    <xsl:value-of
      select="/dum:submitFALFormResponse/GeneralDeclarationResponse/shipID" />
  </xsl:variable>

  <xsl:template match="/dum:submitFALFormResponse">
    <rdf:RDF>
      <e:GeneralDeclarationF1 rdf:about="{$id}">
        <e:hasShip rdf:resource="#ship_{$shipid}" />
      </e:GeneralDeclarationF1>

      <e:Ship rdf:about="#ship_{$shipid}">
        <e:hasCallSign>
          <xsl:copy-of select="$shipid" />
        </e:hasCallSign>
      </e:Ship>
    </rdf:RDF>
  </xsl:template>

</xsl:stylesheet>