Migrations Flyway em SQL puro (dialeto PostgreSQL), aplicadas nos perfis `docker`,
`staging` e `production` — ver `application-docker.yml`.

Mantenha esta pasta em espelho com `db/migration-h2` (mesmo conteúdo, mesma numeração) sempre
que a sintaxe for compatível entre os dois motores — o H2 roda em modo de compatibilidade
PostgreSQL (`MODE=PostgreSQL`), então isso costuma ser o caso. Nomeie os arquivos
`V<N>__descricao_curta.sql` (ex.: `V1__create_widgets_table.sql`).
