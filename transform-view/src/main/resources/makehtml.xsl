<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0">

    <xsl:output method="html"/>

    <xsl:template match="/staff">
        <table border="1"><xsl:apply-templates/></table>
    </xsl:template>

    <xsl:template match="/staff/person">
        <tr><xsl:apply-templates/></tr>
    </xsl:template>

    <xsl:template match="/staff/person/firstName">
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match="/staff/person/secondName">
        <td>$<xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match="/staff/employee/email">
        <td>$<xsl:apply-templates/></td>
    </xsl:template>

</xsl:stylesheet>