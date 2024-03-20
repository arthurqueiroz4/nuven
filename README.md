# Nuven - desafio técnico para desenvolvedor Java

Este é um projeto CRUD que utiliza Spring Boot e Spring Data JPA para criar, ler, atualizar e deletar registros de um banco de dados. E para a segurança da aplicação, foi utilizado o Spring Security com usando tokens JWT.

## Tecnologias Utilizadas

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway
- Lombok

## Arquitetura usada

#### Foi usado uma arquitetura que aplico atualmente em meus projetos pessoais e profissionais, onde temos a camada de domínio, aplicação e infraestrutura baseada no Clean Architecture, escrito por Robert C. Martin.
- **Application**
  - DTO: os objetos de transferência de dados que sao usados para transferir dados entre a camada de aplicação e a camada de domínio.
  - Filter: os filtros que sao usados para interceptar as requisições e fazer alguma ação antes de chegar no controller.
    - o filtro de token JWT é um exemplo disso.
    - o filtro de tratamento de exceções é outro exemplo.
  - Rest: os controllers que sao usados para receber as requisições e configurar o envio das respostas.
  - Security: as classes que sao usadas para configurar a segurança da aplicação.
    - a classe de configuração do Spring Security 
    - a implementacao dos UserDetails e UserDetailsService.
    - configuracao do uso do filtro JWT.
  - Swagger: as classes de configuração do Swagger.
- **Domain**
   - Entity: as entidades do domínio.
  - Repository: as interfaces que sao usadas para acessar o banco de dados na camada de Domain.
    - sao usadas para fazer a inversao de dependencia, para nao ter uma dependência direta de um componente fora da camada de Domain. Elas serao implementadas na camada de Infra que será comentada abaixo.
  - Service: as regras de negócio da aplicação.
    - Tem dependencia direta das interfaces de repository. 
  - Exception: as exceções customizadas da aplicação que serao tratadas na camada de Application.
  - Seeder: classes que sao usadas para popular o banco de dados com dados iniciais.
- **Infra**
  - Init: classe de configuracao do Spring para gerenciar melhor a criacao de Beans.
  - Repository: as implementacoes das interfaces de repository.
    - as classes com o prefixo "Postgres" sao as implementacoes das interfaces de repository.
    - essas classes fazem um intermédio entre a camada de Domain e a camada de Infra.
    - além de usar Specification para fazer consultas mais complexas e dinâmicas no banco de dados.
    - as interfaces com o prefixo "Spring" que sao as interfaces que estendem JpaRepository e JpaSpecificationExecutor que é usado para conseguir criar Specification citada acima.
  - Utils: classes utilitárias que sao usadas em toda a aplicação.
    - a classe de utilitária de token JWT.

## Pré-requisitos

- Docker instalado

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
    ./mvnw test
    ```
## Contato
Se tiver alguma dúvida ou sugestão, entre em contato através do email: arthur.queiroz08@aluno.ifce.edu.br
