# Contrato de interface: <nome da feature>

Escrito antes da implementação — a implementação segue o contrato.

**Endpoint REST?** Vai em `docs/openapi.yaml`, o contrato único e
cumulativo da API (ver "Documentação viva por feature" em `memory/constitution.md`) — adicionar o
endpoint lá, não aqui. Este template é para interfaces que o OpenAPI não cobre: CLI (exemplo
abaixo), contrato de mensagem de fila, ou o consumo de um serviço externo. Adapte a estrutura (e
o nome do arquivo, ex. `cli-commands.md`, `queue-message.md`, `external-client.md`) ao tipo de
interface que a feature expõe ou consome.

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
