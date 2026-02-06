import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productionSuggestionService, type ProductionSuggestion } from '@/services/productionSuggestionService'

export const useProductionSuggestionStore = defineStore('productionSuggestion', () => {
  const suggestions = ref<ProductionSuggestion[]>([])
  const currentPage = ref(0)
  const pageSize = ref(10)
  const totalElements = ref(0)
  const totalPages = ref(0)
  const searchName = ref('')
  const sortDirection = ref('')
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchSuggestions() {
    loading.value = true
    error.value = null
    try {
      const response = await productionSuggestionService.getAll(
        currentPage.value,
        pageSize.value,
        searchName.value,
        sortDirection.value
      )
      suggestions.value = response.content
      currentPage.value = response.pageNumber
      pageSize.value = response.pageSize
      totalElements.value = response.totalElements
      totalPages.value = response.totalPages
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to load production suggestions'
      console.error('Error fetching production suggestions:', e)
    } finally {
      loading.value = false
    }
  }

  function setPage(page: number) {
    currentPage.value = page
    fetchSuggestions()
  }

  function setPageSize(size: number) {
    pageSize.value = size
    currentPage.value = 0
    fetchSuggestions()
  }

  function setSearchName(search: string) {
    searchName.value = search
    currentPage.value = 0
    fetchSuggestions()
  }

  function setSortDirection(direction: string) {
    sortDirection.value = direction
    currentPage.value = 0
    fetchSuggestions()
  }

  return {
    suggestions,
    currentPage,
    pageSize,
    totalElements,
    totalPages,
    searchName,
    sortDirection,
    loading,
    error,
    fetchSuggestions,
    setPage,
    setPageSize,
    setSearchName,
    setSortDirection
  }
})
