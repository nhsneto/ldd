<table><caption>Funcionários</caption><tr><th>Funcionário</th><th>Supervisor</th></tr>{
  for $emp in //employee
  let $sup := //employee[@id = $emp/supervisor/text()]
  return <tr><td>{$emp/firstname || " " || $emp/lastname}</td>
  {if (exists($sup)) then <td>{$sup/firstname || " " || $sup/lastname}</td>
  else <td></td>}</tr>
}</table>