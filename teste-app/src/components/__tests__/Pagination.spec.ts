import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Pagination from '../common/Pagination.vue'

describe('Pagination', () => {
  it('renders pagination info correctly', () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 0,
        totalPages: 5,
        totalElements: 47,
        pageSize: 10
      }
    })

    expect(wrapper.text()).toContain('Showing 1 to 10 of 47 results')
  })

  it('calculates showing range correctly for last page', () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 4,
        totalPages: 5,
        totalElements: 47,
        pageSize: 10
      }
    })

    expect(wrapper.text()).toContain('Showing 41 to 47 of 47 results')
  })

  it('disables previous button on first page', () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 0,
        totalPages: 5,
        totalElements: 50,
        pageSize: 10
      }
    })

    const prevButton = wrapper.findAll('button')[0]
    expect(prevButton.attributes('disabled')).toBeDefined()
  })

  it('disables next button on last page', () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 4,
        totalPages: 5,
        totalElements: 50,
        pageSize: 10
      }
    })

    const buttons = wrapper.findAll('button')
    const nextButton = buttons[buttons.length - 1]
    expect(nextButton.attributes('disabled')).toBeDefined()
  })

  it('emits page-change event when clicking next', async () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 0,
        totalPages: 5,
        totalElements: 50,
        pageSize: 10
      }
    })

    const buttons = wrapper.findAll('button')
    const nextButton = buttons[buttons.length - 1]
    await nextButton.trigger('click')

    expect(wrapper.emitted('page-change')).toBeTruthy()
    expect(wrapper.emitted('page-change')?.[0]).toEqual([1])
  })

  it('emits page-change event when clicking previous', async () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 2,
        totalPages: 5,
        totalElements: 50,
        pageSize: 10
      }
    })

    const prevButton = wrapper.findAll('button')[0]
    await prevButton.trigger('click')

    expect(wrapper.emitted('page-change')).toBeTruthy()
    expect(wrapper.emitted('page-change')?.[0]).toEqual([1])
  })

  it('emits page-size-change event when changing page size', async () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 0,
        totalPages: 5,
        totalElements: 50,
        pageSize: 10
      }
    })

    const select = wrapper.find('select')
    await select.setValue('20')

    expect(wrapper.emitted('page-size-change')).toBeTruthy()
    expect(wrapper.emitted('page-size-change')?.[0]).toEqual([20])
  })

  it('renders all page size options', () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 0,
        totalPages: 5,
        totalElements: 50,
        pageSize: 10
      }
    })

    const options = wrapper.findAll('option')
    const values = options.map(opt => opt.element.value)

    expect(values).toContain('5')
    expect(values).toContain('10')
    expect(values).toContain('20')
    expect(values).toContain('50')
    expect(values).toContain('100')
  })

  it('displays correct current page size', () => {
    const wrapper = mount(Pagination, {
      props: {
        currentPage: 0,
        totalPages: 5,
        totalElements: 50,
        pageSize: 20
      }
    })

    const select = wrapper.find('select')
    expect(select.element.value).toBe('20')
  })
})
