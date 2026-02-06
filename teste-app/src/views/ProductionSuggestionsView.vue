<script setup lang="ts">
import { onMounted } from 'vue'
import { useProductionSuggestionStore } from '@/stores/productionSuggestionStore'
import ProductionSuggestionsList from '@/components/production-suggestion/ProductionSuggestionsList.vue'

const productionSuggestionStore = useProductionSuggestionStore()

onMounted(() => {
  productionSuggestionStore.fetchSuggestions()
})
</script>

<template>
  <div class="production-suggestions-view">
    <ProductionSuggestionsList
      :suggestions="productionSuggestionStore.suggestions"
      :loading="productionSuggestionStore.loading"
      :error="productionSuggestionStore.error"
      :current-page="productionSuggestionStore.currentPage"
      :page-size="productionSuggestionStore.pageSize"
      :total-elements="productionSuggestionStore.totalElements"
      :total-pages="productionSuggestionStore.totalPages"
      :search-name="productionSuggestionStore.searchName"
      :sort-direction="productionSuggestionStore.sortDirection"
      @page-change="productionSuggestionStore.setPage"
      @page-size-change="productionSuggestionStore.setPageSize"
      @search-change="productionSuggestionStore.setSearchName"
      @sort-change="productionSuggestionStore.setSortDirection"
    />
  </div>
</template>

<style scoped>
.production-suggestions-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
