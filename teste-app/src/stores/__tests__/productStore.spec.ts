import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useProductStore } from '../productStore'
import { productService } from '@/services/productService'

vi.mock('@/services/productService')

describe('Product Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('initializes with default state', () => {
    const store = useProductStore()

    expect(store.products).toEqual([])
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
    expect(store.currentPage).toBe(0)
    expect(store.pageSize).toBe(10)
    expect(store.totalPages).toBe(0)
    expect(store.totalElements).toBe(0)
    expect(store.searchTerm).toBe('')
    expect(store.sortBy).toBe('')
    expect(store.sortDirection).toBe('asc')
  })

  it('fetches products successfully', async () => {
    const store = useProductStore()
    const mockResponse = {
      content: [
        { id: 1, name: 'Product 1', value: 100 },
        { id: 2, name: 'Product 2', value: 200 }
      ],
      pageNumber: 0,
      pageSize: 10,
      totalElements: 2,
      totalPages: 1
    }

    vi.mocked(productService.getAll).mockResolvedValue(mockResponse)

    await store.fetchProducts()

    expect(store.products).toEqual(mockResponse.content)
    expect(store.currentPage).toBe(0)
    expect(store.totalPages).toBe(1)
    expect(store.totalElements).toBe(2)
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
  })

  it('handles fetch error', async () => {
    const store = useProductStore()
    const errorMessage = 'Network error'

    vi.mocked(productService.getAll).mockRejectedValue({
      response: { data: { error: errorMessage } }
    })

    await store.fetchProducts()

    expect(store.error).toBe(errorMessage)
    expect(store.loading).toBe(false)
  })

  it('creates product successfully', async () => {
    const store = useProductStore()
    const newProduct = { name: 'New Product', value: 150 }
    const mockResponse = { id: 3, ...newProduct }
    const mockPageResponse = {
      content: [mockResponse],
      pageNumber: 0,
      pageSize: 10,
      totalElements: 1,
      totalPages: 1
    }

    vi.mocked(productService.create).mockResolvedValue(mockResponse)
    vi.mocked(productService.getAll).mockResolvedValue(mockPageResponse)

    await store.createProduct(newProduct)

    expect(productService.create).toHaveBeenCalledWith(newProduct)
    expect(productService.getAll).toHaveBeenCalled()
  })

  it('updates product successfully', async () => {
    const store = useProductStore()
    store.products = [{ id: 1, name: 'Product 1', value: 100 }]

    const updatedProduct = { id: 1, name: 'Updated Product', value: 200 }
    vi.mocked(productService.update).mockResolvedValue(updatedProduct)

    await store.updateProduct(1, updatedProduct)

    expect(store.products[0]).toEqual(updatedProduct)
    expect(productService.update).toHaveBeenCalledWith(1, updatedProduct)
  })

  it('deletes product successfully', async () => {
    const store = useProductStore()
    const mockPageResponse = {
      content: [],
      pageNumber: 0,
      pageSize: 10,
      totalElements: 0,
      totalPages: 0
    }

    vi.mocked(productService.delete).mockResolvedValue(undefined)
    vi.mocked(productService.getAll).mockResolvedValue(mockPageResponse)

    await store.deleteProduct(1)

    expect(productService.delete).toHaveBeenCalledWith(1)
    expect(productService.getAll).toHaveBeenCalled()
  })

  it('fetches products with search and sort', async () => {
    const store = useProductStore()
    const mockResponse = {
      content: [{ id: 1, name: 'Found Product', value: 100 }],
      pageNumber: 0,
      pageSize: 10,
      totalElements: 1,
      totalPages: 1
    }

    vi.mocked(productService.getAll).mockResolvedValue(mockResponse)

    await store.fetchProducts(0, 10, 'search term', 'value', 'desc')

    expect(productService.getAll).toHaveBeenCalledWith(0, 10, 'search term', 'value', 'desc')
    expect(store.searchTerm).toBe('search term')
    expect(store.sortBy).toBe('value')
    expect(store.sortDirection).toBe('desc')
  })
})
