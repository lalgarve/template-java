# Contrato de interface: <nome da feature>

Escrito antes da implementação — a implementação segue o contrato. Equivalente a um contrato
OpenAPI para uma API REST; o exemplo abaixo é para uma interface de linha de comando. Adapte
a estrutura (e o nome do arquivo, ex. `endpoints.md`) ao tipo de interface que a feature
expõe.

## Comando: `<comando>`

**Uso:**
```
<nome-do-binario> <comando> [opções]
```

**Argumentos:**

| Argumento | Obrigatório | Descrição |
|---|---|---|
| `--<flag>` | sim/não | <descrição> |

**Saída (stdout), sucesso:**
```
<exemplo de saída>
```

**Exit codes:**

| Código | Significado |
|---|---|
| 0 | Sucesso |
| 1 | <erro esperado> |

**Erros (stderr):**

| Condição | Mensagem | Exit code |
|---|---|---|
| <condição> | <mensagem> | <código> |
