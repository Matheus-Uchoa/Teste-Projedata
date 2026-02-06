<script setup lang="ts">
import { ref, watch } from 'vue'
import { LinkIcon, PencilIcon, TrashIcon, CheckIcon, XMarkIcon, PlusIcon } from '@heroicons/vue/24/outline'
import type { Product } from '../../services/productService'
import Pagination from '../common/Pagination.vue'
import SearchAndSort from '../common/SearchAndSort.vue'

const props = defineProps<{
  products: Product[]
  loading: boolean
  error: string | null
  currentPage: number
  pageSize: number
  totalPages: number
  totalElements: number
  searchTerm: string
  sortBy: string
  sortDirection: string
}>()

const emit = defineEmits<{
  create: []
  update: [id: number, product: Product]
  delete: [id: number]
  manageMaterials: [id: number]
  pageChange: [page: number]
  pageSizeChange: [size: number]
  search: [search: string, sortBy: string, sortDirection: string]
}>()

const sortOptions = [{ value: 'value', label: 'Sort by Value' }]
const localSearch = ref(props.searchTerm)
const localSortBy = ref(props.sortBy)
const localSortDirection = ref(props.sortDirection)

function handleSearch() {
  emit('search', localSearch.value, localSortBy.value, localSortDirection.value)
}

const editingId = ref<number | null>(null)
const editForm = ref<Product>({ name: '', value: 0 })

function startEdit(product: Product) {
  editingId.value = product.id!
  editForm.value = { ...product }
}

function cancelEdit() {
  editingId.value = null
  editForm.value = { name: '', value: 0 }
}

function saveEdit(id: number) {
  emit('update', id, editForm.value)
}

// Fecha o modo de edição quando a atualização é bem-sucedida
watch(() => props.loading, (newLoading, oldLoading) => {
  // Se estava carregando e parou de carregar
  if (oldLoading && !newLoading) {
    // Se não há erro, significa que foi bem-sucedido
    if (!props.error && editingId.value) {
      cancelEdit()
    }
  }
})

function formatCurrency(value: number) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value)
}
</script>

<template>
  <div class="product-list">
    <div class="header">
      <h1>Products</h1>
      <button class="btn btn-primary" @click="$emit('create')">
        <PlusIcon class="icon" /> New Product
      </button>
    </div>

    <SearchAndSort
      v-model:model-search="localSearch"
      v-model:model-sort-by="localSortBy"
      v-model:model-sort-direction="localSortDirection"
      search-placeholder="Search products by name..."
      :sort-options="sortOptions"
      @search="handleSearch"
    />

    <div v-if="loading" class="loading">Loading...</div>

    <div v-else class="content-wrapper">
      <div class="table-container">
        <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Value</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="product in products" :key="product.id">
            <tr>
              <!-- View mode -->
              <template v-if="editingId !== product.id">
                <td data-label="ID">{{ product.id }}</td>
                <td data-label="Name">{{ product.name }}</td>
                <td data-label="Value">{{ formatCurrency(product.value) }}</td>
                <td data-label="Actions">
                  <div class="actions">
                    <button class="btn-icon btn-materials" @click="$emit('manageMaterials', product.id!)" title="Manage Materials">
                      <LinkIcon class="icon-sm" />
                    </button>
                    <button class="btn-icon btn-edit" @click="startEdit(product)" title="Edit">
                      <PencilIcon class="icon-sm" />
                    </button>
                    <button class="btn-icon btn-delete" @click="$emit('delete', product.id!)" title="Delete">
                      <TrashIcon class="icon-sm" />
                    </button>
                  </div>
                </td>
              </template>

              <!-- Edit mode -->
              <template v-else>
                <td>{{ product.id }}</td>
                <td>
                  <input
                    v-model="editForm.name"
                    type="text"
                    class="input-edit"
                    placeholder="Product name"
                    required
                  />
                </td>
                <td>
                  <input
                    v-model.number="editForm.value"
                    type="number"
                    step="0.01"
                    class="input-edit"
                    placeholder="Value"
                    required
                  />
                </td>
                <td>
                  <div class="actions">
                    <button class="btn-icon btn-save" @click="saveEdit(product.id!)" title="Save">
                      <CheckIcon class="icon-sm" />
                    </button>
                    <button class="btn-icon btn-cancel" @click="cancelEdit" title="Cancel">
                      <XMarkIcon class="icon-sm" />
                    </button>
                  </div>
                </td>
              </template>
            </tr>
            <tr v-if="editingId === product.id && error" class="error-row">
              <td colspan="4">
                <div class="inline-error">{{ error }}</div>
              </td>
            </tr>
          </template>
          </tbody>
        </table>

        <div v-if="products.length === 0" class="empty-state">
          <p>No products found. Create your first product!</p>
        </div>
      </div>

      <Pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total-pages="totalPages"
        :total-elements="totalElements"
        @page-change="(page) => $emit('pageChange', page)"
        @page-size-change="(size) => $emit('pageSizeChange', size)"
      />
    </div>
  </div>
</template>

<style scoped>
.product-list {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 2rem;
  gap: 1.5rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-shrink: 0;
}

.header h1 {
  font-size: 1.75rem;
  font-weight: 600;
  color: #0f172a;
  margin: 0 0 0.25rem 0;
  letter-spacing: -0.02em;
}

.btn {
  padding: 0.625rem 1rem;
  border: none;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  font-family: inherit;
}

.btn-primary {
  background: #3b82f6;
  color: white;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.btn-primary:hover {
  background: #2563eb;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
  transform: translateY(-1px);
}

.btn-primary:active {
  transform: translateY(0);
}

.loading {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
  font-size: 0.9375rem;
}

.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 0.75rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.08), 0 1px 2px 0 rgba(0, 0, 0, 0.02);
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

.table-container {
  flex: 1;
  overflow: auto;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table thead {
  background: #f8fafc;
  position: sticky;
  top: 0;
  z-index: 1;
  border-bottom: 1px solid #e2e8f0;
}

.table th {
  padding: 0.875rem 1.5rem;
  font-weight: 600;
  color: #64748b;
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  text-align: left;
}

.table th:last-child {
  text-align: right;
}

.table th:nth-child(3) {
  text-align: right;
}

.table td {
  padding: 1rem 1.5rem;
  color: #0f172a;
  font-size: 0.875rem;
  border-bottom: 1px solid #f1f5f9;
}

.table td:nth-child(3) {
  text-align: right;
  font-weight: 500;
  color: #0f172a;
}

.table td:last-child {
  text-align: right;
}

.table tbody tr {
  transition: background-color 0.15s ease;
}

.table tbody tr:hover {
  background: #fafbfc;
}

.table tbody tr:last-child td {
  border-bottom: none;
}

.input-edit {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border: 1px solid #cbd5e1;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-family: inherit;
  transition: all 0.2s ease;
  background: white;
}

.input-edit:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.actions {
  display: flex;
  gap: 0.25rem;
  justify-content: flex-end;
}

.btn-icon {
  padding: 0.375rem;
  border: none;
  background: transparent;
  cursor: pointer;
  border-radius: 0.375rem;
  transition: all 0.15s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
}

.btn-icon:hover {
  color: #0f172a;
}

.btn-materials {
  color: #10b981;
}

.btn-materials:hover {
  background: #f0fdf4;
  color: #059669;
}

.btn-edit {
  color: #3b82f6;
}

.btn-edit:hover {
  background: #eff6ff;
  color: #2563eb;
}

.btn-delete {
  color: #ef4444;
}

.btn-delete:hover {
  background: #fef2f2;
  color: #dc2626;
}

.btn-save {
  color: #10b981;
}

.btn-save:hover {
  background: #f0fdf4;
  color: #059669;
}

.btn-cancel {
  color: #ef4444;
}

.btn-cancel:hover {
  background: #fef2f2;
  color: #dc2626;
}

.empty-state {
  padding: 4rem 2rem;
  text-align: center;
  color: #94a3b8;
  font-size: 0.9375rem;
}

.icon {
  width: 1.125rem;
  height: 1.125rem;
  flex-shrink: 0;
}

.icon-sm {
  width: 1rem;
  height: 1rem;
  flex-shrink: 0;
}

.error-row {
  background: #fef2f2 !important;
}

.error-row:hover {
  background: #fef2f2 !important;
}

.inline-error {
  color: #ef4444;
  padding: 0.75rem 1.5rem;
  font-size: 0.8125rem;
  font-weight: 500;
}

/* Responsive Design */
@media (max-width: 768px) {
  .product-list {
    padding: 1rem;
  }

  .header {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
  }

  .header h1 {
    font-size: 1.5rem;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }

  .table th,
  .table td {
    padding: 0.75rem 1rem;
    font-size: 0.8125rem;
  }

  .table th {
    font-size: 0.6875rem;
  }

  /* Hide text in action buttons on small screens */
  .action-buttons .btn-icon span {
    display: none;
  }

  .action-buttons {
    gap: 0.25rem;
  }
}

@media (max-width: 640px) {
  .product-list {
    padding: 0.75rem;
  }

  .content-wrapper {
    border-radius: 0.5rem;
  }

  .table th,
  .table td {
    padding: 0.625rem 0.75rem;
  }

  /* Stack table on very small screens */
  .table {
    display: block;
  }

  .table thead {
    display: none;
  }

  .table tbody {
    display: block;
  }

  .table tr {
    display: flex;
    flex-direction: column;
    margin-bottom: 1rem;
    border: 1px solid #e2e8f0;
    border-radius: 0.5rem;
    overflow: hidden;
    background: white;
  }

  .table td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.75rem 1rem;
    text-align: left !important;
    border-bottom: 1px solid #f1f5f9;
  }

  .table td:last-child {
    border-bottom: none;
  }

  .table td::before {
    content: attr(data-label);
    font-weight: 600;
    color: #64748b;
    font-size: 0.75rem;
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }

  .table td:last-child {
    justify-content: flex-end;
  }

  .table td:last-child::before {
    display: none;
  }

  .action-buttons {
    width: 100%;
    justify-content: flex-end;
  }

  .input-edit {
    max-width: 60%;
  }
}
</style>
