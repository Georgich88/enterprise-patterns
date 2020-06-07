<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/person">
        <xsl:variable name="email" select="email"/>
        <xsl:variable name="firstName" select="firstName"/>
        <xsl:variable name="lastName" select="lastName"/>
        <form action="welcome.jsp"  method="post">
            <table>
                <tr><td>Email:</td><td><input type="text" name="email" value="{$email}"></input></td></tr>
                <tr><td>First name:</td><td><input type="text" name="firstName" value="{$firstName}"/></td></tr>
                <tr><td>Last name:</td><td><input type="text" name="lastName" value="{$lastName}"/></td></tr>
                <tr><td></td><td><input type="submit" value="Submit"/></td></tr>
            </table>
        </form>
    </xsl:template>
</xsl:stylesheet>