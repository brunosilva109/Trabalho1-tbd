# Catálogo de Livros

Trabalho 1 da disciplina TBD (Tópicos em Banco de Dados) — IFRS.

Sistema de catálogo de livros demonstrando diferentes abordagens de persistência e otimização de consultas.

## Tecnologias

- Java 17
- Spring Boot 3.x
- Maven
- Thymeleaf + Bootstrap 5
- PostgreSQL
- JDBC (puro)
- JPA / Hibernate
- Redis

## Pré-requisitos

- Java 17+
- Maven 3.9+
- Docker (para PostgreSQL e Redis)

## Setup rápido (Docker)

```bash
# Inicia PostgreSQL e Redis em containers
docker compose up -d

# Para parar:
docker compose down
```

## Configuração manual (sem Docker)

Caso prefira rodar PostgreSQL e Redis localmente sem Docker:

1. Crie o banco de dados PostgreSQL:
   ```sql
   CREATE DATABASE tbd_livros;
   ```

2. Configure credenciais em `src/main/resources/application.properties` se necessário.

## Execução

```bash
mvn spring-boot:run
```

A aplicação inicia em `http://localhost:8080`.

> Ao iniciar, o `DataInitializer` popula o banco com **10.000 livros** automaticamente. Este volume foi escolhido para que o teste de desempenho (`/desempenho`) mostre uma diferença real entre a consulta via JDBC (sem cache) e via Redis (com cache). Com poucos registros, a query é tão rápida que o overhead de serialização do Redis mascara o ganho.

## Estrutura do Projeto

```
src/main/java/com/tbd/livros/
├── LivrosApplication.java        # Entry point Spring Boot
├── model/Livro.java              # Entidade JPA
├── jdbc/LivroJDBC.java           # JDBC puro (Parte 1)
├── repository/LivroRepository.java  # Spring Data JPA (Parte 2)
├── cache/LivroCache.java         # Cache Redis (Parte 3)
├── service/LivroService.java     # Orquestração + teste de desempenho
├── controller/LivroController.java  # Spring MVC
├── config/RedisConfig.java       # Configuração Redis
└── dto/RelatorioDesempenhoDTO.java  # DTO de resultados
```

## Rotas

| Rota | Descrição |
|------|-----------|
| `/` | Home |
| `/jdbc/livros` | Listar via JDBC puro |
| `/jpa/livros` | Listar via JPA/Hibernate |
| `/jpa/livros/buscar?autor=` | Buscar livros por autor |
| `/cache/livros` | Listar via Redis cache |
| `/desempenho` | Comparação de desempenho |

## Partes do Trabalho

| Parte | Pontos | Implementação |
|-------|--------|---------------|
| 1 – JDBC | 1,5 | `LivroJDBC.listarLivros()` — JDBC puro |
| 2 – JPA/Hibernate | 1,5 | `LivroRepository` — Spring Data JPA |
| 3 – Cache Redis | 1,0 | `LivroCache.listarLivros()` — Cache-Aside |
| 4 – Teste de desempenho | 1,0 | `LivroService` — console + página web |
