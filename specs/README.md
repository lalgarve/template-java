# Specs

Cada feature não trivial ganha uma pasta `NNN-nome-da-feature/` aqui (numeração sequencial
de 3 dígitos, nome em kebab-case), copiada a partir de `templates/`:

```
specs/
  001-nome-da-feature/
    spec.md            # requisitos + critérios de aceite (o QUÊ e POR QUÊ)
    plan.md             # decisões técnicas (o COMO)
    data-model.md         # só se a feature tiver dados persistentes
    contracts/
      cli-commands.md       # ou queue-message.md, etc. — só interfaces não-REST (REST vai em docs/openapi.yaml)
    tasks.md                # tarefas, espelhadas como Issues no GitHub
```

## Variante opcional: agrupar por iteração

Projetos que trabalham em iterações podem usar `NN-NNN-nome-da-feature/` no lugar de
`NNN-nome-da-feature/`:

- `NN` — número da iteração (2 dígitos, ex. `05`), mesmo número do label `iteration-N` da Issue;
- `NNN` — sequência da feature **dentro dessa iteração** (reinicia a cada iteração: a primeira
  feature da Iteração 5 é `05-001`, a primeira da Iteração 8 volta a ser `08-001`).

Escolher uma das duas formas no início do projeto e registrar aqui — não misturar.

Ver `memory/constitution.md` (seções "Documentação viva por feature" e "Rastreamento de
trabalho via Issues") para o fluxo completo: `spec.md`/`plan.md` são escritos antes do
código, cada feature tem uma Issue-épico no GitHub, e `tasks.md` é a fonte de verdade das
tarefas que essa Issue rastreia.
