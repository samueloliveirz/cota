# Cota

API REST para criação e acompanhamento de orçamentos comerciais, feita em **Java + Spring Boot**.

O projeto nasceu de uma necessidade real: trabalho como vendedor em uma loja de materiais hidráulicos e fazia os orçamentos numa ferramenta HTML simples, sem nada salvo. O Cota guarda cada orçamento no banco, calcula os valores no back-end e mostra quais orçamentos ainda estão aguardando resposta do cliente.

## Funcionalidades

- Criar, listar, buscar e editar orçamentos
- Itens com cálculo automático de total por item, subtotal e total com frete
- Status do orçamento: `ENVIADO`, `AGUARDANDO`, `FECHADO` e `PERDIDO`
- Remover um item de um orçamento já salvo
- Histórico de orçamentos por CNPJ
- Verificação de cliente recorrente (se o CNPJ já teve algum orçamento fechado)
- Regras de negócio:
  - envio por Sedex exige endereço
  - pagamento faturado exige a quantidade de dias
  - orçamento fechado não pode ser alterado
- Validação dos dados de entrada com mensagens claras
- Tratamento global de erros com respostas padronizadas (400 e 404)

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- PostgreSQL 16
- Docker e Docker Compose
- Lombok
- Maven

## Arquitetura

O projeto segue a separação em camadas:

```
controller  → recebe as requisições HTTP
service     → regras de negócio
repository  → acesso ao banco (Spring Data JPA)
model       → entidades JPA (Orcamento, ItemOrcamento)
dto         → dados de entrada (Request) e saída (Response)
exception   → tratamento global de erros (@RestControllerAdvice)
enums       → TipoRetirada, FormaPagamento, StatusOrcamento
Documentação interativa (Swagger): `http://localhost:8080/swagger-ui.html`

```

Os valores (subtotal e total) não ficam salvos no banco: são calculados a partir dos itens e do frete, assim nunca ficam desatualizados.

## Como rodar

**Pré-requisitos:** Java 21 e Docker.

1. Clone o repositório:

```bash
git clone https://github.com/samueloliveirz/cota.git
cd cota
```

2. Suba o banco de dados e o pgAdmin:

```bash
docker compose up -d
```

3. Rode a aplicação:

```bash
./mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

| Serviço | Endereço |
|---|---|
| API | `http://localhost:8080` |
| PostgreSQL | `localhost:5433` (usuário e senha: `postgres`) |
| pgAdmin | `http://localhost:5051` (`admin@admin.com` / `admin`) |

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/orcamentos` | Cria um orçamento |
| `GET` | `/orcamentos/{id}/pdf` | Gera o PDF do orçamento |
| `GET` | `/orcamentos/{id}` | Busca um orçamento |
| `PUT` | `/orcamentos/{id}` | Edita um orçamento |
| `PATCH` | `/orcamentos/{id}/status?status=FECHADO` | Altera o status |
| `DELETE` | `/orcamentos/{id}/itens/{itemId}` | Remove um item |
| `GET` | `/orcamentos/cnpj/{cnpj}` | Histórico de orçamentos do CNPJ |
| `GET` | `/orcamentos/cnpj/{cnpj}/recorrente` | Informa se o cliente já comprou antes |

### Exemplo: criar orçamento

`POST /orcamentos`

```json
{
  "empresa": "Metalúrgica Teste Ltda",
  "cnpj": "12.345.678/0001-90",
  "tipoRetirada": "SEDEX",
  "enderecoEnvio": "Rua das Flores, 100 - Santo André/SP",
  "formaPagamento": "FATURADO",
  "diasFaturamento": 28,
  "frete": 45.50,
  "itens": [
    { "produto": "Cilindro hidráulico 50mm", "codigo": "CH-050", "quantidade": 2, "precoUnitario": 850.00 },
    { "produto": "Kit de vedação", "codigo": "KV-050", "quantidade": 4, "precoUnitario": 35.90 }
  ]
}
```

Resposta (resumida):

```json
{
  "id": 1,
  "empresa": "Metalúrgica Teste Ltda",
  "status": "ENVIADO",
  "dataValidade": "2026-10-17",
  "itens": [
    { "produto": "Cilindro hidráulico 50mm", "quantidade": 2, "precoUnitario": 850.00, "total": 1700.00 },
    { "produto": "Kit de vedação", "quantidade": 4, "precoUnitario": 35.90, "total": 143.60 }
  ],
  "subtotal": 1843.60,
  "frete": 45.50,
  "total": 1889.10
}
```

### Exemplo: erro de validação

```json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "campos": {
    "empresa": "Informe o nome da empresa"
  },
  "dataHora": "2026-09-29T21:00:00"
}
```

## Próximos passos

- [x] Geração do orçamento em PDF
- [x] Autenticação com Spring Security
- [ ] Testes unitários e de integração
- [ ] Migrations com Flyway
- [ ] Documentação com Swagger / OpenAPI
- [ ] Front-end

## Autor

**Samuel Oliveira**

- GitHub: [@samueloliveirz](https://github.com/samueloliveirz)
- LinkedIn: [samueloliveirz](https://www.linkedin.com/in/samueloliveirz)
