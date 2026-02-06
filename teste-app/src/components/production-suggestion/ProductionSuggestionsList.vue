<script setup lang="ts">
import { ref } from 'vue'
import { ChartBarIcon, MagnifyingGlassIcon, FunnelIcon } from '@heroicons/vue/24/outline'
import type { ProductionSuggestion } from '../../services/productionSuggestionService'
import Pagination from '../common/Pagination.vue'

const props = defineProps<{
  suggestions: ProductionSuggestion[]
  loading: boolean
  error: string | null
  currentPage: number
  pageSize: number
  totalElements: number
  totalPages: number
  searchName: string
  sortDirection: string
}>()

const emit = defineEmits<{
  'page-change': [page: number]
  'page-size-change': [size: number]
  'search-change': [search: string]
  'sort-change': [direction: string]
}>()

const localSearchName = ref(props.searchName)
const localSortDirection = ref(props.sortDirection)

function handleSearch() {
  emit('search-change', localSearchName.value)
}

function handleSortChange() {
  emit('sort-change', localSortDirection.value)
}

function formatCurrency(value: number) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value)
}

function formatQuantity(quantity: number) {
  return new Intl.NumberFormat('pt-BR', {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(quantity)
}
</script>

<template>
  <div class="production-suggestions-list">
    <div class="header">
      <h1>Production Suggestions</h1>
    </div>

    <div class="description">
      <p>
        Suggested production based on available stock. Products are prioritized by highest value.
        Each product shows the maximum quantity that can be produced with current raw materials.
      </p>
    </div>

    <div class="filters">
      <div class="search-box">
        <MagnifyingGlassIcon class="search-icon" />
        <input
          v-model="localSearchName"
          type="text"
          placeholder="Search by product name..."
          @keyup.enter="handleSearch"
        />
        <button @click="handleSearch" class="search-button">Search</button>
      </div>

      <div class="sort-box">
        <FunnelIcon class="sort-icon" />
        <select v-model="localSortDirection" @change="handleSortChange">
          <option value="">Sort by Priority (Default)</option>
          <option value="desc">Unit Value: High to Low</option>
          <option value="asc">Unit Value: Low to High</option>
        </select>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading production suggestions...</div>

    <div v-else-if="error" class="error-state">
      <p>{{ error }}</p>
    </div>

    <div v-else class="content-wrapper">
      <div class="table-container">
        <table class="table">
          <thead>
            <tr>
              <th>Priority</th>
              <th>Product</th>
              <th>Unit Value</th>
              <th>Suggested Quantity</th>
              <th>Total Value</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="suggestion in suggestions" :key="suggestion.productId">
              <td>
                <div class="priority-badge">
                  #{{ suggestion.priorityRank }}
                </div>
              </td>
              <td class="product-name">{{ suggestion.productName }}</td>
              <td>{{ formatCurrency(suggestion.productValue) }}</td>
              <td class="quantity">{{ formatQuantity(suggestion.suggestedQuantity) }}</td>
              <td class="total-value">{{ formatCurrency(suggestion.totalValue) }}</td>
            </tr>
          </tbody>
        </table>

        <div v-if="suggestions.length === 0" class="empty-state">
          <ChartBarIcon class="empty-icon" />
          <p>No production suggestions available.</p>
          <p class="empty-hint">Make sure you have products with associated raw materials and sufficient stock.</p>
        </div>
      </div>

      <Pagination
        v-if="totalElements > 0"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-elements="totalElements"
        :page-size="pageSize"
        @page-change="emit('page-change', $event)"
        @page-size-change="emit('page-size-change', $event)"
      />
    </div>
  </div>
</template>

<style scoped>
.production-suggestions-list {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 2rem;
  gap: 1.5rem;
}

.header {
  flex-shrink: 0;
}

.header h1 {
  font-size: 1.75rem;
  font-weight: 600;
  color: #0f172a;
  margin: 0;
  letter-spacing: -0.02em;
}

.filters {
  display: flex;
  gap: 1rem;
  flex-shrink: 0;
}

.search-box {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  padding: 0.625rem 1rem;
  transition: all 0.2s ease;
}

.search-box:focus-within {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: #94a3b8;
  flex-shrink: 0;
}

.search-box input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 0.875rem;
  color: #0f172a;
  background: transparent;
}

.search-box input::placeholder {
  color: #94a3b8;
}

.search-button {
  padding: 0.375rem 0.875rem;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s ease;
}

.search-button:hover {
  background: #2563eb;
}

.sort-box {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  padding: 0.625rem 1rem;
  min-width: 250px;
}

.sort-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: #94a3b8;
  flex-shrink: 0;
}

.sort-box select {
  flex: 1;
  border: none;
  outline: none;
  font-size: 0.875rem;
  color: #0f172a;
  background: transparent;
  cursor: pointer;
}

.description {
  background: #f8fafc;
  padding: 1rem 1.25rem;
  border-radius: 0.5rem;
  border-left: 3px solid #3b82f6;
  flex-shrink: 0;
}

.description p {
  margin: 0;
  color: #475569;
  font-size: 0.875rem;
  line-height: 1.6;
}

.loading {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
  font-size: 0.9375rem;
}

.error-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ef4444;
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

.table th:nth-child(3),
.table th:nth-child(4),
.table th:nth-child(5) {
  text-align: right;
}

.table td {
  padding: 1rem 1.5rem;
  color: #0f172a;
  font-size: 0.875rem;
  border-bottom: 1px solid #f1f5f9;
}

.table td:nth-child(3),
.table td:nth-child(4),
.table td:nth-child(5) {
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

.priority-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.25rem 0.625rem;
  background: #eff6ff;
  color: #2563eb;
  font-weight: 600;
  font-size: 0.75rem;
  border-radius: 0.375rem;
}

.product-name {
  font-weight: 500;
  color: #0f172a;
}

.quantity {
  font-weight: 600;
  color: #059669;
}

.total-value {
  font-weight: 700;
  color: #3b82f6;
}

.empty-state {
  padding: 4rem 2rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.empty-icon {
  width: 3rem;
  height: 3rem;
  color: #cbd5e1;
}

.empty-state p {
  color: #94a3b8;
  font-size: 0.9375rem;
  margin: 0;
}

.empty-hint {
  font-size: 0.8125rem !important;
  color: #cbd5e1 !important;
}

.icon {
  width: 1.125rem;
  height: 1.125rem;
  flex-shrink: 0;
}
</style>
