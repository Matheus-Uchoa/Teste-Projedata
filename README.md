# TesteProjedata - Sistema de Gestão de Inventário

Sistema completo para gestão de inventário industrial com controle de produtos, matérias-primas, associações entre produtos e materiais, e sugestões de produção baseadas em disponibilidade de estoque.

## 📁 Estrutura do Projeto

```
TesteProjedata/
├── teste-api/          # Back-end (Quarkus 3.31.2 + PostgreSQL)
│   ├── src/main/java/com/example/
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── entity/           # Entidades JPA
│   │   ├── exception/        # Exceções customizadas
│   │   ├── filter/           # Filtros de paginação e busca
│   │   ├── mapper/           # Mapeadores DTO ↔ Entity
│   │   ├── repository/       # Repositórios Panache
│   │   ├── resource/         # Endpoints REST (JAX-RS)
│   │   └── service/          # Regras de negócio
│   └── src/main/resources/db/migration/  # Flyway migrations
│
└── teste-app/          # Front-end (Vue 3 + TypeScript)
    ├── src/
    │   ├── components/
    │   │   ├── common/               # Componentes reutilizáveis
    │   │   ├── product/              # Componentes de produtos
    │   │   ├── raw-material/         # Componentes de matérias-primas
    │   │   └── product-material/     # Componentes de associações
    │   ├── services/                 # Serviços API (Axios)
    │   ├── stores/                   # Pinia stores
    │   ├── views/                    # Views/Páginas
    │   └── router/                   # Vue Router
    └── package.json
```

## 🔧 Tecnologias

**Backend:** Quarkus 3.31.2, Java 17, PostgreSQL 13, Flyway, Hibernate ORM Panache, JAX-RS

**Frontend:** Vue.js 3, TypeScript, Pinia, Vue Router, Axios, Vite

**Testes:** JUnit 5, Mockito, Vitest

## ✨ Funcionalidades

- ✅ CRUD completo de produtos e matérias-primas
- ✅ Associação entre produtos e materiais com quantidade necessária
- ✅ Sugestões de produção baseadas em estoque disponível
- ✅ Busca, filtros e paginação em todas as entidades
- ✅ Edição inline nas tabelas
- ✅ Validação de integridade referencial
- ✅ Interface responsiva (mobile, tablet, desktop)
- ✅ Wizard de criação de produtos com vinculação de materiais

## 🚀 Como Executar

### 1. Banco de Dados PostgreSQL

```bash
docker run -d --name postgres-inventory \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=projedata \
  -p 5433:5432 \
  postgres:13-alpine
```

### 2. Back-end

```bash
cd teste-api
./mvnw quarkus:dev
```

**API:** `http://localhost:8081`
**Swagger UI:** `http://localhost:8081/q/swagger-ui`

### 3. Front-end

```bash
cd teste-app
npm install
npm run dev
```

**Aplicação:** `http://localhost:5173`

## 🧪 Testes

**Backend:**
```bash
cd teste-api
./mvnw test
```

**Frontend:**
```bash
cd teste-app
npm run test
```

## 📚 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/products` | Lista produtos |
| GET | `/api/raw-materials` | Lista matérias-primas |
| GET | `/api/products/{id}/raw-materials` | Lista materiais do produto |
| GET | `/api/production-suggestions` | Sugestões de produção |

Todos os endpoints suportam paginação (`page`, `size`), busca (`search`) e ordenação (`sortBy`, `sortDirection`).

## 🗄️ Banco de Dados

- **products** - Produtos fabricados
- **raw_materials** - Matérias-primas em estoque
- **product_raw_materials** - Associação N:N entre produtos e materiais
- **production_suggestions** - View materializada com sugestões de produção

As migrations Flyway são executadas automaticamente ao iniciar a aplicação e incluem dados de exemplo (seed data).
