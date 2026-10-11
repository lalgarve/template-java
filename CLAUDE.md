# CLAUDE.md

Orientação rápida para trabalhar neste repositório. Antes de qualquer commit, Issue ou PR, leia
**[`memory/constitution.md`](memory/constitution.md)** — é a fonte da verdade para processo e
convenções deste projeto (fluxo SDD, formato de commit, branches/PRs, testes, onde cada tipo de
documentação mora). Este arquivo não repete a constitution: só resume as regras que mais causam
erro, para que fiquem visíveis logo no início de cada sessão.

## Regras que mais causam erro

<!--
Mantenha esta seção curta. Quando uma regra da constitution for violada na prática, acrescente-a
aqui com uma linha sobre o que aconteceu (ex.: "Já aconteceu: PR #N aberta em português") — o
registro do erro concreto é o que faz a regra ser lembrada. Detalhe completo fica na constitution.
-->

- **Idioma**: código, commits, Issues e PRs (título e descrição) em inglês; documentação de
  projeto (`README.md`, `docs/*.md`, `specs/**/*.md`) em português. O GitHub usa o título da PR
  como corpo do merge commit — escrever o texto em inglês desde o início, não "traduzir depois".
- **Nunca começar a implementar sem pedido explícito** ("pode implementar"). Discutir/decidir não
  é sinal verde para código, por mais simples que a mudança pareça.
- **Nunca abrir, editar, mesclar ou fechar PR, nem criar/editar Issue, sem pedido explícito.** O
  padrão é commit e push no branch. `.claude/settings.json` pede confirmação para essas
  ferramentas — não remover essas regras para "destravar" uma sessão.
- **Baseline Java**: não sugerir nem iniciar upgrade de LTS durante uma tarefa não relacionada;
  reportar a incompatibilidade e aguardar decisão.
- **Testes exigem a infraestrutura de pé** (`docker compose up -d --wait db` antes de
  `mvn verify`); nenhum teste se pula sozinho por falta dela.

## O projeto

<!-- TODO (ao usar este template): 2-3 frases sobre o que o projeto faz, e uma tabela de
módulos/serviços quando houver mais de um. -->

## Onde procurar o quê

- **Processo/convenções** → `memory/constitution.md`.
- **Requisito + decisão técnica de uma feature** → `specs/NNN-slug/` (`spec.md`/`plan.md`/
  `tasks.md`), a partir de `templates/`.
- **Contrato de API** → `docs/openapi.yaml` (contract-first).
- **Diário de desenvolvimento** (não é documentação de produto) → `docs/context/diario.md`.

## Lembretes rápidos (detalhe completo em `memory/constitution.md`)

- Commit: `<tipo>: <resumo curto, no imperativo>` — tipos `feat|fix|refactor|test|docs|chore|
  decision`; hook em `.githooks/` (`git config core.hooksPath .githooks`).
- Nunca commitar direto em `main`; um branch por linha de trabalho, reaproveitado entre
  sessões/ferramentas.
- Task de `tasks.md` riscada na mesma PR que a implementa.
- Toda classe de exceção criada tem um teste que a lança, e todo branch de exceção é exercitado.
- Banco: schema próprio do serviço, nunca o `public`; migrations em `db/migration-<serviço>`.
- Código de teste/dev nunca dentro de um módulo de produção, nem atrás de profile/flag.
