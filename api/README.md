# API Raízes do Nordeste

API Rest desenvolvida para gerenciar o cadastramento de usuários e a realização de pedidos para a Rede de Lanchonetes **Raízes do Nordeste** (fictícia). 

## Fluxos Críticos Implementados:
### Cadastro e Autenticação
Foi implementado o cadastramento do usuário com validação do formato das informações e evitando o cadastro de mais de um usuário com o mesmo CPF ou e-mail. Além disso, foi criada uma rota para autenticação e geração de token para acesso às rotas protegidas.
### Realização de Pedido
Foi implementado o fluxo para realização de pedido em unidade, com validação da disponibilidade do produto no cardápio ativo e nos estoques da unidade, calculando descontos de acordo com a regra de fidelidade, validando o pagamento por meio de gateway de pagamento (mock), dando baixa no estoque da unidade e atualizando o status do pedido.
### Pontos por Fidelidade
Foi determinado como regra que o usuário acumula um ponto de fidelidade a cada real gasto em compras. Os pontos de fidelidade podem ser convertidos em descontos nos próximos pedidos, sendo o valor do desconto equivalente a 5% do valor dos pontos acumulados, ou seja, 100 pontos podem gerar R$ 5,00 de desconto. Todavia, o desconto fica limitado a 20% do valor total do pedido.
### Cancelamento do Pedido
Ficou determinado como regra que o usuário poderá cancelar apenas o último pedido realizado no sistema, e apenas se o status do pedido for AGUARDANDO_PAGAMENTO ou PAGAMENTO_CONFIRMADO. Os demais status não permitem cancelamento devido à regra de reposição de estoque, que não poderia ser aplicada após o início do preparo do pedido. Ao cancelar o pedido, os pontos de fidelidade do usuário retornam ao estado anterior ao pedido, os produtos/ingredientes são devolvidos aos estoques da unidade e o status do pagamento é alterado para A_ESTORNAR.


## Principais Tecnologias Utilizadas:
* **Java 25**
* **Spring Boot 4.0.6** (Spring Data JPA, Spring Security)
* **Apache Maven** (gerenciamento de dependências)
* **MySQL / H2** (bando de dados)
* **Flyway** (migrations)
* **Lombok** (aceleração de desenvolvimento)
* **Jakarta Validation**(validação de dados)
* **SpringDoc OpenAPI 3 (Swagger UI)** (documentação interativa)
* **Java JWT** (gerenciamento de tokens)
* **SLF4J** (logs para auditoria)

## Pré-requisitos
Antes de realizar os testes, deve instalar na sua máquina:
* [JDK 25](https://www.oracle.com/br/java/technologies/downloads/#java25)
* [Maven](https://maven.apache.org/download.cgi) (**opcional**, se usar o wrapper `mvnw`)

## Para Executar o Projeto
### 1. Deve clonar o repositório
```bash 
git clone https://github.com/tarcisiordossantos/raizes-do-nordeste
```

### 2. Configuração do banco de dados
Para facilitar os testes, o projeto foi configurado para utilização do banco de dados em memória **H2**. Caso deseje utilizar e tenha o **MySQL** instalado no computador, deverá alterar a linha a seguir arquivo `src\main\resources\application.properties` de:
`spring.profiles.active=h2`
para: 
`spring.profiles.active=mysql`


### 3. Para rodar a aplicação
```bash
./mvnw spring-boot:run
```
## Documentação da API 
Para testar os Endpoints deve acessar via Swagger UI com a aplicação rodando em:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Descrição das Rotas
|Método | Endpoint | Descrição | Acesso |
| --- | --- | --- |--- | 
| POST | /usuarios | Cadastrar novos usuários no sistema | Público
| GET | /unidades/{unidadeId}/cardapio | Lista produtos do cardápio ativo na unidade | Público
| POST | /auth/login | Autentica usuário e gera token | Público
| GET | /usuarios/{id} | Consultar usuário por ID | Usuário Autenticado*
| GET | /usuarios | Listar todos os usuários cadastrados | Perfil GERENTE**
| DELETE | /usuarios/{id} | Deletar usuário por ID | Usuário Autenticado*
| PATCH | /usuarios/{id} | Atualizar cadastro do usuário por ID | Usuário Autenticado*
| POST | /usuarios/{usuarioId}/enderecos | Cadastrar novos endereço para o usuário | Usuário Autenticado*
| GET | /usuarios/{usuarioId}/enderecos | Consultar endereços do usuário | Usuário Autenticado*
| DELETE | /usuarios/{usuarioId}/enderecos/{enderecoId} | Deletar endereço do usuário pelo ID | Usuário Autenticado*
| PUT | /usuarios/{usuarioId}/enderecos/{enderecoId} | Atualizar endereço do usuário pelo ID | Usuário Autenticado*
| POST | /pedidos | Cadastrar novo pedido | Usuário Autenticado*
| GET | /pedidos | Consultar pedidos por unidade, canal ou usuário | Perfil GERENTE**
| GET | /pedidos/meus | Listar pedido do usuário autenticado | Usuário Autenticado
| PATCH | /pedidos/{id} | Atualizar/Cancelar pedido realizado | Usuário Autenticado*
---
\* O usuário com perfil **CLIENTE** não consegue acessar, criar ou alterar recursos usando o ID de outro usuário.
\** O perfil **GERENTE** concede acesso a todas as rotas e possibilita acessar, criar ou alterar recursos para o ID de outros usuários.
***
## Migrations e Seeds
 Para realização de testes no projeto, foi utilizado o **Flyway** para criar a estrutura da tabelas necessárias ao funcionamento do projeto e popular estas com dados iniciais. Foram criadas duas unidades (ID 1 e 2) com seus respectivos estoques e cardápios, que podem ser consultados na aplicação. 
Para mais detalhes sobre as migrations e seeds, acessar os arquivos no diretório `src\main\resources\db\migration`. 
 Pode visualizar o diagrama do banco de dados utilizado no projeto em `docs\diagramas\diagrama_entidade_relacionamento.png`.

## Organização do Projeto

```
com.raizesdonordeste.api
├───api 			# Rotas, contratos de request/response e documentação (Swagger)
│   ├───config		# Configuração para Swagger e para mensagens de exceções personalizadas
│   ├───controller 	# Endpoints 
│   └───dto			# Objetos de transferência de dados
|
├───application 	# Classes de serviço (casos de uso e orquestração)
│   └───exception 	# Exceções personalizadas lançadas nos serviços
|
├───domain 			# Entidades do domínio (Usuario, Pedido, Unidade)
│   └───enuns 		# Enumerações 
|
└───infrastructure	# persistência, integrações e segurança
│   ├───gateway		# integração com gateway de pagamento (mock)
│   ├───repository	# Interfaces de persistência
│   └───security	# Filtros de acesso e configuração de autenticação
```
## Testes no Postman
Caso deseje executar testes utilizando o Postman, deve seguir os seguintes passos:
1. Abra o **[Postman](https://www.postman.com/downloads/)** na sua máquina;
2. Importe os arquivos da coleção e do ambiente localizados no diretório `/testes` do repositório;
3. Ative o ambiente no canto superior direito do Postman;
4. Certifique-se de que a aplicação Spring Boot está em execução;
5. Abra a coleção na barra lateral, selecione os cenários desejados e clique em **Send** para verificar os resultados.

O projeto possui 10 cenários de teste planejados para validar o fluxo crítico da API, conforme o quadro a seguir. Registra-se que os testes de 01 a 09 podem ser realizados com o primeiro usuário cadastrado (ID 1). Todavia, para a realização do teste 10, deverá ser cadastrado um novo usuário e realizada a autenticação com ele, visto que o gateway de pagamento (mock) está programado para retornar false caso o ID do usuário seja um número par, ocasionando uma exceção de falha no pagamento.

| ID   | Cenário                                  | Endpoint                                           | Pré-condição                                      | Entrada                                                | Esperado (status + resposta)                            | Nome na coleção                      |
|------|-------------------------------------------|----------------------------------------------------|---------------------------------------------------|---------------------------------------------------------|----------------------------------------------------------|--------------------------------------|
| T01  | Usuário cadastrado                        | POST /usuarios                                     | —                                                 | Body: dados do usuário                                  | 201 + Body: dados do usuário com ID e perfil             | T01 – Cadastrar usuário              |
| T02  | Login válido                              | POST /auth/login                                   | Usuário cadastrado                                | Body: (cpf, senha)                                      | 200 + accessToken                                        | T02 – Login válido                   |
| T03  | Listar produtos do cardápio da unidade    | GET /unidades/{unidadeId}/cardapio                 | —                                                 | Path: id da unidade                                     | 200 + lista de produtos                                  | T03 – Listar produtos do cardápio    |
| TN04  | Pedido sem token válido                   | POST /pedidos                                      | —                                                 | Body: dados do pedido                                   | 401 + erro padrão                                        | TN04 – Pedido sem token válido        |
| T05  | Pedido realizado                          | POST /pedidos                                      | Usuário autenticado e autorizado com token válido | Body: dados do pedido                                   | 201 + Body: dados do pedido com valor total              | T05 – Cadastrar pedido               |
| TN06 | Produto inexistente                       | POST /pedidos                                      | Usuário autenticado e autorizado com token válido | Body: dados do pedido com id de produto inexistente     | 404 + erro personalizado                                 | TN06 – Produto inexistente           |
| TN07 | Pedido sem informar canal de origem       | POST /pedidos                                      | Usuário autenticado e autorizado com token válido | Body: dados do pedido sem informar canal de origem      | 400 + erro padrão                                        | TN07 – Pedido sem canal de origem    |
| T08  | Cancelar último pedido                    | PATCH /pedidos/{id}                                | Usuário autenticado e autorizado com token válido | Path: id do pedido                                      | 200 + Body: dados do pedido com status cancelado         | T08 – Cancelar pedido                |
| T09  | Excluir endereço de usuário               | DELETE /usuarios/{usuarioId}/enderecos/{enderecoId}| Usuário autenticado e autorizado com token válido | Path: id do usuário e id do endereço                    | 204 + sem conteúdo                                       | T09 – Deletar endereço usuário       |
| TN10 | Falha no pagamento                        | POST /pedidos                                      | Usuário autenticado e autorizado com token válido | Body: dados do pedido (usuarioId inválido)              | 422 + erro personalizado                                 | TN10 – Falha no pagamento            |


