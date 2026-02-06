# Guia de Testes

Este documento descreve como executar os testes unitários do projeto.

## Backend (Quarkus)

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Executar todos os testes
```bash
cd teste-api
./mvnw test
```

### Executar testes de uma classe específica
```bash
./mvnw test -Dtest=ProductServiceTest
```

### Executar com cobertura
```bash
./mvnw test jacoco:report
```

O relatório de cobertura estará disponível em: `target/site/jacoco/index.html`

### Testes Criados

#### Services
- **ProductServiceTest**: Testa CRUD completo, paginação, busca e ordenação
- **RawMaterialServiceTest**: Testa CRUD completo, paginação, busca e ordenação
- **ProductionSuggestionServiceTest**: Testa cálculo de sugestões de produção e totais

#### Resources (Integration Tests)
- **ProductResourceTest**: Testa endpoints REST, validação e tratamento de erros
- **ProductionSuggestionResourceTest**: Testa endpoint de sugestões de produção

## Frontend (Vue.js + Vitest)

### Pré-requisitos
- Node.js 20.19+ ou 22.12+
- npm ou yarn

### Instalar dependências
```bash
cd teste-app
npm install
```

### Executar todos os testes
```bash
npm test
```

### Executar testes em modo watch
```bash
npm test -- --watch
```

### Executar testes com UI interativa
```bash
npm run test:ui
```

### Executar com cobertura
```bash
npm run test:coverage
```

### Testes Criados

#### Stores
- **productStore.spec.ts**: Testa estado inicial, fetch, create, update, delete, search e sort
- **productionSuggestionStore.spec.ts**: Testa fetch de sugestões, tratamento de erros e estados vazios

#### Components
- **ConfirmDialog.spec.ts**: Testa renderização, props, eventos (confirm/cancel), variantes
- **Pagination.spec.ts**: Testa navegação, mudança de tamanho de página, desabilitar botões, cálculos

## Estrutura dos Testes

### Backend
```
teste-api/src/test/java/com/example/
├── service/
│   ├── ProductServiceTest.java
│   ├── RawMaterialServiceTest.java
│   └── ProductionSuggestionServiceTest.java
└── resource/
    ├── ProductResourceTest.java
    └── ProductionSuggestionResourceTest.java
```

### Frontend
```
teste-app/src/
├── stores/__tests__/
│   ├── productStore.spec.ts
│   └── productionSuggestionStore.spec.ts
└── components/__tests__/
    ├── ConfirmDialog.spec.ts
    └── Pagination.spec.ts
```

## Cobertura de Testes

### Backend
- ✅ Services: CRUD completo, paginação, busca, ordenação
- ✅ Resources: Endpoints REST, validação, status codes
- ✅ Production Suggestions: Cálculos e agregações

### Frontend
- ✅ Stores (Pinia): Estado, ações, tratamento de erros
- ✅ Componentes comuns: Interações, props, eventos
- ⚠️ Componentes de formulário: Não cobertos (podem ser adicionados)
- ⚠️ Views: Não cobertos (podem ser adicionados)

## Boas Práticas

### Backend
- Use `@InjectMock` para mockar dependências
- Teste casos de sucesso e erro
- Verifique se métodos foram chamados com `verify()`
- Use `assertThrows` para exceções esperadas

### Frontend
- Use `vi.mock()` para mockar serviços
- Limpe mocks com `vi.clearAllMocks()` no `beforeEach`
- Teste emissão de eventos com `wrapper.emitted()`
- Use `setActivePinia` para testes de stores

## CI/CD

### Backend
```bash
./mvnw clean test
```

### Frontend
```bash
npm ci
npm test
```

## Troubleshooting

### Backend
- **Erro de contexto do Quarkus**: Execute `./mvnw clean` antes dos testes
- **Banco de dados**: Testes usam H2 em memória automaticamente

### Frontend
- **Módulos não encontrados**: Execute `npm install`
- **Erro de jsdom**: Verifique se `jsdom` está instalado
- **Testes lentos**: Use `--run` para executar uma vez sem watch mode
