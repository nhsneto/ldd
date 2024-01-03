<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:n="http://www.n.org/n"
    version="2.0">

    <xsl:output method="html" indent="yes" />

    <xsl:template match="/">
        <html lang="pt-br">
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <title>Copa 2018</title>
            </head>
            <body>
                <table>
                    <xsl:for-each select="copa//fase[@tipo = 'finais']//jogo">
                        <tr>
                            <td>
                                <xsl:value-of select="@mandante" />
                            </td>
                            <td>
                                <xsl:value-of select="count(eventos/evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')])" />
                            </td>
                            <td>
                                <xsl:value-of select="count(eventos/evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')])" />
                            </td>
                            <td>
                                <xsl:value-of select="@visitante" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>