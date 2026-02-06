<script setup lang="ts">
import { computed } from 'vue'
import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline'

const props = defineProps<{
  currentPage: number
  totalPages: number
  totalElements: number
  pageSize: number
}>()

const emit = defineEmits<{
  'page-change': [page: number]
  'page-size-change': [size: number]
}>()

const pageSizeOptions = [5, 10, 20, 50, 100]

const isFirstPage = computed(() => props.currentPage === 0)
const isLastPage = computed(() => props.currentPage === props.totalPages - 1)

const visiblePages = computed(() => {
  const pages: number[] = []
  const maxVisible = 5
  let start = Math.max(0, props.currentPage - 2)
  let end = Math.min(props.totalPages - 1, start + maxVisible - 1)

  if (end - start < maxVisible - 1) {
    start = Math.max(0, end - maxVisible + 1)
  }

  for (let i = start; i <= end; i++) {
    pages.push(i)
  }

  return pages
})

function goToPage(page: number) {
  if (page >= 0 && page < props.totalPages && page !== props.currentPage) {
    emit('page-change', page)
  }
}

function previousPage() {
  if (!isFirstPage.value) {
    goToPage(props.currentPage - 1)
  }
}

function nextPage() {
  if (!isLastPage.value) {
    goToPage(props.currentPage + 1)
  }
}

function handlePageSizeChange(event: Event) {
  const newSize = Number((event.target as HTMLSelectElement).value)
  emit('page-size-change', newSize)
}
</script>

<template>
  <div class="pagination">
    <div class="pagination-info">
      <span class="info-text">
        Showing {{ Math.min(currentPage * pageSize + 1, totalElements) }} to {{ Math.min((currentPage + 1) * pageSize, totalElements) }} of {{ totalElements }} results
      </span>
      <div class="page-size-selector">
        <label for="pageSize">Items per page:</label>
        <select id="pageSize" :value="pageSize" @change="handlePageSizeChange" class="page-size-select">
          <option v-for="size in pageSizeOptions" :key="size" :value="size">{{ size }}</option>
        </select>
      </div>
    </div>
    
    <div class="pagination-controls">
      <button 
        class="pagination-btn" 
        :disabled="isFirstPage"
        @click="previousPage"
        title="Previous page"
      >
        <ChevronLeftIcon class="icon" />
      </button>

      <button
        v-for="page in visiblePages"
        :key="page"
        class="pagination-btn"
        :class="{ active: page === currentPage }"
        @click="goToPage(page)"
      >
        {{ page + 1 }}
      </button>

      <button 
        class="pagination-btn" 
        :disabled="isLastPage"
        @click="nextPage"
        title="Next page"
      >
        <ChevronRightIcon class="icon" />
      </button>
    </div>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  border-top: 1px solid #e2e8f0;
  background: white;
}

.pagination-info {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.info-text {
  white-space: nowrap;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.page-size-selector label {
  font-size: 0.875rem;
  color: #64748b;
  white-space: nowrap;
}

.page-size-select {
  padding: 0.375rem 0.75rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.375rem;
  background: white;
  color: #0f172a;
  font-size: 0.875rem;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.15s ease;
}

.page-size-select:hover {
  border-color: #cbd5e1;
}

.page-size-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.pagination-controls {
  display: flex;
  gap: 0.25rem;
}

.pagination-btn {
  min-width: 2.25rem;
  height: 2.25rem;
  padding: 0 0.5rem;
  border: 1px solid #e2e8f0;
  background: white;
  color: #64748b;
  font-size: 0.875rem;
  font-weight: 500;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.15s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-btn:hover:not(:disabled) {
  background: #f8fafc;
  border-color: #cbd5e1;
  color: #0f172a;
}

.pagination-btn.active {
  background: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

.pagination-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.icon {
  width: 1.125rem;
  height: 1.125rem;
}

/* Responsive Design */
@media (max-width: 768px) {
  .pagination-container {
    padding: 0.875rem 1rem;
    gap: 0.75rem;
  }

  .pagination-info {
    font-size: 0.75rem;
  }

  .page-size-select {
    font-size: 0.8125rem;
    padding: 0.375rem 1.75rem 0.375rem 0.625rem;
  }

  .pagination-btn {
    min-width: 2rem;
    height: 2rem;
    font-size: 0.8125rem;
  }

  .icon {
    width: 1rem;
    height: 1rem;
  }
}

@media (max-width: 640px) {
  .pagination-container {
    flex-direction: column;
    align-items: stretch;
    padding: 0.75rem;
  }

  .pagination-left {
    justify-content: space-between;
    width: 100%;
  }

  .pagination-info {
    flex: 1;
  }

  .pagination-controls {
    width: 100%;
    justify-content: center;
  }

  /* Hide some page numbers on very small screens */
  .pagination-btn:not(.active):not(:disabled):nth-child(n+4):nth-last-child(n+4) {
    display: none;
  }
}
</style>
