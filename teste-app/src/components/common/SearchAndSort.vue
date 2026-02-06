<script setup lang="ts">
import { ref, watch } from 'vue'
import { MagnifyingGlassIcon } from '@heroicons/vue/24/outline'

const props = defineProps<{
  searchPlaceholder?: string
  sortOptions: Array<{ value: string; label: string }>
  modelSearch?: string
  modelSortBy?: string
  modelSortDirection?: string
}>()

const emit = defineEmits<{
  'update:modelSearch': [value: string]
  'update:modelSortBy': [value: string]
  'update:modelSortDirection': [value: string]
  search: []
}>()

const localSearch = ref(props.modelSearch || '')
const localSortBy = ref(props.modelSortBy || '')
const localSortDirection = ref(props.modelSortDirection || 'asc')

watch(() => props.modelSearch, (newVal) => {
  localSearch.value = newVal || ''
})

watch(() => props.modelSortBy, (newVal) => {
  localSortBy.value = newVal || ''
})

watch(() => props.modelSortDirection, (newVal) => {
  localSortDirection.value = newVal || 'asc'
})

function handleSearch() {
  emit('update:modelSearch', localSearch.value)
  emit('search')
}

function handleSortChange() {
  emit('update:modelSortBy', localSortBy.value)
  emit('search')
}

function handleDirectionChange() {
  emit('update:modelSortDirection', localSortDirection.value)
  emit('search')
}

function handleKeyPress(event: KeyboardEvent) {
  if (event.key === 'Enter') {
    handleSearch()
  }
}
</script>

<template>
  <div class="search-and-sort">
    <div class="search-box">
      <MagnifyingGlassIcon class="search-icon" />
      <input
        v-model="localSearch"
        type="text"
        :placeholder="searchPlaceholder || 'Search...'"
        class="search-input"
        @keypress="handleKeyPress"
        @blur="handleSearch"
      />
    </div>

    <div class="sort-controls">
      <select
        v-model="localSortBy"
        class="sort-select"
        @change="handleSortChange"
      >
        <option value="">No sorting</option>
        <option v-for="option in sortOptions" :key="option.value" :value="option.value">
          {{ option.label }}
        </option>
      </select>

      <select
        v-if="localSortBy"
        v-model="localSortDirection"
        class="sort-select direction-select"
        @change="handleDirectionChange"
      >
        <option value="asc">↑ Ascending</option>
        <option value="desc">↓ Descending</option>
      </select>
    </div>
  </div>
</template>

<style scoped>
.search-and-sort {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 200px;
}

.search-icon {
  position: absolute;
  left: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  width: 1.125rem;
  height: 1.125rem;
  color: #94a3b8;
  pointer-events: none;
}

.search-input {
  width: 100%;
  padding: 0.625rem 0.75rem 0.625rem 2.5rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-family: inherit;
  transition: all 0.15s ease;
  background: white;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-input::placeholder {
  color: #94a3b8;
}

.sort-controls {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.sort-select {
  padding: 0.625rem 0.75rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-family: inherit;
  background: white;
  color: #0f172a;
  cursor: pointer;
  transition: all 0.15s ease;
  min-width: 140px;
}

.direction-select {
  min-width: 120px;
}

.sort-select:hover {
  border-color: #cbd5e1;
}

.sort-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* Responsive Design */
@media (max-width: 768px) {
  .search-sort-container {
    gap: 0.75rem;
  }

  .search-box {
    flex: 1 1 100%;
    min-width: 100%;
  }

  .sort-controls {
    flex: 1 1 100%;
    width: 100%;
  }

  .sort-select {
    flex: 1;
    min-width: 0;
  }

  .direction-select {
    min-width: 0;
    flex: 0 0 auto;
  }
}

@media (max-width: 640px) {
  .search-sort-container {
    padding: 0.75rem;
  }

  .sort-controls {
    flex-direction: column;
    gap: 0.5rem;
  }

  .sort-select,
  .direction-select {
    width: 100%;
  }
}
</style>
