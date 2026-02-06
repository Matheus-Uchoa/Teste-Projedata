import api from './api'
import type { PageResponse } from './productService'

export interface ProductionSuggestion {
  productId: number
  productName: string
  productValue: number
  suggestedQuantity: number
  totalValue: number
  priorityRank: number
}

export const productionSuggestionService = {
  async getAll(
    page: number = 0,
    size: number = 10,
    searchName: string = '',
    sortDirection: string = ''
  ): Promise<PageResponse<ProductionSuggestion>> {
    const params = new URLSearchParams()
    params.append('page', page.toString())
    params.append('size', size.toString())
    if (searchName) params.append('searchName', searchName)
    if (sortDirection) params.append('sortDirection', sortDirection)

    const response = await api.get(`/production-suggestions?${params.toString()}`)
    return response.data
  }
}
