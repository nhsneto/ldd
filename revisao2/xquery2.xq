let $jogadores := 
for $jog in distinct-values(copa//evento[not(@type = 'contra')]/jogador)
order by $jog
return <jogador gols="{count(//jogador[. = $jog])}">{$jog}</jogador>

return $jogadores[@gols = max($jogadores/@gols)]/text()
