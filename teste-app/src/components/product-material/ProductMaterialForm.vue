<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal">
      <div class="modal-header">
        <h3>Add Raw Material</h3>
        <button class="close-btn" @click="$emit('close')">&times;</button>
      </div>

      <form @submit.prevent="handleSubmit" class="modal-body">
        <div class="form-group">
          <label for="rawMaterialId">Raw Material *</label>
          <select
            id="rawMaterialId"
            v-model.number="form.rawMaterialId"
            class="form-control"
            required
          >
            <option value="" disabled>Select a raw material</option>
            <option v-for="rm in rawMaterials" :key="rm.id" :value="rm.id">
              {{ rm.name }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="quantityNeeded">Quantity Needed *</label>
          <input
            id="quantityNeeded"
            v-model.number="form.quantityNeeded"
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
          <button type="button" class="btn btn-secondary" @click="$emit('close')">
            Cancel
          </button>
          <button type="submit" class="btn btn-primary" :disabled="loading">
            {{ loading ? 'Saving...' : 'Save' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { ProductRawMaterial } from '@/services/productRawMaterialService'
import type { RawMaterial } from '@/services/rawMaterialService'

const props = defineProps<{
  rawMaterials: RawMaterial[]
  loading: boolean
  error: string | null
}>()

const emit = defineEmits<{
  close: []
  save: [association: ProductRawMaterial]
}>()

const form = ref<ProductRawMaterial>({
  rawMaterialId: 0,
  quantityNeeded: 0
})

const errorMessage = ref<string | null>(null)

watch(() => props.error, (newError) => {
  errorMessage.value = newError
})

function handleSubmit() {
  errorMessage.value = null

  if (!form.value.rawMaterialId) {
    errorMessage.value = 'Please select a raw material'
    return
  }

  if (form.value.quantityNeeded <= 0) {
    errorMessage.value = 'Quantity must be greater than zero'
    return
  }

  emit('save', form.value)
}
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
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s;
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
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #e9ecef;
}

.modal-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.5rem;
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

select.form-control {
  cursor: pointer;
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

.btn-secondary {
  background: #e9ecef;
  color: #2c3e50;
}

.btn-secondary:hover {
  background: #dfe6e9;
}
</style>
