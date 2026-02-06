import api from './api'
import type { PageResponse } from './productService'

export interface RawMaterial {
  id?: number
  name: string
  stockQuantity: number
}

export const rawMaterialService = {
  async getAll(page: number = 0, size: number = 10, search?: string, sortBy?: string, sortDirection?: string): Promise<PageResponse<RawMaterial>> {
    const response = await api.get('/raw-materials', {
      params: { page, size, search, sortBy, sortDirection }
    })
    return response.data
  },

  async getAllUnpaginated(): Promise<RawMaterial[]> {
    const response = await api.get('/raw-materials', {
      params: { page: 0, size: 1000 }
    })
    return response.data.content
  },

  async getById(id: number): Promise<RawMaterial> {
    const response = await api.get(`/raw-materials/${id}`)
    return response.data
  },

  async create(rawMaterial: RawMaterial): Promise<RawMaterial> {
    const response = await api.post('/raw-materials', rawMaterial)
    return response.data
  },

  async update(id: number, rawMaterial: RawMaterial): Promise<RawMaterial> {
    const response = await api.put(`/raw-materials/${id}`, rawMaterial)
    return response.data
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/raw-materials/${id}`)
  }
}
