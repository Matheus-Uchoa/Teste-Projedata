import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productService, type Product } from '@/services/productService'

export const useProductStore = defineStore('product', () => {
  const products = ref<Product[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const totalPages = ref(0)
  const totalElements = ref(0)
  const searchTerm = ref('')
  const sortBy = ref('')
  const sortDirection = ref('asc')

  async function fetchProducts(
    page: number = currentPage.value,
    size: number = pageSize.value,
    search: string = searchTerm.value,
    sort: string = sortBy.value,
    direction: string = sortDirection.value
  ) {
    loading.value = true
    error.value = null
    try {
      const response = await productService.getAll(page, size, search, sort, direction)
      products.value = response.content
      currentPage.value = response.pageNumber
      pageSize.value = response.pageSize
      totalPages.value = response.totalPages
      totalElements.value = response.totalElements
      searchTerm.value = search
      sortBy.value = sort
      sortDirection.value = direction
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to load products'
      console.error('Error fetching products:', e)
    } finally {
      loading.value = false
    }
  }

  async function createProduct(product: Product): Promise<Product | null> {
    loading.value = true
    error.value = null
    try {
      const createdProduct = await productService.create(product)
      await fetchProducts(currentPage.value, pageSize.value)
      return createdProduct
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to create product'
      console.error('Error creating product:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  async function updateProduct(id: number, product: Product) {
    loading.value = true
    error.value = null
    try {
      const updated = await productService.update(id, product)
      const index = products.value.findIndex(p => p.id === id)
      if (index !== -1) {
        products.value[index] = updated
      }
      return updated
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to update product'
      console.error('Error updating product:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  async function deleteProduct(id: number) {
    loading.value = true
    error.value = null
    try {
      await productService.delete(id)
      await fetchProducts(currentPage.value, pageSize.value)
    } catch (e: any) {
      error.value = e.response?.data?.error || 'Failed to delete product'
      console.error('Error deleting product:', e)
      throw e
    } finally {
      loading.value = false
    }
  }

  return {
    products,
    loading,
    error,
    currentPage,
    pageSize,
    totalPages,
    totalElements,
    searchTerm,
    sortBy,
    sortDirection,
    fetchProducts,
    createProduct,
    updateProduct,
    deleteProduct
  }
})
