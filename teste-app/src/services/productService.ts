import api from './api'

export interface Product {
  id?: number
  name: string
  value: number
}

export interface PageResponse<T> {
  content: T[]
  pageNumber: number
  pageSize: number
  totalElements: number
  totalPages: number
}

export const productService = {
  async getAll(page: number = 0, size: number = 10, search?: string, sortBy?: string, sortDirection?: string): Promise<PageResponse<Product>> {
    const response = await api.get('/products', {
      params: { page, size, search, sortBy, sortDirection }
    })
    return response.data
  },

  async getById(id: number): Promise<Product> {
    const response = await api.get(`/products/${id}`)
    return response.data
  },

  async create(product: Product): Promise<Product> {
    const response = await api.post('/products', product)
    return response.data
  },

  async update(id: number, product: Product): Promise<Product> {
    const response = await api.put(`/products/${id}`, product)
    return response.data
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/products/${id}`)
  }
}
