<script setup lang="ts">
import { ref, watch } from 'vue'
import { PencilIcon, TrashIcon, CheckIcon, XMarkIcon, PlusIcon } from '@heroicons/vue/24/outline'
import type { ProductRawMaterial } from '../../services/productRawMaterialService'
import Pagination from '../common/Pagination.vue'

const props = defineProps<{
  associations: ProductRawMaterial[]
  loading: boolean
  error: string | null
  currentPage: number
  pageSize: number
  totalPages: number
  totalElements: number
}>()

const emit = defineEmits<{
  create: []
  update: [rawMaterialId: number, association: ProductRawMaterial]
  delete: [rawMaterialId: number]
  pageChange: [page: number]
  pageSizeChange: [size: number]
}>()

const editingId = ref<number | null>(null)
const editForm = ref<ProductRawMaterial>({ rawMaterialId: 0, quantityNeeded: 0 })

function startEdit(association: ProductRawMaterial) {
  editingId.value = association.id!
  editForm.value = { ...association }
}

function cancelEdit() {
  editingId.value = null
  editForm.value = { rawMaterialId: 0, quantityNeeded: 0 }
}

function saveEdit(rawMaterialId: number) {
  emit('update', rawMaterialId, editForm.value)
}

watch(() => props.loading, (newLoading, oldLoading) => {
  if (oldLoading && !newLoading) {
    if (!props.error && editingId.value) {
      cancelEdit()
    }
  }
})

function formatQuantity(quantity: number) {
  return new Intl.NumberFormat('pt-BR', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(quantity)
}
</script>

<template>
  <div class="material-list">
    <div class="header">
      <h2>Raw Materials</h2>
      <button class="btn btn-primary" @click="$emit('create')">
        <PlusIcon class="icon" /> Add Material
      </button>
    </div>

    <div v-if="loading" class="loading">Loading...</div>

    <div v-else class="content-wrapper">
      <div class="table-container">
        <table class="table">
        <thead>
          <tr>
            <th>Raw Material</th>
            <th>Quantity Needed</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="association in associations" :key="association.id">
            <tr>
              <template v-if="editingId !== association.id">
                <td>{{ association.rawMaterialName }}</td>
                <td>{{ formatQuantity(association.quantityNeeded) }}</td>
                <td>
                  <div class="actions">
                    <button class="btn-icon btn-edit" @click="startEdit(association)" title="Edit">
                      <PencilIcon class="icon-sm" />
                    </button>
                    <button class="btn-icon btn-delete" @click="$emit('delete', association.rawMaterialId!)" title="Delete">
                      <TrashIcon class="icon-sm" />
                    </button>
                  </div>
                </td>
              </template>

              <template v-else>
                <td>{{ association.rawMaterialName }}</td>
                <td>
                  <input
                    v-model.number="editForm.quantityNeeded"
                    type="number"
                    step="0.01"
                    class="input-edit"
                    placeholder="Quantity needed"
                    required
                  />
                </td>
                <td>
                  <div class="actions">
                    <button class="btn-icon btn-save" @click="saveEdit(association.rawMaterialId!)" title="Save">
                      <CheckIcon class="icon-sm" />
                    </button>
                    <button class="btn-icon btn-cancel" @click="cancelEdit" title="Cancel">
                      <XMarkIcon class="icon-sm" />
                    </button>
                  </div>
                </td>
              </template>
            </tr>
            <tr v-if="editingId === association.id && error" class="error-row">
              <td colspan="3">
                <div class="inline-error">{{ error }}</div>
              </td>
            </tr>
          </template>
          </tbody>
        </table>

        <div v-if="associations.length === 0" class="empty-state">
          <p>No raw materials associated yet. Add your first material!</p>
        </div>
      </div>

      <Pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total-pages="totalPages"
        :total-elements="totalElements"
        @page-change="(page) => $emit('pageChange', page)"
        @page-size-change="(size) => $emit('pageSizeChange', size)"
      />
    </div>
  </div>
</template>

<style scoped>
.material-list {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 1.5rem 2rem;
  gap: 1.5rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-shrink: 0;
}

.header h2 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #0f172a;
  margin: 0 0 0.25rem 0;
  letter-spacing: -0.01em;
}

.btn {
  padding: 0.625rem 1rem;
  border: none;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  font-family: inherit;
}

.btn-primary {
  background: #3b82f6;
  color: white;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.btn-primary:hover {
  background: #2563eb;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
  transform: translateY(-1px);
}

.btn-primary:active {
  transform: translateY(0);
}

.loading {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
  font-size: 0.9375rem;
}

.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 0.75rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.08), 0 1px 2px 0 rgba(0, 0, 0, 0.02);
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

.table-container {
  flex: 1;
  overflow: auto;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table thead {
  background: #f8fafc;
  position: sticky;
  top: 0;
  z-index: 1;
  border-bottom: 1px solid #e2e8f0;
}

.table th {
  padding: 0.875rem 1.5rem;
  font-weight: 600;
  color: #64748b;
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  text-align: left;
}

.table th:last-child {
  text-align: right;
}

.table th:nth-child(2) {
  text-align: right;
}

.table td {
  padding: 1rem 1.5rem;
  color: #0f172a;
  font-size: 0.875rem;
  border-bottom: 1px solid #f1f5f9;
}

.table td:nth-child(2) {
  text-align: right;
  font-weight: 500;
  color: #0f172a;
}

.table td:last-child {
  text-align: right;
}

.table tbody tr {
  transition: background-color 0.15s ease;
}

.table tbody tr:hover {
  background: #fafbfc;
}

.table tbody tr:last-child td {
  border-bottom: none;
}

.input-edit {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border: 1px solid #cbd5e1;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-family: inherit;
  transition: all 0.2s ease;
  background: white;
}

.input-edit:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.actions {
  display: flex;
  gap: 0.25rem;
  justify-content: flex-end;
}

.btn-icon {
  padding: 0.375rem;
  border: none;
  background: transparent;
  cursor: pointer;
  border-radius: 0.375rem;
  transition: all 0.15s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
}

.btn-icon:hover {
  color: #0f172a;
}

.btn-edit {
  color: #3b82f6;
}

.btn-edit:hover {
  background: #eff6ff;
  color: #2563eb;
}

.btn-delete {
  color: #ef4444;
}

.btn-delete:hover {
  background: #fef2f2;
  color: #dc2626;
}

.btn-save {
  color: #10b981;
}

.btn-save:hover {
  background: #f0fdf4;
  color: #059669;
}

.btn-cancel {
  color: #ef4444;
}

.btn-cancel:hover {
  background: #fef2f2;
  color: #dc2626;
}

.empty-state {
  padding: 4rem 2rem;
  text-align: center;
  color: #94a3b8;
  font-size: 0.9375rem;
}

.icon {
  width: 1.125rem;
  height: 1.125rem;
  flex-shrink: 0;
}

.icon-sm {
  width: 1rem;
  height: 1rem;
  flex-shrink: 0;
}

.error-row {
  background: #fef2f2 !important;
}

.error-row:hover {
  background: #fef2f2 !important;
}

.inline-error {
  color: #ef4444;
  padding: 0.75rem 1.5rem;
  font-size: 0.8125rem;
  font-weight: 500;
}
</style>
