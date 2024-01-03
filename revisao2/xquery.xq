let $jogos :=
for $j in copa//jogo
order by count($j//evento) descending
return <j mandante="{$j/@mandante}" visitante="{$j/@visitante}" gols="{count($j//evento)}" />

for $j in $jogos[@gols = max($jogos/@gols)]
return copa//jogo[@mandante = $j/@mandante and @visitante = $j/@visitante]