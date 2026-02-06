import { ref } from 'vue'
import { defineStore } from 'pinia'
import { productRawMaterialService, type ProductRawMaterial } from '@/services/productRawMaterialService'

export const useProductRawMaterialStore = defineStore('productRawMaterial', () => {
  const associations = ref<ProductRawMaterial[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const totalPages = ref(0)
  const totalElements = ref(0)

  async function fetchByProductId(productId: number, page: number = currentPage.value, size: number = pageSize.value) {
    loading.value = true
    error.value = null
    try {
      const response = await productRawMaterialService.getByProductId(productId, page, size)
      associations.value = response.content
      currentPage.value = response.pageNumber
      pageSize.value = response.pageSize
      totalPages.value = response.totalPages
      totalElements.value = response.totalElements
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to fetch associations'
      console.error('Error fetching associations:', e)
    } finally {
      loading.value = false
    }
  }

  async function createAssociation(productId: number, association: ProductRawMaterial) {
    error.value = null
    try {
      await productRawMaterialService.create(productId, association)
      await fetchByProductId(productId, currentPage.value, pageSize.value)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to create association'
      console.error('Error creating association:', e)
      throw e
    }
  }

  async function updateAssociation(productId: number, rawMaterialId: number, association: ProductRawMaterial) {
    error.value = null
    try {
      await productRawMaterialService.update(productId, rawMaterialId, association)
      await fetchByProductId(productId, currentPage.value, pageSize.value)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to update association'
      console.error('Error updating association:', e)
      throw e
    }
  }

  async function deleteAssociation(productId: number, rawMaterialId: number) {
    error.value = null
    try {
      await productRawMaterialService.delete(productId, rawMaterialId)
      await fetchByProductId(productId, currentPage.value, pageSize.value)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to delete association'
      console.error('Error deleting association:', e)
      throw e
    }
  }

  return {
    associations,
    loading,
    error,
    currentPage,
    pageSize,
    totalPages,
    totalElements,
    fetchByProductId,
    createAssociation,
    updateAssociation,
    deleteAssociation
  }
})
