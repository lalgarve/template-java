# deployo-template-java

Template de repositório Java (Spring Boot + Spec-Driven Development) — ponto de partida para
novos projetos, não um produto em si. Nasceu da consolidação do que funcionou em dois
projetos anteriores (`jogo-acoes`, `deployo-api-key`): metodologia de desenvolvimento
(specs escritas antes do código, rastreamento via Issues, diário de decisões) e a mesma base
técnica (Spring Boot, Flyway, perfis `sandbox`/`docker`, JaCoCo, CI).

## O que vem pronto

- **Metodologia (SDD)**: [`memory/constitution.md`](memory/constitution.md) (convenções —
  idioma, commits, branches/PR, testes, rastreamento via Issues), [`templates/`](templates/)
  (modelos para começar uma feature nova), [`specs/`](specs/) (onde as specs vivas de cada
  feature moram), [`docs/context/diario.md`](docs/context/diario.md) (diário de
  desenvolvimento).
- **Projeto Java buildável**: Spring Boot 4.1.0, Java 21, Spring Data JPA, Flyway, perfis
  `sandbox` (H2, sem precisar de Docker) e `docker` (PostgreSQL real, usado também em CI).
- **Qualidade**: piso de cobertura JaCoCo (80% de linha, `mvn verify`), CI
  (`.github/workflows/ci.yml`) rodando a suíte contra PostgreSQL real via
  `docker-compose.yml` e comentando a cobertura na PR.
- **Convenção de commit**: hook `.githooks/commit-msg` validando `<tipo>: <resumo>`.

## Como usar este template

1. No GitHub, "Use this template" para criar o novo repositório (ou clone este e troque o
   remote).
2. Renomear o projeto:
   - `pom.xml`: `groupId`, `artifactId`, `name`, `description`.
   - Pacote `io.deployo.template` (em `src/main` e `src/test`) para o pacote real — inclui
     renomear `TemplateApplication`/`TemplateApplicationTests`.
   - `application.yml`: `spring.application.name`.
   - `application-sandbox.yml`/`application-docker.yml`/`src/test/resources/application.yml`
     e `docker-compose.yml`: substituir `deployo_template_java`
     (nome do banco/usuário) pelo nome real do projeto.
   - Este `README.md`.
3. Ativar o hook de commit: `git config core.hooksPath .githooks`.
4. Rodar `mvn verify` uma vez para confirmar que tudo builda e a suíte passa antes de
   começar a mexer.
5. Escrever a primeira feature: copiar `templates/spec-template.md` (e os demais) para
   `specs/001-nome-da-feature/`, seguindo `memory/constitution.md`.

## Decisões já tomadas (e por quê)

- **Java 21 + Spring Boot 4.1.0**: mesma versão usada em `jogo-acoes`/`deployo-api-key` —
  consistência entre projetos do portfólio.
- **`sandbox` (H2) vs `docker` (PostgreSQL)**: nunca rodar a suíte de CI contra H2 — ele
  existe só para desenvolvimento local sem Docker (ex.: um agente rodando num container sem
  acesso a Docker). CI sempre usa PostgreSQL real (`SPRING_PROFILES_ACTIVE=docker`).
  Ver "Nomenclatura de ambientes" em `memory/constitution.md`.
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
