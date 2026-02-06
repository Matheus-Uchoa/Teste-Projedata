# Configuração de Testes - Correções Aplicadas

## Backend (Quarkus)

### Dependência Mockito Adicionada

Foi adicionada a dependência `quarkus-junit5-mockito` no `pom.xml`:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5-mockito</artifactId>
    <scope>test</scope>
</dependency>
```

### Reset de Mocks no @BeforeEach

**IMPORTANTE**: Adicionado `Mockito.reset()` em todos os `@BeforeEach` para limpar estado entre testes:

```java
@BeforeEach
void setUp() {
    Mockito.reset(productRepository);  // Limpa mocks anteriores
    // ... resto do setup
}
```

Isso é necessário porque o Quarkus reutiliza o contexto entre testes e os mocks do `@InjectMock` mantêm estado.

### Métodos de PageResponse Corrigidos

Os testes foram atualizados para usar getters em vez de métodos de record:
- `result.content()` → `result.getContent()`
- `result.pageNumber()` → `result.getPageNumber()`
- `result.pageSize()` → `result.getPageSize()`
- `result.totalElements()` → `result.getTotalElements()`

### Executar Testes Backend

```bash
cd teste-api
./mvnw clean test
```

## Frontend (Vue.js + Vitest)

### Configuração do Vitest Simplificada

O arquivo `vitest.config.ts` foi simplificado para evitar conflitos com plugins:

```typescript
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  test: {
    environment: 'jsdom',
    globals: true
  }
})
```

### Instalar Dependências

Antes de executar os testes, instale as dependências:

```bash
cd teste-app
npm install
```

Isso instalará:
- `vitest@^2.1.8`
- `@vue/test-utils@^2.4.6`
- `@vitest/ui@^2.1.8`
- `jsdom@^26.0.0`

### Executar Testes Frontend

```bash
npm test
```

## Verificação Rápida

### Backend
```bash
cd teste-api && ./mvnw test
```

Deve executar ~20 testes com sucesso:
- ✅ ProductServiceTest (10 testes)
- ✅ RawMaterialServiceTest (8 testes)
- ✅ ProductionSuggestionServiceTest (3 testes)
- ✅ ProductResourceTest (8 testes)
- ✅ ProductionSuggestionResourceTest (2 testes)

### Frontend
```bash
cd teste-app && npm test
```

Deve executar ~29 testes com sucesso:
- ✅ productStore.spec.ts (7 testes)
- ✅ productionSuggestionStore.spec.ts (4 testes)
- ✅ ConfirmDialog.spec.ts (9 testes)
- ✅ Pagination.spec.ts (9 testes)

## Problemas Comuns

### Backend

**Erro: "package org.mockito does not exist"**
- Solução: Execute `./mvnw clean install` para baixar a dependência mockito

**Erro: "cannot find symbol: method content()"**
- Solução: Já corrigido. Os testes agora usam `getContent()` ao invés de `content()`

### Frontend

**Erro: "Cannot convert undefined or null to object" no vite-plugin-inspect**
- Solução: Já corrigido. O vitest.config.ts foi simplificado e não usa mergeConfig

**Erro: "Cannot find module '@/...'"**
- Solução: Execute `npm install` para instalar todas as dependências

**Testes não encontram componentes Vue**
- Solução: Verifique se o plugin `@vitejs/plugin-vue` está instalado e configurado no vitest.config.ts

## Notas Importantes

1. **Backend**: Use `@InjectMock` para mockar dependências injetadas
2. **Frontend**: Use `vi.mock()` para mockar serviços e módulos externos
3. **Records vs Classes**: PageResponse é uma classe, não um record, então use getters
4. **Test Environment**: Frontend usa `jsdom` para simular DOM do browser
