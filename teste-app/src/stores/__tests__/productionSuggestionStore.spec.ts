import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useProductionSuggestionStore } from '../productionSuggestionStore'
import { productionSuggestionService } from '@/services/productionSuggestionService'

vi.mock('@/services/productionSuggestionService')

describe('Production Suggestion Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('initializes with default state', () => {
    const store = useProductionSuggestionStore()

    expect(store.suggestions).toEqual([])
    expect(store.currentPage).toBe(0)
    expect(store.pageSize).toBe(10)
    expect(store.totalElements).toBe(0)
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
  })

  it('fetches suggestions successfully', async () => {
    const store = useProductionSuggestionStore()
    const mockResponse = {
      content: [
        {
          productId: 1,
          productName: 'Product A',
          productValue: 10,
          suggestedQuantity: 100,
          totalValue: 1000,
          priorityRank: 1
        },
        {
          productId: 2,
          productName: 'Product B',
          productValue: 20,
          suggestedQuantity: 50,
          totalValue: 1000,
          priorityRank: 2
        }
      ],
      totalElements: 2,
      totalPages: 1,
      pageNumber: 0,
      pageSize: 10
    }

    vi.mocked(productionSuggestionService.getAll).mockResolvedValue(mockResponse)

    await store.fetchSuggestions()

    expect(store.suggestions).toEqual(mockResponse.content)
    expect(store.totalElements).toBe(2)
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
  })

  it('handles fetch error', async () => {
    const store = useProductionSuggestionStore()
    const errorMessage = 'Failed to load suggestions'

    vi.mocked(productionSuggestionService.getAll).mockRejectedValue({
      response: { data: { error: errorMessage } }
    })

    await store.fetchSuggestions()

    expect(store.error).toBe(errorMessage)
    expect(store.loading).toBe(false)
    expect(store.suggestions).toEqual([])
  })

  it('handles empty suggestions', async () => {
    const store = useProductionSuggestionStore()
    const mockResponse = {
      content: [],
      totalElements: 0,
      totalPages: 0,
      pageNumber: 0,
      pageSize: 10
    }

    vi.mocked(productionSuggestionService.getAll).mockResolvedValue(mockResponse)

    await store.fetchSuggestions()

    expect(store.suggestions).toEqual([])
    expect(store.totalElements).toBe(0)
    expect(store.error).toBeNull()
  })
})
