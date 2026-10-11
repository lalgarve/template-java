# Spec: <nome da feature>

**Status:** rascunho | em revisão | aprovada
**Issue:** #<número da Issue-épico, quando criada>
**Iteração:** <label `iteration-N`, se o projeto usar iterações — senão, remover esta linha>

## Resumo

<1-2 frases: o que esta feature faz, do ponto de vista de quem usa>

## Motivação

<Por que esta feature é necessária — o problema que resolve>

## Cenários (comportamento esperado)

Descrever em Gherkin (Given/When/Then) ou em prosa estruturada — o que importa é que cada
cenário tenha um critério de aceite verificável. Se o projeto tiver Gherkin executável
(`.feature`, ex. com Cucumber), referenciar os arquivos em vez de duplicar os cenários aqui.

```gherkin
Scenario: <nome do cenário>
  Given <estado inicial>
  When <ação>
  Then <resultado esperado>
```

## Requisitos funcionais

- <requisito 1>
- <requisito 2>

## Requisitos não-funcionais

<performance, segurança, limites — quando relevante>

## Fora de escopo

<o que esta feature explicitamente não cobre, para não crescer sem limite>

## Decisões em aberto

<perguntas de requisito ainda não respondidas — decisões técnicas vão em plan.md, não aqui>
