<template>
  <div class="modal-overlay" @click.self="handleClose">
    <div class="modal" :class="{ 'modal-large': currentStep === 2 }">
      <div class="modal-header">
        <div>
          <h3>{{ isEdit ? 'Edit Product' : 'New Product' }}</h3>
          <div class="step-indicator" v-if="!isEdit && currentStep === 2">
            <span class="step">1. Basic Info ✓</span>
            <span class="step-separator">→</span>
            <span class="step active">2. Link Materials (Optional)</span>
          </div>
        </div>
        <button class="close-btn" @click="handleClose">&times;</button>
      </div>

      <!-- Step 1: Product Basic Info -->
      <form v-if="currentStep === 1" @submit.prevent="handleStep1Submit" class="modal-body">
        <div class="form-group">
          <label for="name">Name *</label>
          <input
            id="name"
            v-model="form.name"
            type="text"
            class="form-control"
            placeholder="Enter product name"
            required
          />
        </div>

        <div class="form-group">
          <label for="value">Value (R$) *</label>
          <input
            id="value"
            v-model.number="form.value"
            type="number"
            step="0.01"
            min="0.01"
            class="form-control"
            placeholder="0.00"
            required
          />
        </div>

        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="handleClose">
            Cancel
          </button>
          <button type="submit" class="btn btn-success" :disabled="loading">
            {{ loading ? 'Saving...' : (isEdit ? 'Save' : 'Finish') }}
          </button>
          <button
            v-if="!isEdit"
            type="button"
            class="btn btn-primary"
            :disabled="loading"
            @click="handleLinkMaterials"
          >
            Link Materials →
          </button>
        </div>
      </form>

      <!-- Step 2: Link Materials -->
      <div v-if="currentStep === 2" class="step2-container">
        <div class="step2-content">
          <p class="step2-info">Link raw materials to: <strong>{{ form.name }}</strong></p>

          <ProductMaterialList
            :associations="productRawMaterialStore.associations"
            :loading="productRawMaterialStore.loading"
            :error="productRawMaterialStore.error"
            :current-page="productRawMaterialStore.currentPage"
            :page-size="productRawMaterialStore.pageSize"
            :total-pages="productRawMaterialStore.totalPages"
            :total-elements="productRawMaterialStore.totalElements"
            @create="openMaterialModal"
            @update="handleUpdateMaterial"
            @delete="handleDeleteMaterial"
            @page-change="handlePageChange"
            @page-size-change="handlePageSizeChange"
          />

          <ProductMaterialForm
            v-if="showMaterialModal"
            :raw-materials="allRawMaterials"
            :loading="productRawMaterialStore.loading"
            :error="productRawMaterialStore.error"
            @close="closeMaterialModal"
            @save="handleCreateMaterial"
          />

          <ConfirmDialog
            v-if="showConfirmDelete"
            title="Delete Material Link"
            message="Are you sure you want to remove this material from the product?"
            confirm-text="Delete"
            cancel-text="Cancel"
            @confirm="confirmDeleteMaterial"
            @cancel="cancelDeleteMaterial"
          />

          <ConfirmDialog
            v-if="showConfirmClose"
            title="Finish Linking Materials?"
            message="Do you want to finish linking materials and close this wizard?"
            confirm-text="Yes, Finish"
            cancel-text="Continue Linking"
            @confirm="confirmClose"
            @cancel="cancelClose"
          />
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-success" @click="handleFinishWizard">
            Finish
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import type { Product } from '@/services/productService'
import ProductMaterialList from '@/components/product-material/ProductMaterialList.vue'
import ProductMaterialForm from '@/components/product-material/ProductMaterialForm.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import type { ProductRawMaterial } from '@/services/productRawMaterialService'
import { useProductRawMaterialStore } from '@/stores/productRawMaterialStore'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'

const props = defineProps<{
  product?: Product | null
  loading: boolean
  error: string | null
}>()

const emit = defineEmits<{
  close: []
  save: [product: Product]
  saveAndLinkMaterials: [product: Product]
}>()

const form = ref<Product>({
  name: '',
  value: 0
})

const errorMessage = ref<string | null>(null)
const isEdit = ref(false)
const currentStep = ref(1)
const createdProductId = ref<number | null>(null)
const productRawMaterialStore = useProductRawMaterialStore()
const rawMaterialStore = useRawMaterialStore()
const allRawMaterials = ref<any[]>([])
const showMaterialModal = ref(false)
const showConfirmDelete = ref(false)
const showConfirmClose = ref(false)
const materialToDelete = ref<number | null>(null)

watch(() => props.product, (newProduct) => {
  if (newProduct) {
    form.value = { ...newProduct }
    isEdit.value = true
    currentStep.value = 1
  } else {
    form.value = { name: '', value: 0 }
    isEdit.value = false
    currentStep.value = 1
    createdProductId.value = null
  }
}, { immediate: true })

watch(() => props.error, (newError) => {
  errorMessage.value = newError
})

function validateForm(): boolean {
  errorMessage.value = null

  if (!form.value.name.trim()) {
    errorMessage.value = 'Name is required'
    return false
  }

  if (form.value.value <= 0) {
    errorMessage.value = 'Value must be greater than zero'
    return false
  }

  return true
}

function handleStep1Submit() {
  if (!validateForm()) return
  emit('save', form.value)
}

async function handleLinkMaterials() {
  if (!validateForm()) return
  emit('saveAndLinkMaterials', form.value)
}

function handleFinishWizard() {
  emit('close')
}

function handleClose() {
  if (currentStep.value === 2) {
    showConfirmClose.value = true
  } else {
    emit('close')
  }
}

function confirmClose() {
  showConfirmClose.value = false
  emit('close')
}

function cancelClose() {
  showConfirmClose.value = false
}

function openMaterialModal() {
  showMaterialModal.value = true
}

function closeMaterialModal() {
  showMaterialModal.value = false
}

async function handleCreateMaterial(association: ProductRawMaterial) {
  if (!createdProductId.value) return
  try {
    await productRawMaterialStore.createAssociation(createdProductId.value, association)
    closeMaterialModal()
  } catch (error) {
    // Error handled in store
  }
}

async function handleUpdateMaterial(id: number, association: ProductRawMaterial) {
  if (!createdProductId.value) return
  try {
    await productRawMaterialStore.updateAssociation(createdProductId.value, id, association)
  } catch (error) {
    // Error handled in store
  }
}

function handleDeleteMaterial(id: number) {
  materialToDelete.value = id
  showConfirmDelete.value = true
}

async function confirmDeleteMaterial() {
  if (!createdProductId.value || materialToDelete.value === null) return
  try {
    await productRawMaterialStore.deleteAssociation(createdProductId.value, materialToDelete.value)
    showConfirmDelete.value = false
    materialToDelete.value = null
  } catch (error) {
    // Error handled in store
  }
}

function cancelDeleteMaterial() {
  showConfirmDelete.value = false
  materialToDelete.value = null
}

async function handlePageChange(page: number) {
  if (!createdProductId.value) return
  await productRawMaterialStore.fetchByProductId(createdProductId.value, page, productRawMaterialStore.pageSize)
}

async function handlePageSizeChange(size: number) {
  if (!createdProductId.value) return
  await productRawMaterialStore.fetchByProductId(createdProductId.value, 0, size)
}

// Expor função para avançar para step 2 (será chamado pelo parent)
async function goToStep2(productId: number) {
  createdProductId.value = productId
  currentStep.value = 2

  // Carregar materiais disponíveis e associações do produto
  allRawMaterials.value = await rawMaterialStore.fetchAllRawMaterialsForDropdown()
  await productRawMaterialStore.fetchByProductId(productId)
}

defineExpose({ goToStep2 })
</script>

<style scoped>
.modal-overlay {
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
  animation: fadeIn 0.2s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s;
  transition: max-width 0.3s ease;
}

.modal-large {
  max-width: 900px;
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1.5rem;
  border-bottom: 1px solid #e9ecef;
}

.modal-header h3 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.5rem;
}

.step-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  margin-top: 0.5rem;
}

.step {
  color: #95a5a6;
  font-weight: 500;
}

.step.active {
  color: #3498db;
  font-weight: 600;
}

.step-separator {
  color: #bdc3c7;
}

.close-btn {
  background: none;
  border: none;
  font-size: 2rem;
  color: #7f8c8d;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #f8f9fa;
  color: #2c3e50;
}

.modal-body {
  padding: 1.5rem;
  overflow-y: auto;
}

.step2-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
}

.step2-content {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem;
}

.step2-info {
  margin-bottom: 1rem;
  padding: 0.75rem;
  background: #e3f2fd;
  border-radius: 8px;
  color: #1976d2;
  font-size: 0.9rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #2c3e50;
}

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #dfe6e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s;
}

.form-control:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.error-message {
  background: #fee;
  color: #e74c3c;
  padding: 0.75rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-size: 0.875rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  padding: 1.5rem;
  border-top: 1px solid #e9ecef;
}

.btn {
  padding: 0.625rem 1.25rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: #3498db;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #2980b9;
}

.btn-success {
  background: #27ae60;
  color: white;
}

.btn-success:hover:not(:disabled) {
  background: #229954;
}

.btn-secondary {
  background: #e9ecef;
  color: #2c3e50;
}

.btn-secondary:hover {
  background: #dfe6e9;
}

/* Responsive Design */
@media (max-width: 768px) {
  .modal {
    width: 95%;
    max-width: none;
    margin: 1rem;
  }

  .modal-large {
    max-width: none;
    width: 95%;
  }

  .modal-header h3 {
    font-size: 1.25rem;
  }

  .step-indicator {
    font-size: 0.75rem;
  }
}

@media (max-width: 640px) {
  .modal-overlay {
    align-items: flex-start;
    padding: 0;
  }

  .modal {
    width: 100%;
    max-width: 100%;
    max-height: 100%;
    margin: 0;
    border-radius: 0;
  }

  .modal-large {
    width: 100%;
    max-width: 100%;
  }

  .modal-header {
    padding: 1rem;
  }

  .modal-header h3 {
    font-size: 1.125rem;
  }

  .modal-body {
    padding: 1rem;
  }

  .step2-content {
    padding: 1rem;
  }

  .modal-footer {
    padding: 1rem;
    flex-direction: column-reverse;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }

  .step-indicator {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.25rem;
  }

  .step-separator {
    display: none;
  }
}
</style>
