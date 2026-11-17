# mapstruct-demo

![Java](https://img.shields.io/badge/java_17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring_boot_3.1-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/maven-%23C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
![MapStruct](https://img.shields.io/badge/mapstruct_1.6-%23E3262A.svg?style=for-the-badge&logoColor=white)
![Lombok](https://img.shields.io/badge/lombok-%23BC4521.svg?style=for-the-badge&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-%234169E1.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![H2](https://img.shields.io/badge/h2-%230B5FA5.svg?style=for-the-badge&logoColor=white)

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
- MapStruct 1.6.3 e Lombok 1.18.42
- H2 e PostgreSQL (drivers incluídos)
- Maven

O estudo é de 2023 e o Spring Boot ficou na versão da época. O MapStruct e o Lombok foram atualizados porque as versões originais não compilam em JDKs recentes; testado no JDK 25.

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

A aplicação sobe na porta padrão do Spring Boot (8080), com o perfil `test`: banco H2 em memória, populado pelo `import.sql`, e console em `/h2-console`.

Outros perfis, escolhidos pela variável `APP_PROFILE`:

- `dev`: PostgreSQL local (`localhost:5432/cobaia`, usuário e senha padrão do instalador). As tabelas precisam existir antes, pois `ddl-auto=none`.
- `prod`: PostgreSQL por variáveis de ambiente (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).

## Endpoints

| Método | Rota | Retorno |
| --- | --- | --- |
| GET | `/people` | Página de `PersonSummarizedDTO` (id, nome, telefone, e-mail) |
| GET | `/people/{id}` | `PersonDTO` completo, com os endereços (404 se não existir) |
| POST | `/people` | Cria a pessoa com os endereços e devolve `PersonDTO` |

Exemplo de corpo para o POST:

```json
{
  "name": "Fulana de Tal",
  "cpf": "11144477735",
  "phone": "34999990000",
  "email": "fulana@email.com",
  "addresses": [
    { "road": "Rua Exemplo", "number": "10", "district": "Centro", "city": "Uberlândia", "zipCode": "38400000" }
  ]
}
```

## Observações

- Lombok e MapStruct juntos exigem a ordem certa dos processadores de anotação no `maven-compiler-plugin` e o `lombok-mapstruct-binding`. Está tudo configurado no `pom.xml`.
- Na ida (`PersonDTO` → `Person`) o MapStruct não sabe ligar cada endereço à pessoa dona dele. O `@AfterMapping` do mapper faz isso; sem ele, os endereços seriam gravados com `person_id` nulo.
- As entidades usam `@Getter`/`@Setter` em vez de `@Data`: com a relação bidirecional, o `toString()` e o `hashCode()` do `@Data` entram em recursão infinita.
- Todos os dados são fictícios.

## Limitações conhecidas

- Sem validação dos dados de entrada (CPF, e-mail, campos obrigatórios).
- Sem testes além do `contextLoads` e sem `PUT`/`DELETE`; o objetivo era só ver o MapStruct funcionando.
- Os arquivos `.properties` estão em ISO-8859-1 (só os comentários têm acento).

## Quando vale usar MapStruct

Compensa em projetos com muitas entidades e DTOs, onde o mapeamento manual vira repetição e fonte de erro. Em projetos pequenos, um construtor ou um método `toDTO()` resolve sem adicionar dependência nem processador de anotações. Este repo existe pra experimentar a ferramenta, não pra dizer que todo projeto precisa dela.

## Status

Projeto de estudo, sem planos de evolução.
