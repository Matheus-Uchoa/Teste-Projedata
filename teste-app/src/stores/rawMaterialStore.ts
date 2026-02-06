import { ref } from 'vue'
import { defineStore } from 'pinia'
import { rawMaterialService, type RawMaterial } from '@/services/rawMaterialService'

export const useRawMaterialStore = defineStore('rawMaterial', () => {
  const rawMaterials = ref<RawMaterial[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const totalPages = ref(0)
  const totalElements = ref(0)
  const searchTerm = ref('')
  const sortBy = ref('')
  const sortDirection = ref('asc')

  async function fetchRawMaterials(
    page: number = currentPage.value,
    size: number = pageSize.value,
    search: string = searchTerm.value,
    sort: string = sortBy.value,
    direction: string = sortDirection.value
  ) {
    loading.value = true
    error.value = null
    try {
      const response = await rawMaterialService.getAll(page, size, search, sort, direction)
      rawMaterials.value = response.content
      currentPage.value = response.pageNumber
      pageSize.value = response.pageSize
      totalPages.value = response.totalPages
      totalElements.value = response.totalElements
      searchTerm.value = search
      sortBy.value = sort
      sortDirection.value = direction
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to fetch raw materials'
      console.error('Error fetching raw materials:', e)
    } finally {
      loading.value = false
    }
  }

  async function createRawMaterial(rawMaterial: RawMaterial) {
    loading.value = true
    error.value = null
    try {
      await rawMaterialService.create(rawMaterial)
      await fetchRawMaterials(currentPage.value, pageSize.value)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to create raw material'
      console.error('Error creating raw material:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  async function updateRawMaterial(id: number, rawMaterial: RawMaterial) {
    loading.value = true
    error.value = null
    try {
      const updated = await rawMaterialService.update(id, rawMaterial)
      const index = rawMaterials.value.findIndex(rm => rm.id === id)
      if (index !== -1) {
        rawMaterials.value[index] = updated
      }
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to update raw material'
      console.error('Error updating raw material:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  async function deleteRawMaterial(id: number) {
    loading.value = true
    error.value = null
    try {
      await rawMaterialService.delete(id)
      await fetchRawMaterials(currentPage.value, pageSize.value)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to delete raw material'
      console.error('Error deleting raw material:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  async function fetchAllRawMaterialsForDropdown() {
    try {
      const allMaterials = await rawMaterialService.getAllUnpaginated()
      return allMaterials
    } catch (e: any) {
      console.error('Error fetching all raw materials:', e)
      return []
    }
  }

  return {
    rawMaterials,
    loading,
    error,
    currentPage,
    pageSize,
    totalPages,
    totalElements,
    searchTerm,
    sortBy,
    sortDirection,
    fetchRawMaterials,
    createRawMaterial,
    updateRawMaterial,
    deleteRawMaterial,
    fetchAllRawMaterialsForDropdown
  }
})
