# deployo-template-java

Template de repositório Java (Spring Boot + Spec-Driven Development) — ponto de partida para
novos projetos, não um produto em si. Nasceu da consolidação do que funcionou em dois
projetos anteriores (`jogo-acoes`, `deployo-api-key`): metodologia de desenvolvimento
(specs escritas antes do código, rastreamento via Issues, diário de decisões) e a mesma base
técnica (Spring Boot, Flyway, perfil `docker` com PostgreSQL real, JaCoCo, CI).

## O que vem pronto

- **Metodologia (SDD)**: [`memory/constitution.md`](memory/constitution.md) (convenções —
  idioma, commits, branches/PR, testes, rastreamento via Issues), [`templates/`](templates/)
  (modelos para começar uma feature nova), [`specs/`](specs/) (onde as specs vivas de cada
  feature moram), [`docs/context/diario.md`](docs/context/diario.md) (diário de
  desenvolvimento), [`docs/openapi.yaml`](docs/openapi.yaml) (contrato da API, escrito antes
  do código).
- **Agentes de código**: [`CLAUDE.md`](CLAUDE.md) (resumo das regras que mais causam erro) e
  `.claude/settings.json` (pede confirmação antes de criar/editar/mesclar PR ou Issue).
- **Projeto Java buildável**: Spring Boot 4.1.0, Java 21, Spring Data JPA, Flyway, perfil
  padrão `docker` (PostgreSQL real, o mesmo em teste local e em CI), schema próprio do serviço
  (nunca o `public`).
- **Qualidade**: piso de cobertura JaCoCo (80% de linha, `mvn verify`), CI
  (`.github/workflows/ci.yml`) rodando a suíte contra PostgreSQL real via
  `docker-compose.yml`, comentando a cobertura na PR e publicando o relatório HTML;
  `SchemaLayoutTest` (nada no schema `public`) e `ArchitectureTest` (ArchUnit — nenhum teste se
  pula sozinho).
- **Convenção de commit**: hook `.githooks/commit-msg` validando `<tipo>: <resumo>`.

## Como usar este template

1. No GitHub, "Use this template" para criar o novo repositório (ou clone este e troque o
   remote).
2. Renomear o projeto:
   - `pom.xml`: `groupId`, `artifactId`, `name`, `description`.
   - Pacote `io.deployo.template` (em `src/main` e `src/test`) para o pacote real — inclui
     renomear `TemplateApplication`/`TemplateApplicationTests` e ajustar `BASE_PACKAGE` em
     `ArchitectureTest`.
   - `application.yml`, `application-docker.yml`, `docker-compose.yml` e `SchemaLayoutTest`:
     substituir `deployo_template_java` (banco, usuário e schema) e `deployo-template-java`
     (`spring.application.name` e pasta de migrations) pelo nome real do serviço; renomear a
     pasta `src/main/resources/db/migration-deployo-template-java` para
     `db/migration-<nome-do-serviço>`.
   - `docs/openapi.yaml` (ou apagá-lo, se o projeto não expuser API REST).
   - `CLAUDE.md` (seção "O projeto") e este `README.md`.
   - `memory/constitution.md`: revisar a versão em "Baseline Java", decidir se as specs usam a
     variante por iteração (`specs/README.md`) e manter ou remover a seção "Fronteira entre
     módulos e serviços" na parte de serviços, conforme o projeto tenha um serviço ou vários.
3. Ativar o hook de commit: `git config core.hooksPath .githooks`.
4. Subir a infraestrutura e rodar a suíte uma vez para confirmar que tudo builda e passa antes
   de começar a mexer:

   ```
   docker compose up -d --wait db
   mvn verify
   ```

   Os testes exigem o PostgreSQL de pé — sem ele falham com erro de conexão, nunca aparecem
   como pulados (ver "Testes exigem a infraestrutura de pé" em `memory/constitution.md`).
5. Escrever a primeira feature: copiar `templates/spec-template.md` (e os demais) para
   `specs/001-nome-da-feature/`, seguindo `memory/constitution.md`.

## Decisões já tomadas (e por quê)

- **Java 21 + Spring Boot 4.1.0**: mesma versão usada em `jogo-acoes`/`deployo-api-key` —
  consistência entre projetos do portfólio.
- **Sem H2, perfil padrão `docker`**: a versão inicial deste template tinha um perfil
  `sandbox` com H2 para ambientes sem Docker. O `jogo-acoes` removeu o mesmo perfil quando o
  ambiente que o justificava passou a ter Docker: com ele como padrão, um `mvn verify` local
  testava contra H2, um estado que a CI nunca testava. Aqui também não há
  `src/test/resources/application.yml` — os testes leem a mesma configuração da aplicação.
  Ver "Nomenclatura de ambientes" em `memory/constitution.md`.
- **Schema próprio desde o primeiro commit**: custa algumas linhas de configuração agora e
  evita a migração que o `jogo-acoes` precisou fazer quando uma biblioteca trouxe a própria
  `db/migration/V1__...sql`. Ver "Banco de dados: um schema por serviço" em
  `memory/constitution.md`.
- **`spring-boot-starter-web` incluído por padrão**: a maioria dos projetos novos vai expor
  alguma API HTTP; remover a dependência (e ajustar `spring.main.web-application-type` para
  `none`) é mais simples do que adicionar depois, para o caso raro de um projeto ser só CLI
  (como foi o caso do `deployo-api-key`).
- **Smoke test chamando `main()` diretamente** (`TemplateApplicationTests`): a classe
  `@SpringBootApplication` só tem uma linha de bootstrap. Excluí-la da cobertura faria o
  JaCoCo analisar zero classes num projeto novo e passar o gate vazio, escondendo o número
  real — o smoke test cobre essa linha de verdade em vez de mascarar a métrica.

## Licença

GPL-3.0 (ver [`LICENSE`](LICENSE)) — ajuste conforme a licença do novo projeto.
