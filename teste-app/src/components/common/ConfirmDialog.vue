<script setup lang="ts">
import { ExclamationTriangleIcon, XMarkIcon } from '@heroicons/vue/24/outline'

const props = defineProps<{
  title?: string
  message: string
  confirmText?: string
  cancelText?: string
  variant?: 'danger' | 'warning'
  error?: string | null
}>()

const emit = defineEmits<{
  confirm: []
  cancel: []
}>()

function handleConfirm() {
  emit('confirm')
}

function handleCancel() {
  emit('cancel')
}

function handleBackdropClick(event: MouseEvent) {
  if (event.target === event.currentTarget) {
    handleCancel()
  }
}
</script>

<template>
  <div class="modal-backdrop" @click="handleBackdropClick">
    <div class="modal-dialog">
      <div class="modal-header">
        <div class="icon-wrapper" :class="variant || 'danger'">
          <ExclamationTriangleIcon class="icon" />
        </div>
        <button class="btn-close" @click="handleCancel" title="Close">
          <XMarkIcon class="icon-sm" />
        </button>
      </div>

      <div class="modal-body">
        <h3 class="title">{{ title || 'Confirm Action' }}</h3>
        <p class="message">{{ message }}</p>
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn btn-secondary" @click="handleCancel">
          {{ cancelText || 'Cancel' }}
        </button>
        <button class="btn btn-danger" @click="handleConfirm">
          {{ confirmText || 'Confirm' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.15s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-dialog {
  background: white;
  border-radius: 0.75rem;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  width: 90%;
  max-width: 450px;
  animation: slideIn 0.2s ease;
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  padding: 1.5rem 1.5rem 0 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.icon-wrapper {
  width: 3rem;
  height: 3rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-wrapper.danger {
  background: #fef2f2;
}

.icon-wrapper.warning {
  background: #fffbeb;
}

.icon-wrapper .icon {
  width: 1.5rem;
  height: 1.5rem;
}

.icon-wrapper.danger .icon {
  color: #ef4444;
}

.icon-wrapper.warning .icon {
  color: #f59e0b;
}

.btn-close {
  background: transparent;
  border: none;
  padding: 0.25rem;
  cursor: pointer;
  color: #94a3b8;
  border-radius: 0.375rem;
  transition: all 0.15s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close:hover {
  background: #f1f5f9;
  color: #64748b;
}

.icon-sm {
  width: 1.25rem;
  height: 1.25rem;
}

.modal-body {
  padding: 1.25rem 1.5rem;
}

.title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #0f172a;
  margin: 0 0 0.5rem 0;
  letter-spacing: -0.01em;
}

.message {
  font-size: 0.875rem;
  color: #64748b;
  line-height: 1.6;
  margin: 0;
}

.error-message {
  margin-top: 0.75rem;
  padding: 0.75rem;
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 0.5rem;
  color: #dc2626;
  font-size: 0.875rem;
  line-height: 1.5;
}

.modal-footer {
  padding: 1rem 1.5rem 1.5rem 1.5rem;
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
}

.btn {
  padding: 0.625rem 1.25rem;
  border: none;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
}

.btn-secondary {
  background: white;
  color: #64748b;
  border: 1px solid #e2e8f0;
}

.btn-secondary:hover {
  background: #f8fafc;
  color: #475569;
  border-color: #cbd5e1;
}

.btn-danger {
  background: #ef4444;
  color: white;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.btn-danger:hover {
  background: #dc2626;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25);
  transform: translateY(-1px);
}

.btn-danger:active {
  transform: translateY(0);
}
</style>
