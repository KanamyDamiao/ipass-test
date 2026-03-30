# ipass-test

Projeto de demonstração utilizando **Java 21**, **Spring Boot**, **PostgreSQL** e **Docker** com foco em uma estrutura
simples que permita evoluir para arquiteturas baseadas em **microsserviços e eventos**.

---

# Tecnologias

* Java 21
* Spring Boot
* Maven
* Docker
* Docker Compose
* PostgreSQL
* Makefile

---

# Portas da aplicação

| Serviço         | Porta padrão | Observação                                                                                                       |
|-----------------|-------------:|------------------------------------------------------------------------------------------------------------------|
| API Spring Boot |       `8080` | Acesso local em `http://localhost:8080`                                                                          |
| PostgreSQL      |       `5433` | Banco usado no ambiente Docker e local (porta 5433 para evitar conflito com um PostgreSQL já rodando na máquina) |

> As portas podem ser alteradas nas configurações do projeto, como `docker-compose.yml` e
`src/main/resources/application.properties`.

---

# Pré-requisitos

Antes de rodar o projeto é necessário possuir instalado:

* Java 21
* Maven
* Docker
* Docker Compose
* make

No Ubuntu / Linux Mint o `make` pode ser instalado com:

```
sudo apt install build-essential
```

---

# Comandos do Makefile

| Comando             | O que faz                                               |
|---------------------|---------------------------------------------------------|
| `make run`          | Inicia a aplicação localmente com `mvn spring-boot:run` |
| `make build`        | Executa build do projeto com `mvn clean package`        |
| `make docker-up`    | Sobe o ambiente completo com Docker Compose             |
| `make docker-db`    | Sobe apenas o serviço de banco (`postgres`)             |
| `make docker-down`  | Para os containers                                      |
| `make docker-reset` | Para containers e remove volumes                        |
| `make rebuild`      | Executa build Maven e sobe novamente com Docker         |
| `make clean`        | Limpa artefatos de build (`mvn clean`)                  |

---

# Fluxos de uso

## Executar localmente (sem Docker)

1. Gerar o build do projeto

```
make build
```

2. Executar a aplicação

```
make run
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

---

## Executar com Docker

Subir todos os serviços da aplicação:

```
make docker-up
```

Esse comando irá:

* buildar a aplicação
* iniciar o PostgreSQL
* iniciar a API Spring Boot

---

## Subir apenas o banco de dados

Caso queira rodar a aplicação localmente mas utilizar o banco do Docker:

```
make docker-db
```

---

## Parar o ambiente Docker

```
make docker-down
```

---

## Resetar ambiente Docker (incluindo volumes)

Remove containers e volumes persistentes do banco:

```
make docker-reset
```

---

# Estrutura do projeto

```
ipass-test
 ├── docker-compose.yml
 ├── Dockerfile
 ├── Makefile
 ├── pom.xml
 └── src
     └── main
         ├── java
         └── resources
             └── application.properties
```

---

# Observações

Este projeto foi criado com o objetivo de servir como base para evoluções futuras, como:

* arquitetura baseada em **microsserviços**
* comunicação assíncrona com **RabbitMQ**
* implementação de **event-driven architecture**
* utilização de padrões como **CQRS**

---
