import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ConfirmDialog from '../common/ConfirmDialog.vue'

describe('ConfirmDialog', () => {
  it('renders with default props', () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?'
      }
    })

    expect(wrapper.text()).toContain('Confirm Action')
    expect(wrapper.text()).toContain('Are you sure?')
    expect(wrapper.text()).toContain('Cancel')
    expect(wrapper.text()).toContain('Confirm')
  })

  it('renders with custom props', () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        title: 'Delete Item',
        message: 'This action cannot be undone',
        confirmText: 'Delete',
        cancelText: 'No, keep it',
        variant: 'danger'
      }
    })

    expect(wrapper.text()).toContain('Delete Item')
    expect(wrapper.text()).toContain('This action cannot be undone')
    expect(wrapper.text()).toContain('Delete')
    expect(wrapper.text()).toContain('No, keep it')
  })

  it('emits confirm event when confirm button is clicked', async () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?'
      }
    })

    const confirmButton = wrapper.findAll('button').find(btn =>
      btn.text().includes('Confirm')
    )
    await confirmButton?.trigger('click')

    expect(wrapper.emitted('confirm')).toBeTruthy()
    expect(wrapper.emitted('confirm')?.length).toBe(1)
  })

  it('emits cancel event when cancel button is clicked', async () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?'
      }
    })

    const cancelButton = wrapper.findAll('button').find(btn =>
      btn.text().includes('Cancel')
    )
    await cancelButton?.trigger('click')

    expect(wrapper.emitted('cancel')).toBeTruthy()
    expect(wrapper.emitted('cancel')?.length).toBe(1)
  })

  it('emits cancel event when close button is clicked', async () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?'
      }
    })

    const closeButton = wrapper.find('.btn-close')
    await closeButton.trigger('click')

    expect(wrapper.emitted('cancel')).toBeTruthy()
  })

  it('emits cancel event when backdrop is clicked', async () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?'
      }
    })

    const backdrop = wrapper.find('.modal-backdrop')
    await backdrop.trigger('click')

    expect(wrapper.emitted('cancel')).toBeTruthy()
  })

  it('does not emit cancel when clicking inside modal dialog', async () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?'
      }
    })

    const dialog = wrapper.find('.modal-dialog')
    await dialog.trigger('click')

    expect(wrapper.emitted('cancel')).toBeFalsy()
  })

  it('applies danger variant class', () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?',
        variant: 'danger'
      }
    })

    const iconWrapper = wrapper.find('.icon-wrapper')
    expect(iconWrapper.classes()).toContain('danger')
  })

  it('applies warning variant class', () => {
    const wrapper = mount(ConfirmDialog, {
      props: {
        message: 'Are you sure?',
        variant: 'warning'
      }
    })

    const iconWrapper = wrapper.find('.icon-wrapper')
    expect(iconWrapper.classes()).toContain('warning')
  })
})
