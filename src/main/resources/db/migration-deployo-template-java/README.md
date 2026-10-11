Migrations Flyway em SQL puro (dialeto PostgreSQL), aplicadas em todos os perfis (`docker`,
`staging`, `production`). Nomeie os arquivos `V<N>__descricao_curta.sql` (ex.:
`V1__create_widgets_table.sql`).

A pasta é `db/migration-<serviço>`, nunca a padrão `db/migration` — nem como pasta-mãe, já que a
varredura do Flyway é recursiva. As tabelas vão para o schema próprio do serviço, não para o
`public`. Ver "Banco de dados: um schema por serviço" em `memory/constitution.md`.

TODO (ao usar este template): renomear esta pasta para `db/migration-<nome-do-serviço>` e
ajustar `spring.flyway.locations` em `application.yml`.
