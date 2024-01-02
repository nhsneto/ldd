<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" indent="yes" />

<xsl:template match="/">
    <html lang="pt-br">
        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>Tabela de Funcionários</title>
        </head>
        <body>
            <table>
                <caption>Tabela de Funcionários</caption>
                <tr>
                    <th>Nome</th>
                    <th>Sobrenome</th>
                    <th>Cargo</th>
                    <th>Data de Admissão</th>
                    <th>Salário</th>
                </tr>
                <xsl:apply-templates select="/employees/employee">
                    <xsl:sort select="salary" order="descending" data-type="number" />
                </xsl:apply-templates>
            </table>
        </body>
    </html>
</xsl:template>

<xsl:template match="employee">
    <tr>
        <td><xsl:value-of select="firstname" /></td>
        <td><xsl:value-of select="lastname" /></td>
        <td><xsl:value-of select="title" /></td>
        <td><xsl:value-of select="@admissionDate" /></td>
        <td><xsl:value-of select="format-number(salary, 'R$ #.###,00', 'real')" /></td>
    </tr>
</xsl:template>

<xsl:decimal-format name="real" decimal-separator="," grouping-separator="."/>

</xsl:stylesheet>