import api from './api'
import type { PageResponse } from './productService'

export interface ProductRawMaterial {
  id?: number
  productId?: number
  rawMaterialId: number
  rawMaterialName?: string
  quantityNeeded: number
}

export const productRawMaterialService = {
  async getByProductId(productId: number, page: number = 0, size: number = 10): Promise<PageResponse<ProductRawMaterial>> {
    const response = await api.get(`/products/${productId}/raw-materials`, {
      params: { page, size }
    })
    return response.data
  },

  async create(productId: number, association: ProductRawMaterial): Promise<ProductRawMaterial> {
    const response = await api.post(`/products/${productId}/raw-materials`, {
      rawMaterialId: association.rawMaterialId,
      quantityNeeded: association.quantityNeeded
    })
    return response.data
  },

  async update(productId: number, id: number, association: ProductRawMaterial): Promise<ProductRawMaterial> {
    const response = await api.put(`/products/${productId}/raw-materials/${id}`, {
      rawMaterialId: association.rawMaterialId,
      quantityNeeded: association.quantityNeeded
    })
    return response.data
  },

  async delete(productId: number, id: number): Promise<void> {
    await api.delete(`/products/${productId}/raw-materials/${id}`)
  }
}
