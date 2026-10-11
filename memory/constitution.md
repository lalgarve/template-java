# Como desenvolvemos software

Este documento descreve o **fluxo de trabalho e as convenções** usados neste projeto — não
as decisões técnicas específicas dele (essas ficam em `specs/*/plan.md`). A ideia é que este
arquivo seja **agnóstico de projeto**: pode ser copiado como ponto de partida para outro
repositório sem precisar reexplicar o processo do zero. Este próprio repositório
(`deployo-template-java`) existe para isso — veja o `README.md` para o passo a passo de como
usá-lo como ponto de partida.

No vocabulário do Spec-Driven Development (SDD), este arquivo é a *constitution* do
projeto: o conjunto de princípios que toda `spec.md`/`plan.md` deve respeitar.

> Convenções consolidadas a partir de dois projetos anteriores (`jogo-acoes`,
> `deployo-api-key`), que já usavam uma filosofia semelhante (specs escritas antes da
> implementação, decisões registradas conforme tomadas) antes de virar este template.

## Idioma

| O quê | Idioma |
|---|---|
| Código: identificadores, comentários, nomes de arquivo de código | Inglês |
| Mensagens de commit | Inglês |
| Issues e Pull Requests (título e descrição) | Inglês |
| Specs de comportamento (Gherkin, quando usado dentro de `spec.md`) | Inglês |
| Documentação de projeto (`README.md`, `docs/*.md`, `specs/**/*.md`) | Português |

Código em inglês porque é o padrão do ecossistema (bibliotecas, mensagens de erro,
convenções da linguagem). Documentação em português porque é o idioma da equipe — não faz
sentido traduzir decisões e raciocínio para um idioma que não é o nativo de quem escreve e
lê. Ajuste esta tabela se o seu projeto tiver uma equipe com outro idioma nativo — o que
importa é a consistência declarada, não os idiomas específicos escolhidos aqui.

## Status do sistema: pré-produção

Um projeto criado a partir deste template começa sem usuários reais nem dado em produção, e
continua assim até este status ser explicitamente revisado neste documento. Isso tem
consequências diretas em como `spec.md`/`plan.md` são escritas e implementadas:

- **Nenhuma migração de dado existente é necessária.** Uma coluna nova pode ser `NOT NULL`
  direto, sem `default`/sem virar `nullable` "para registros antigos" — não existe registro
  antigo real que dependa disso. Uma tabela pode ser recriada, uma coluna removida, um enum
  perder valores, sem plano de migração dos dados já lá.
- **Nenhuma preocupação de compatibilidade retroativa de contrato** (versionamento de API,
  período de depreciação, cliente antigo continuando a funcionar) — o contrato pode mudar de
  forma incompatível entre specs, sem manter a forma anterior funcionando em paralelo.
- **Nenhum plano de rollback que preserve dado real** — uma migration nova pode fazer
  `DROP COLUMN`/`DROP TABLE` sem se preocupar com "e se alguém já tiver algo lá".
- Isso não dispensa manter a suíte de testes verde nem escrever spec/contrato antes do código —
  só remove a categoria de risco "o que fazer com dado/cliente já existente", que normalmente
  motivaria `nullable`s defensivos, migrações em duas fases, ou versionamento de contrato.

**Quando isso muda**: no primeiro deploy com usuário real em produção, esta seção precisa ser
atualizada (ou removida) — a partir daí, specs que tocam schema/contrato voltam a precisar
considerar migração de dado existente e compatibilidade retroativa.

## Nunca começar a implementar sem pedido explícito

Discutir como resolver uma Issue/problema — mesmo que a sessão tenha entendido errado o pedido
inicial e sido corrigida no meio da conversa, e mesmo que, depois da correção, julgue a solução
óbvia ou pequena — não é sinal verde pra escrever código. Só começa a implementação quando a
pessoa pedir isso explicitamente (ex.: "pode implementar", "começa a implementação", "pode
codar"). Vale mesmo com um desenho técnico já claro na conversa, e não importa quão simples a
mudança pareça — "simples" é julgamento da sessão, não permissão de quem pediu.

**Por quê**: discussão/decisão técnica (o que `spec.md`/`plan.md` existem pra registrar) e
implementação são passos distintos de propósito — pular direto pro código sem confirmação tira
da pessoa a chance de corrigir o rumo antes que vire trabalho a desfazer. Aconteceu na prática
(`jogo-acoes`): uma sessão discutindo o desenho de uma correção, corrigida no meio da conversa
sobre o que realmente precisava mudar, assumiu que agora tinha entendido tudo, julgou a solução
simples e começou a implementar sozinha — sem que ninguém tivesse pedido.

## Nunca abrir PR nem Issue sem pedido explícito

O fluxo padrão é **commit e push no branch**. Abrir PR só quando pedirem explicitamente ("abre a
PR", "pode abrir PR"); o mesmo vale para editar, mesclar ou fechar PR e para criar/editar Issue.
Recuperar algo que ficou de fora de um merge, ou continuar o trabalho depois de uma PR mesclada,
**não** é pedido de PR nova.

**Por quê**: aconteceu na prática (`jogo-acoes`): depois de uma PR mesclada sem um commit que
tinha ficado para trás, a sessão recriou o branch e abriu sozinha uma PR nova — prematura, com
specs que ainda tinham decisões em aberto, e sem ninguém ter pedido. PR e Issue são visíveis
para outras pessoas; quem decide quando algo está pronto para revisão é quem pediu o trabalho.

**Como é reforçado**: `.claude/settings.json` coloca as ferramentas de PR/Issue do GitHub em
`permissions.ask`, então qualquer tentativa pede confirmação antes de executar. Não remover essas
regras para "destravar" uma sessão.

## Baseline Java e upgrades de LTS

Java/JDK 21 é a baseline suportada atualmente por este projeto (ajuste a versão ao criar um
projeto novo). Essa baseline é refletida no `pom.xml`, na CI e nas imagens Docker.

Uma tarefa de documentação, teste, correção ou funcionalidade que não peça mudança de Java não
deve sugerir nem iniciar uma atualização para outro LTS. Um upgrade de Java/JDK só pode ser
discutido ou executado após um pedido explícito.

Se uma dependência, framework, ferramenta ou ambiente exigir uma versão diferente, a
incompatibilidade deve ser reportada com sua causa, componente afetado e versão exigida. Isso
não autoriza uma atualização automática: a mudança da baseline continua sendo uma decisão
explícita, que deve avaliar build, testes, CI, imagens Docker e compatibilidade das
dependências.

## Commits semânticos

Formato da primeira linha:

```
<tipo>: <resumo curto, no imperativo>
```

Tipos usados neste projeto (convenção Conventional Commits, mais um tipo próprio):

| Tipo | Quando usar |
|---|---|
| `feat` | Nova funcionalidade ou comportamento observável |
| `fix` | Correção de bug |
| `refactor` | Mudança estrutural que não altera comportamento (renomear, mover, reorganizar) |
| `test` | Adição/alteração de testes ou especificações |
| `docs` | Mudança só de documentação |
| `chore` | Manutenção sem impacto em código de produção (dependências, config de build) |
| `decision` | Registra uma decisão de arquitetura/design tomada, antes ou junto da implementação que ela habilita |

`decision` é a extensão específica deste fluxo: quando uma pergunta de arquitetura em aberto
(documentada previamente como pendente em `plan.md`) é resolvida, isso vira um commit
próprio, separado da implementação — mesmo que a decisão não mude nenhuma linha de código
sozinha. Isso deixa o histórico do git navegável como uma trilha de decisões, não só de
mudanças de código.

Esses mesmos tipos são usados como **labels de Issue** — ver "Rastreamento de trabalho via
Issues" abaixo — para que commit, Issue e PR falem o mesmo vocabulário.

### Corpo da mensagem

Uma mensagem de commit completa, para uma mudança não trivial, normalmente tem:

1. **Título**: `<tipo>: <resumo>`.
2. **Por quê** (parágrafo): o raciocínio/problema que motivou a mudança — não repetir o que
   o diff já mostra, explicar a razão por trás dele.
3. **O quê** (lista com marcadores, opcional): mudanças concretas relevantes, arquivo por
   arquivo ou tema por tema, quando o "por quê" sozinho não é suficiente para orientar quem
   revisa.
4. **Validação** (parágrafo, opcional): o que foi de fato testado/rodado nesta sessão de
   trabalho para confirmar que a mudança funciona.
5. **Referência cruzada** (linha final, opcional): se a mudança resolve uma decisão em
   aberto registrada em `plan.md`, ou fecha uma Issue, apontar para ela (ex.: "Resolves the
   retry-strategy open decision in specs/002-process-payment/plan.md", ou "Closes #12").

Exemplo:

```
feat: retry failed payment webhooks with exponential backoff

Webhook delivery failures were being dropped silently, so a transient
network blip on the receiving end meant the event was gone for good.

WebhookRetryTest proves a failed delivery is retried up to the configured
limit with increasing delay, and gives up (moving to the dead-letter
table) after the last attempt (4/4 passing).

Resolves the retry-strategy open decision in specs/002-process-payment/plan.md.
Closes #12.
```

Commits pequenos e focados em uma mudança revisável de cada vez — evitar juntar mudanças sem
relação numa mesma mensagem.

### Enforcement

Um hook `commit-msg` versionado em `.githooks/commit-msg` valida o formato da primeira linha
automaticamente (o `<tipo>: ` do título — idioma não é validado por hook). Mensagens geradas
pelo próprio git (`Merge ...`, de `git merge`, que também chama o hook, e `Revert ...`) são
aceitas. Ativar uma vez por clone/sessão:

```
git config core.hooksPath .githooks
```

## Branches e Pull Requests

- Um branch por linha de trabalho revisável — nome descritivo do que está sendo feito, não
  um identificador genérico.
- Nunca commitar direto no branch principal (`main`); toda mudança entra por PR. A única
  exceção é o commit inicial que cria o repositório a partir deste template, antes de existir
  qualquer histórico para proteger.
- Uma PR corresponde a um branch — não empilhar trabalhos sem relação na mesma PR só porque
  foram feitos na mesma sessão.
- Título e descrição de PR seguem a mesma convenção de idioma das mensagens de commit —
  inglês — porque o GitHub usa o título da PR como corpo do merge commit em `main` quando a
  PR é mesclada; uma PR escrita em português vaza pro histórico de commits nesse ponto
  exatamente como um `git commit -m` em português vazaria.
- Quando o trabalho de uma PR já mesclada precisa continuar, reaproveitar o mesmo branch
  (recriado a partir do estado atual do branch principal) em vez de acumular branches novos a
  cada retomada — mantém o histórico de PRs correspondendo 1:1 a unidades de trabalho reais,
  não a sessões de chat.
- Essa regra não tem exceção por tipo de mudança nem por onde a sessão está rodando: uma
  continuação pequena, só de documentação, ou uma correção de CI ainda pertence à mesma linha
  de trabalho — não é motivo pra abrir um branch novo. O mesmo vale trocando de ferramenta
  (ex.: Claude Code no navegador numa sessão, local na outra): a continuidade é da linha de
  trabalho, não da sessão nem da ferramenta que a executa.
- Antes de criar um branch, checar se já existe um branch/PR abordando a mesma feature
  (`specs/NNN-*`) e reaproveitar esse em vez de abrir outro.

## Rastreamento de trabalho via Issues

O board de Issues do GitHub é o lugar para visualizar o andamento do projeto — ele espelha
`specs/*/tasks.md`, não duplica o controle manualmente:

| Nível SDD | GitHub |
|---|---|
| Uma feature (`specs/NNN-nome-da-feature/`) | 1 Issue "guarda-chuva" (épico), corpo linkando `spec.md` e `plan.md` |
| Cada tarefa de `tasks.md` (T001, T002...) | Item da checklist da Issue-épico, ou Issue própria quando a tarefa for grande o suficiente para ter discussão/PR isolada |
| Tipo do commit (`feat`/`fix`/`refactor`/...) | Label da Issue — mesma taxonomia da tabela de commits acima |
| Fase/marco do roadmap, quando existir | Milestone |
| Iteração, quando o projeto trabalha em iterações (opcional) | Label `iteration-N` — mesmo número do prefixo `NN-` de `specs/NN-NNN-slug/` (ver `specs/README.md`) |

Commits e PRs fecham a Issue correspondente com `Closes #N` na mensagem — mesma convenção
usada para referenciar uma decisão resolvida em `plan.md`, só que apontando para a Issue.

**Marcar a task como feita na mesma PR que a implementa.** A PR que resolve uma task de
`tasks.md` já traz a linha riscada (`~~T001~~`) e o registro da verificação dela. O merge torna
as duas coisas verdadeiras ao mesmo tempo; se a PR não for mesclada, a marcação também não
entra. Nunca deixar para "marcar depois do merge": isso exige uma segunda PR só para riscar a
linha, que na prática não acontece, e a tabela fica dessincronizada do código.

Título e corpo da Issue em inglês (ver tabela "Idioma" acima) — mesmo raciocínio das
mensagens de commit e de PR: Issue, commit e PR compõem a mesma trilha rastreável, e misturar
idioma entre eles quebra essa continuidade.

## Documentação viva por feature

- Antes de implementar uma feature não trivial, escrever `specs/NNN-nome-da-feature/spec.md`
  (requisitos, critérios de aceite — o QUÊ e POR QUÊ, sem detalhes de implementação) e
  `plan.md` (decisões técnicas, arquitetura — o COMO). Funciona como uma ata que sobrevive a
  troca de contexto (nova sessão, outra pessoa assumindo o trabalho).
- Decisões em `plan.md` são marcadas como resolvidas no próprio texto conforme são tomadas,
  preservando o raciocínio e as alternativas consideradas — não só a conclusão final.
- Contratos de interface são escritos **antes** da implementação — a implementação segue o
  contrato:
  - **API REST** (o caso comum, já que o template traz `spring-boot-starter-web`): contrato
    único e cumulativo em `docs/openapi.yaml`, escrito antes do controller. Quando possível, o
    build gera interfaces/DTOs a partir dele (ex.: `openapi-generator-maven-plugin`), de modo que
    implementação e contrato não saiam de sincronia sem quebrar a compilação — e o código gerado
    fica fora da cobertura (ver "CI e cobertura de testes").
  - **O que o OpenAPI não cobre** (CLI, mensagem de fila, consumo de um serviço externo):
    `specs/NNN-*/contracts/`, a partir de `templates/contracts-template.md` — não reproduzir ali
    o que já está no OpenAPI.
- Modelo de dados (`data-model.md`), quando a feature envolver dados persistentes, é
  desenhado antes das entidades de código.
- Cenários de comportamento (Gherkin, quando usado) são escritos **antes** do código de
  implementação, dentro de `spec.md` ou como arquivos `.feature` referenciados por ela —
  funcionam como contrato de aceite, não como documentação a posteriori do que já foi
  construído.
- Cada feature nasce a partir de `templates/` (`spec-template.md`, `plan-template.md`,
  `tasks-template.md`, `data-model-template.md`, `contracts-template.md`) — copiar o
  template, não escrever do zero.

## Onde a documentação mora

Documentação de projeto tem plateias diferentes, e cada uma mora num lugar diferente:

- **`memory/constitution.md`** (este arquivo) — princípios e convenções, agnósticos de
  feature específica.
- **`specs/NNN-nome-da-feature/`** — a especificação viva de cada feature (`spec.md`,
  `plan.md`, `data-model.md`, `contracts/`, `tasks.md`). Serve quem está desenvolvendo essa
  feature.
- **`docs/`** — documentação de produto/arquitetura, destinada a quem avalia ou usa o
  projeto de fora (README, roadmap, diagramas, `docs/openapi.yaml`). Pronta para ser publicada
  como está.
- **`docs/context/diario.md`** — diário de desenvolvimento: resumo do que foi feito a cada
  dia de trabalho, com os commits e Issues correspondentes (ver seção abaixo). Não é
  documentação de produto — não deve ser publicada junto com `docs/*.md`.

## Diário de desenvolvimento

`docs/context/diario.md` registra, por dia de trabalho, um resumo curto do que foi feito e a
lista de commits (hash curto + tipo + resumo) e Issues envolvidas naquele dia. Serve para
reconstruir "o que aconteceu e por quê" sem precisar reler o `git log` inteiro, e para dar
contexto a quem retoma o trabalho numa sessão nova.

Formato de cada entrada:

```markdown
## AAAA-MM-DD

**Resumo:** o que foi feito/decidido nesse dia, em 1-3 frases.

**Commits:**
- `<hash curto>` <tipo>: <resumo> (#<issue, se houver>)

**Issues:** #N aberta/fechada/em andamento
```

Uma entrada nova é adicionada ao final do arquivo a cada dia com commits relevantes — não
precisa ser todo dia se não houve trabalho.

## Diagramas Mermaid: validar a renderização de verdade

Um diagrama Mermaid com sintaxe que "parece certa" pode ainda assim falhar ao renderizar — a
gramática tem armadilhas que só aparecem no parser de verdade. Antes de considerar um
diagrama pronto, renderizar de verdade contra um motor Mermaid real (ex.:
`npx @mermaid-js/mermaid-cli`, mesmo motor que o GitHub usa para blocos ```` ```mermaid ````),
não só validar visualmente/mentalmente a sintaxe.

Exemplo de armadilha real (`jogo-acoes`): um `;` dentro do texto de uma `Note` termina a
instrução ali, no meio da frase, e quebra o resto da linha — o motor real reproduziu o erro
visto no GitHub com a mesma mensagem, e foi bisseccionando contra ele, não adivinhando pela
mensagem, que a causa foi isolada (a seta `->`, que parecia mais óbvia, não era o problema).

Em ambiente rodando como root (ex.: container de um agente), o Chromium headless do Puppeteer
só sobe com o sandbox desabilitado: `-p puppeteer.json` com
`{"args": ["--no-sandbox", "--disable-setuid-sandbox"]}`.

## Nomenclatura de ambientes

Nomear ambientes pela **característica real da infraestrutura**, não por um rótulo genérico
tipo "dev"/"qa"/"test" — esses termos são ambíguos e mudam de significado de projeto pra
projeto, obrigando a reexplicar o que cada um significa aqui. Preferir um nome que já
descreve a própria restrição ou característica do ambiente.

Exemplo usado neste template (`application-docker.yml`):

| Nome | O que descreve |
|---|---|
| `docker` | Infraestrutura real via containers, local (`docker-compose`) ou CI — descartável. Perfil padrão |
| `staging` | Pré-produção: infraestrutura e dados reais, mas isolados de produção |
| `production` | Produção |

Cada ambiente tem seu próprio arquivo de configuração autodescritivo — um comentário no topo
explicando por que aquele ambiente existe, quem o opera e quais restrições ele impõe.

O perfil padrão (quando nenhum é escolhido explicitamente) é o mesmo em todos os serviços e
vale também para os testes rodados à mão: o ambiente com a infraestrutura completa (`docker`).
Rodar os testes na mão deve exigir a linha de comando mais simples possível, e testar o mesmo
estado que a CI testa. Por isso o template não tem `src/test/resources/application.yml`: um
arquivo com esse nome substitui (não complementa) o `application.yml` principal nos testes, e
cada chave esquecida nele vira uma diferença entre teste e aplicação.

Não criar um ambiente "sem infraestrutura" (sem Docker, com substitutos do banco ou da fila,
ex.: H2) enquanto nenhum lugar onde o projeto roda precisar dele. O `jogo-acoes` teve um perfil
`sandbox` assim, para um ambiente de agente sem Docker; ele fazia `mvn verify` local testar
contra H2, um estado que a CI nunca testava, e foi removido quando esse ambiente passou a ter
Docker. Se um ambiente assim for realmente necessário, ele ganha nome próprio, arquivo próprio
e nunca é o padrão.

## Banco de dados: um schema por serviço

Todo serviço ou biblioteca que é dono de tabelas usa nomes que dizem de quem elas são, nunca os
padrões do banco ou da ferramenta de migração:

- **Schema próprio**, com o nome do serviço, e **nunca o `public`** — nenhuma tabela, sequência
  ou histórico de migração fica lá.
- **Pasta de migrations própria**, `db/migration-<serviço>`, fora da pasta padrão
  (`db/migration`). A pasta padrão não serve nem como pasta-mãe: a varredura do Flyway é
  recursiva, então um subdiretório dela seria encontrado por quem ainda usa o padrão.
- **Tabela de histórico própria**, `<schema>_schema_history`, dentro do próprio schema.
- **O schema é definido pela aplicação**, não pela URL de conexão: a URL muda por ambiente e,
  fora do `docker`, costuma vir de fora (de quem opera o banco).

**Por quê**: poder colocar vários serviços numa única instância do banco, e até num único banco,
quando for conveniente — sem que nenhum nome colida e sem que um serviço dependa de ter um banco
só para si. Instâncias separadas continuam possíveis: juntar ou separar vira só uma troca de URL.
Os padrões colidem assim que dois donos de tabelas se encontram: aconteceu de verdade
(`jogo-acoes`) com uma biblioteca que trazia a própria `db/migration/V1__...sql` e quebrou o
Flyway do serviço que a usava (duas migrations `V1`). Num projeto novo isso custa algumas linhas
de configuração; migrar depois custou uma spec inteira.

**Como está no template**: `spring.datasource.hikari.schema` (vira o `search_path` de toda
conexão do pool, inclusive as do Flyway), `spring.flyway.schemas`/`table`/`locations` e
`spring.jpa.properties.hibernate.default_schema`, todos no `application.yml` comum. O
`SchemaLayoutTest` confere no banco real, depois do Flyway, que nada ficou no `public`.

## Fronteira entre módulos e serviços

Há duas fronteiras diferentes, e cada uma tem a sua regra.

**Módulo** (pacote de primeiro nível dentro de um mesmo serviço, organizado por
domínio/funcionalidade — ex. `user`, `billing`): roda no mesmo processo e fala com outro módulo
**só por serviço Java**. Repositório, entidade interna e detalhe de persistência ficam dentro do
módulo dono — outro módulo não injeta o repositório alheio. Vale desde o primeiro módulo, mesmo
num projeto com um serviço só.

**Serviço** (artefato com deploy próprio) — **quando o projeto tiver mais de um**: fala com
outro serviço **só por contrato** — HTTP documentado em OpenAPI, ou mensagem com contrato
próprio em `specs/NNN-*/contracts/`. Mesmo morando no mesmo repositório e no mesmo reator Maven:

- nenhuma dependência Java entre eles, nem transitiva — nem para "só reaproveitar um DTO";
- nada de persistência compartilhada: entidades, repositórios, migrations, schemas (ver "Banco
  de dados: um schema por serviço" acima). Um serviço nunca lê nem escreve o banco do outro;
- exceção interna não atravessa a fronteira: o cliente vê status HTTP e corpo de erro do
  contrato, e traduz isso para as próprias exceções num único ponto (o *gateway* do cliente);
- os DTOs do cliente são do cliente — gerados a partir do contrato, não importados do servidor;
- cada lado documenta, na spec que cria a integração, quais dados e quais responsabilidades são
  dele.

**Repetição de chamadas síncronas**: o cliente só repete automaticamente (retry) uma operação que
é idempotente pelo contrato — repetir deixa o servidor no mesmo estado. Uma operação que não é
idempotente não é repetida automaticamente até ganhar idempotência própria (ex.: chave de
idempotência), decidida na spec dela.

**Por quê**: o objetivo de separar um serviço é poder implantar, escalar e trocar cada lado sem
mexer no outro. Uma dependência Java "pequena" ou uma tabela lida "só para consulta" amarra os
dois de novo, sem que o build ou o contrato mostrem isso. Retry de operação não idempotente
transforma uma falha de rede num efeito em dobro (ex.: o mesmo e-mail enviado duas vezes).

**Como verificar**: regras ArchUnit no `ArchitectureTest` (ex.: nenhuma classe de um módulo usa
o repositório de outro; nenhuma classe de outro serviço no classpath; o cliente HTTP gerado só
usado pelo *gateway*) e, entre serviços, o `maven-enforcer-plugin` com `bannedDependencies` no
`pom.xml` de cada um.

## Testes: preferir real a fake sempre que der

Sempre que uma dependência externa tiver como rodar localmente/de verdade em teste
automatizado, preferir isso a um mock/stub. Um teste que passa contra uma simulação que não
bate com o comportamento real do sistema dá falsa confiança — "passou no teste, quebrou em
produção". Usar stub/fake só quando a alternativa real não existe ou não é viável no ambiente
de teste; mesmo nesses casos, o stub registra o que faria de verdade, para que o teste possa
checar por asserção em vez de só confiar que o método foi chamado.

Testes que geram cobrança por uso de API externa paga (ex.: um modelo de IA) não rodam na CI
nem no merge — ficam fora da suíte padrão, rodados à mão quando necessário, e isso é
documentado junto do comando de teste.

## Testes exigem a infraestrutura de pé

Os testes rodam contra a infraestrutura real do ambiente com containers (`docker`), que precisa
estar de pé antes da execução (`docker compose up -d --wait`). Nada é pulado por falta dela:

- **Infraestrutura fora do ar é erro.** O teste falha com o erro de conexão, não aparece como
  pulado.
- **Nenhum teste decide em tempo de execução se roda** (ex.: `assumeTrue(reachable(...))`,
  pular quando um container não sobe). O `ArchitectureTest` impede esse padrão (proíbe
  `org.junit.jupiter.api.Assumptions` nos testes).
- **Como subir a infraestrutura** fica documentado no `README.md`, junto do comando de teste.

**Por quê**: um teste que se pula sozinho quando a infraestrutura falta esconde exatamente o
problema que deveria acusar — no `jogo-acoes`, testes apareciam como pulados até na CI, e cada
módulo acabava inventando um jeito diferente de lidar com isso.

## Critério de aceite: exceções e caminhos de erro exercitados por teste

Nenhuma task que cria ou altera código é aceita sem:

- **Toda classe de exceção criada tem pelo menos um teste que a faz ser lançada** pelo caminho
  real do código (a chamada que falha), não um `new XException()` solto no teste só para
  instanciá-la.
- **Todo branch de exceção é exercitado ao menos uma vez**: `catch`, `@ExceptionHandler`,
  tradução de erro de um cliente (ex.: status HTTP → exceção da aplicação), retry, fallback. Um
  branch que nenhum teste percorre é comportamento de falha que ninguém viu funcionar.

**Quando for muito difícil exercitar um caso** (ex.: uma falha que só a infraestrutura real
produz e que não dá para provocar no ambiente de teste), a exceção à regra é explícita, nunca
silenciosa:

1. Justificar por escrito na documentação da feature (`plan.md` da spec, ou `tasks.md` no
   registro da verificação): qual exceção/branch ficou sem teste e por quê.
2. Avisar no chat, ao abrir a PR, listando esses casos — para que a decisão de aceitar seja de
   quem revisa, não de quem implementou.

**Por quê**: os caminhos de erro são os que menos aparecem no uso normal e os que mais custam
quando falham em produção (ex.: um serviço remoto fora do ar devolvendo `500` em vez de `503`,
ou uma transação que deveria ser desfeita e não é). Cobertura de linha não garante isso — uma
classe de exceção pode estar coberta só pelo construtor. O template de `tasks.md`
(`templates/tasks-template.md`) traz esta verificação como task própria.

## Código de teste/dev nunca dentro da aplicação

Nenhum código que existe só para testar ou simular (endpoint de teste, seed de dados, poller de
desenvolvimento, verificador que sempre aceita, etc.) entra nos módulos de produção — mesmo
atrás de profile/flag condicional, não importa se funciona, não importa se a alternativa
(módulo/aplicação separada) exigir mais código. Sempre um módulo/aplicação à parte, nunca
misturado ao artefato de produção.

**Por quê**: misturar as duas coisas parece inofensivo no começo (mais rápido de escrever, tudo
num lugar só), mas com o tempo fica cada vez mais confuso separar o que é produto do que é
andaime de teste — e aumenta o risco real de um `@ConditionalOnProperty` mal configurado, uma
variável de ambiente esquecida, ou uma migration futura deixarem código/rota de teste ativos em
produção. Vale mesmo que a solução com módulo separado precise duplicar código ou expor uma API
só pra receber configuração — o isolamento físico (artefato de deploy diferente) é o que garante
que não vaza, não só a intenção de mantê-lo desligado.

## Dados de teste: Object Mother + Test Data Builder

Fábricas de dados de teste ("Mother") retornam um objeto/builder já pré-preenchido com dados
**válidos** por padrão — o ponto de partida de qualquer cenário. Cenários que testam uma
variação **inválida** de um campo específico partem desse builder válido e sobrescrevem só o
campo sob teste, mantendo os demais válidos.

## CI e cobertura de testes

- A suíte de testes roda em CI contra infraestrutura o mais real possível, não contra
  atalhos usados só no dia a dia local.
- Cobertura de linha tem um piso obrigatório, quando a stack tiver ferramenta de cobertura —
  não é só um número informativo, é uma condição de build passar. Código gerado fica de fora
  da contagem (ex.: interfaces/DTOs gerados a partir do OpenAPI, via `<excludes>` do
  `jacoco-maven-plugin`) — mas cuidado para não excluir código real só porque é difícil de cobrir
  (ver nota abaixo sobre a classe de bootstrap do Spring Boot).
- O check de CI é obrigatório antes de mesclar (branch protection do GitHub no branch
  principal) — quebrar a suíte ou cair abaixo do piso de cobertura bloqueia o merge, não é um
  aviso.
- A cobertura aparece como comentário na própria PR, quando a ferramenta suportar, atualizado
  a cada push.

**Java** (stack deste template):

- Piso de cobertura via JaCoCo (`mvn verify`), 80% de linha.
- CI (`.github/workflows/ci.yml`) sobe PostgreSQL real via `docker-compose.yml` e roda a
  suíte no perfil `docker` — o mesmo perfil padrão de uma execução local (ver "Nomenclatura de
  ambientes" acima).
- Cobertura comentada na PR a cada push via `madrapps/jacoco-report`, e o relatório HTML do
  JaCoCo publicado como artefato do workflow (mostra linha a linha o que não está coberto),
  mesmo quando o piso de cobertura falha o build.
- CI pode ser disparada à mão em qualquer branch (`workflow_dispatch`), mesmo sem PR aberta.
- A classe principal (`@SpringBootApplication`) só tem uma linha de bootstrap sem lógica de
  negócio — em vez de excluí-la da cobertura (o que faria o JaCoCo analisar zero classes num
  projeto novo e passar o gate vazio, escondendo o número real), este template inclui um
  smoke test que chama `main()` diretamente (`TemplateApplicationTests`), cobrindo a linha de verdade.
  Mantenha esse teste ao renomear a classe/pacote.
