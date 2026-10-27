# mapstruct-demo

Projeto de estudo que usa **MapStruct** para converter entidades JPA em DTOs (e o contrário) em uma API Spring Boot, sem escrever o código de conversão à mão.

## O que demonstra

- Entidades `Person` e `Address`, com relação 1:N.
- Um mapper (`PersonMapper`) declarado só como interface; o MapStruct gera a implementação em tempo de compilação.
- `PersonDTO` (completo, com a lista de `AddressDTO`) e `PersonSummarizedDTO` (só id, nome, telefone e e-mail, sem CPF nem endereços), mostrando dois "formatos" do mesmo objeto.
- Mapeamento aninhado: a lista de endereços é convertida automaticamente junto com a pessoa.
- Integração do MapStruct com Lombok e com o Spring (`componentModel = "spring"`, o mapper vira um bean injetável).

## Stack

- Java 17
- Spring Boot 3.1.1 (Web, Data JPA)
- MapStruct 1.5.0 e Lombok 1.18.22
- H2 e PostgreSQL (drivers incluídos)
- Maven

Versões da época do estudo (2023), sem atualização.

## Estrutura

```
src/main/java/com/santosediego/cobaia/
  controllers/
  services/
  repositories/
  entities/      Person, Address
  dto/           PersonDTO, PersonSummarizedDTO
  mapper/        PersonMapper (MapStruct)
```

## Como rodar

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta padrão do Spring Boot (8080).

## Observações

- Lombok e MapStruct juntos exigem a ordem certa dos processadores de anotação no `maven-compiler-plugin` e o `lombok-mapstruct-binding`. Está tudo configurado no `pom.xml`, e foi a parte mais chata de acertar.
- Todos os dados são fictícios.

## Quando vale usar MapStruct

Compensa em projetos com muitas entidades e DTOs, onde o mapeamento manual vira repetição e fonte de erro. Em projetos pequenos, um construtor ou um método `toDTO()` resolve sem adicionar dependência nem processador de anotações. Este repo existe pra experimentar a ferramenta, não pra dizer que todo projeto precisa dela.

## Status

Projeto de estudo, sem planos de evolução.
