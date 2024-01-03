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
                    <xsl:for-each select="distinct-values(copa//jogo/@mandante)">

                        <xsl:variable name="golsMarcados" select="n:getGolsMarcados(.)" />
                        <xsl:variable name="golsSofridos" select="n:getGolsSofridos(.)" />

                        <tr>
                            <td>
                                <xsl:value-of select="." />
                            </td>
                            <td>
                                <xsl:value-of select="n:getNumeroVitorias(.)" />
                            </td>
                            <td>
                                <xsl:value-of select="n:getNumeroDerrotas(.)" />
                            </td>
                            <td>
                                <xsl:value-of select="n:getNumeroEmpates(.)" />
                            </td>
                            <td>
                                <xsl:value-of select="$golsMarcados" />
                            </td>
                            <td>
                                <xsl:value-of select="$golsSofridos" />
                            </td>
                            <td>
                                <xsl:value-of select="$golsMarcados - $golsSofridos" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

    <!-- Refatorar este codigo futuramente -->


    <xsl:function name="n:getNumeroVitorias">
        <xsl:param name="nationalTeam" as="xs:string" />

        <xsl:variable name="jogosMan" select="document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@mandante = $nationalTeam]" />
        <xsl:variable name="jogosVis" select="document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@visitante = $nationalTeam]" />

        <xsl:variable name="nVitoriasMan"
            select="count($jogosMan/eventos[count(evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')]) &gt; count(evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')])])" />

        <xsl:variable name="nVitoriasVis"
            select="count($jogosVis/eventos[count(evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')]) &gt; count(evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')])])" />

        <xsl:value-of select="$nVitoriasMan + $nVitoriasVis" />
    </xsl:function>


    <xsl:function name="n:getNumeroDerrotas">
        <xsl:param name="nationalTeam" as="xs:string" />

        <xsl:variable name="jogosMan" select="document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@mandante = $nationalTeam]" />
        <xsl:variable name="jogosVis" select="document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@visitante = $nationalTeam]" />

        <xsl:variable name="nDerrotasMan"
            select="count($jogosMan/eventos[count(evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')]) &lt; count(evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')])])" />

        <xsl:variable name="nDerrotasVis"
            select="count($jogosVis/eventos[count(evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')]) &lt; count(evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')])])" />

        <xsl:value-of select="$nDerrotasMan + $nDerrotasVis" />
    </xsl:function>


    <xsl:function name="n:getNumeroEmpates">
        <xsl:param name="nationalTeam" as="xs:string" />

        <xsl:variable name="jogosMan" select="document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@mandante = $nationalTeam]" />
        <xsl:variable name="jogosVis" select="document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@visitante = $nationalTeam]" />

        <xsl:variable name="nEmptMan" select="count($jogosMan[not(eventos)])" />
        <xsl:variable name="nEmptVis" select="count($jogosVis[not(eventos)])" />

        <xsl:variable name="nEmpatesMan"
            select="count($jogosMan/eventos[count(evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')]) = count(evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')])])" />

        <xsl:variable name="nEmpatesVis"
            select="count($jogosVis/eventos[count(evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')]) = count(evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')])])" />

        <xsl:value-of select="$nEmpatesMan + $nEmpatesVis + $nEmptMan + $nEmptVis" />
    </xsl:function>


    <xsl:function name="n:getGolsMarcados">
        <xsl:param name="nationalTeam" as="xs:string" />

        <xsl:variable name="golsMarcadosMan"
            select="count(document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@mandante = $nationalTeam]//evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')])" />
        <xsl:variable name="golsMarcadosVis"
            select="count(document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@visitante = $nationalTeam]//evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')])" />

        <xsl:value-of select="$golsMarcadosMan + $golsMarcadosVis" />
    </xsl:function>


    <xsl:function name="n:getGolsSofridos">
        <xsl:param name="nationalTeam" as="xs:string" />

        <xsl:variable name="golsSofridosMan"
            select="count(document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@mandante = $nationalTeam]//evento[(@time = 'visitante' and not(@type = 'contra')) or (@time = 'mandante' and @type = 'contra')])" />
        <xsl:variable name="golsSofridosVis"
            select="count(document('Copa2018.xml')//fase[@tipo = 'grupos']//jogo[@visitante = $nationalTeam]//evento[(@time = 'mandante' and not(@type = 'contra')) or (@time = 'visitante' and @type = 'contra')])" />

        <xsl:value-of select="$golsSofridosMan + $golsSofridosVis" />
    </xsl:function>

</xsl:stylesheet>
