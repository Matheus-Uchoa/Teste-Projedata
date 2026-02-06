# TesteProjedata - Sistema de Controle de Estoque

Sistema para controle de estoque de produtos e matérias-primas em indústrias.

## Estrutura do Projeto

```
TesteProjedata/
├── teste-api/          # Back-end (API REST com Quarkus)
└── teste-app/          # Front-end (Vue.js 3 + TypeScript)
```

## 🔧 Tecnologias

### Back-end (teste-api)
- **Quarkus** 3.31.2
- **Java** 17
- **PostgreSQL** 13
- **Flyway** (migrations)
- **Hibernate ORM Panache**

### Front-end (teste-app)
- **Vue.js** 3
- **TypeScript**
- **Vue Router**
- **Pinia** (state management)

## 🚀 Como executar

### Back-end (API)

```bash
cd teste-api
./mvnw quarkus:dev
```

A API estará disponível em: `http://localhost:8081`

Swagger UI: `http://localhost:8081/q/swagger-ui`

### Front-end (App)

```bash
cd teste-app
npm run dev
```

A aplicação estará disponível em: `http://localhost:5173`

## 📦 Banco de Dados

O projeto usa PostgreSQL rodando em Docker:

```bash
docker run -d --name postgres-inventory \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=projedata \
  -p 5433:5432 \
  postgres:13-alpine
```

## 📚 Endpoints da API

### Produtos
- `GET /api/products` - Lista todos os produtos
- `GET /api/products/{id}` - Busca produto por ID
- `POST /api/products` - Cria novo produto
- `PUT /api/products/{id}` - Atualiza produto
- `DELETE /api/products/{id}` - Deleta produto

### Matérias-primas
- `GET /api/raw-materials` - Lista todas as matérias-primas
- `GET /api/raw-materials/{id}` - Busca matéria-prima por ID
- `POST /api/raw-materials` - Cria nova matéria-prima
- `PUT /api/raw-materials/{id}` - Atualiza matéria-prima
- `DELETE /api/raw-materials/{id}` - Deleta matéria-prima

### Associações Produto-Matéria Prima
- `GET /api/products/{productId}/raw-materials` - Lista matérias-primas do produto
- `POST /api/products/{productId}/raw-materials` - Adiciona matéria-prima ao produto
- `PUT /api/products/{productId}/raw-materials/{rawMaterialId}` - Atualiza quantidade
- `DELETE /api/products/{productId}/raw-materials/{rawMaterialId}` - Remove associação

## 🎯 Funcionalidades

- ✅ CRUD de Produtos
- ✅ CRUD de Matérias-primas
- ✅ Associação de matérias-primas aos produtos
- 🔄 Consulta de produtos que podem ser produzidos (em desenvolvimento)
- 🔄 Front-end Vue.js (em desenvolvimento)
