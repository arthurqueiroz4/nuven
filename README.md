# Nuven - Desafio Técnico para Desenvolvedor Java

Este é um projeto CRUD que utiliza Spring Boot e Spring Data JPA para criar, ler, atualizar e deletar registros de um banco de dados. Para a segurança da aplicação, foi utilizado o Spring Security com tokens JWT.

## Tecnologias Utilizadas

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway
- Lombok

## Arquitetura Utilizada

#### Foi utilizada uma arquitetura que aplico atualmente em meus projetos pessoais e profissionais, onde temos as camadas de domínio, aplicação e infraestrutura baseadas na Clean Architecture, escrita por Robert C. Martin.

- **Application**
  - DTO: os objetos de transferência de dados que são usados para transferir dados entre a camada de aplicação e a camada de domínio.
  - Filter: os filtros que são usados para interceptar as requisições e realizar alguma ação antes de chegarem ao controller.
    - O filtro de token JWT é um exemplo disso.
    - O filtro de tratamento de exceções é outro exemplo.
  - Rest: os controllers que são usados para receber as requisições e configurar o envio das respostas.
  - Security: as classes que são usadas para configurar a segurança da aplicação.
    - A classe de configuração do Spring Security.
    - A implementação dos UserDetails e UserDetailsService.
    - Configuração do uso do filtro JWT.
  - Swagger: as classes de configuração do Swagger.

- **Domain**
  - Entity: as entidades do domínio.
  - Repository: as interfaces que são usadas para acessar o banco de dados na camada de domínio.
  - São usadas para realizar a inversão de dependência, evitando uma dependência direta de um componente fora da camada de domínio. Elas serão implementadas na camada de infraestrutura, que será comentada abaixo.
  - Service: as regras de negócio da aplicação.
  - Tem dependência direta das interfaces de repository.
  - Exception: as exceções customizadas da aplicação que serão tratadas na camada de Application.
  - Seeder: classes que são usadas para popular o banco de dados com dados iniciais.

- **Infra**
  - Init: classe de configuração do Spring para gerenciar melhor a criação de Beans.
  - Repository: as implementações das interfaces de repository.
    - As classes com o prefixo "Postgres" são as implementações das interfaces de repository.
    - Essas classes fazem um intermediário entre a camada de domínio e a camada de infraestrutura.
    - Além disso, usam Specification para fazer consultas mais complexas e dinâmicas no banco de dados.
    - As interfaces com o prefixo "Spring" estendem JpaRepository e JpaSpecificationExecutor, que são usados para criar Specification citadas acima.
  - Utils: classes utilitárias que são usadas em toda a aplicação.
    - A classe utilitária de token JWT.

## Pré-Requisitos

- Docker instalado.

## Instalação e Configuração

1. Clone o repositório do GitHub:
    ```
    git clone https://github.com/arthurqueiroz4/nuven.git
    ```
2. Navegue até o diretório do projeto:
    ```
    cd nuven
    ```
3. Execute o projeto:
    ```
    docker compose up --build
    ```
4. O aplicativo estará acessível em [http://localhost:8080/api/todos](http://localhost:8080/api).
5. Para acessar a documentação da API, acesse [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

## Testes

- Para rodar os testes, execute o comando com o container do banco de dados **rodando**:
    ```
    docker compose up -d nuven-db && ./mvnw test
    ```
## Rotas da API

A seguir estão as rotas disponíveis na API:

### 1. Todos

- **Listar Todos**
  - **Método:** GET
  - **Endpoint:** `/api/todos`
  - **Descrição:** Retorna uma lista de todos os itens a serem feitos.

- **Criar Todo**
  - **Método:** POST
  - **Endpoint:** `/api/todos`
  - **Descrição:** Cria um novo item a ser feito.

- **Detalhes do Todo**
  - **Método:** GET
  - **Endpoint:** `/api/todos/{id}`
  - **Descrição:** Retorna os detalhes de um item específico a ser feito com o ID correspondente.

- **Atualizar Todo**
  - **Método:** PUT
  - **Endpoint:** `/api/todos/{id}`
  - **Descrição:** Atualiza as informações de um item específico a ser feito com o ID correspondente.

- **Excluir Todo**
  - **Método:** DELETE
  - **Endpoint:** `/api/todos/{id}`
  - **Descrição:** Exclui um item específico a ser feito com o ID correspondente.

### 2. Autenticação

- **Login**
  - **Método:** POST
  - **Endpoint:** `/api/auth/auth`
  - **Descrição:** Permite que um usuário faça login na aplicação para obter um token JWT válido.


### 3. Documentação da API

- **Swagger UI**
  - **URL:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
  - **Descrição:** Interface interativa para explorar e testar os endpoints da API.

## Contato

Se tiver alguma dúvida ou sugestão, entre em contato através do email: arthur.queiroz08@aluno.ifce.edu.br.
