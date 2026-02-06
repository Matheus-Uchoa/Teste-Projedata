<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useProductStore } from '@/stores/productStore'
import { useProductRawMaterialStore } from '@/stores/productRawMaterialStore'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'
import ProductMaterialList from '@/components/product-material/ProductMaterialList.vue'
import ProductMaterialForm from '@/components/product-material/ProductMaterialForm.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import type { ProductRawMaterial } from '@/services/productRawMaterialService'

const route = useRoute()
const router = useRouter()
const productStore = useProductStore()
const productRawMaterialStore = useProductRawMaterialStore()
const rawMaterialStore = useRawMaterialStore()

const productId = computed(() => Number(route.params.id))
const product = computed(() => productStore.products.find(p => p.id === productId.value))
const showModal = ref(false)
const showConfirmDelete = ref(false)
const materialToDelete = ref<number | null>(null)

const allRawMaterials = ref<any[]>([])

onMounted(async () => {
  if (productStore.products.length === 0) {
    await productStore.fetchProducts()
  }
  allRawMaterials.value = await rawMaterialStore.fetchAllRawMaterialsForDropdown()
  await productRawMaterialStore.fetchByProductId(productId.value)
})

function goBack() {
  router.push('/')
}

function openCreateModal() {
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

async function handleCreate(association: ProductRawMaterial) {
  try {
    await productRawMaterialStore.createAssociation(productId.value, association)
    closeModal()
  } catch (error) {
    // Error is handled in the store
  }
}

async function handleUpdate(id: number, association: ProductRawMaterial) {
  try {
    await productRawMaterialStore.updateAssociation(productId.value, id, association)
  } catch (error) {
    // Error is handled in the store
  }
}

function handleDelete(id: number) {
  materialToDelete.value = id
  showConfirmDelete.value = true
}

async function confirmDelete() {
  if (materialToDelete.value !== null) {
    try {
      await productRawMaterialStore.deleteAssociation(productId.value, materialToDelete.value)
      showConfirmDelete.value = false
      materialToDelete.value = null
    } catch (error) {
      // Error is handled in the store
    }
  }
}

function cancelDelete() {
  showConfirmDelete.value = false
  materialToDelete.value = null
}

async function handlePageChange(page: number) {
  await productRawMaterialStore.fetchByProductId(productId.value, page, productRawMaterialStore.pageSize)
}

async function handlePageSizeChange(size: number) {
  await productRawMaterialStore.fetchByProductId(productId.value, 0, size)
}
</script>

<template>
  <div class="product-materials-view">
    <div class="header">
      <button class="btn-back" @click="goBack" title="Back to Products">
        ← Back
      </button>
      <div class="product-info">
        <h1>Manage Materials</h1>
        <p v-if="product">{{ product.name }}</p>
      </div>
    </div>

    <ProductMaterialList
      :associations="productRawMaterialStore.associations"
      :loading="productRawMaterialStore.loading"
      :error="productRawMaterialStore.error"
      :current-page="productRawMaterialStore.currentPage"
      :page-size="productRawMaterialStore.pageSize"
      :total-pages="productRawMaterialStore.totalPages"
      :total-elements="productRawMaterialStore.totalElements"
      @create="openCreateModal"
      @update="handleUpdate"
      @delete="handleDelete"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
    />

    <ProductMaterialForm
      v-if="showModal"
      :raw-materials="allRawMaterials"
      :loading="productRawMaterialStore.loading"
      :error="productRawMaterialStore.error"
      @close="closeModal"
      @save="handleCreate"
    />

    <ConfirmDialog
      v-if="showConfirmDelete"
      title="Remove Raw Material"
      message="Are you sure you want to remove this raw material from the product? This action cannot be undone."
      confirm-text="Remove"
      cancel-text="Cancel"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
    />
  </div>
</template>

<style scoped>
.product-materials-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 2rem;
  flex-shrink: 0;
}

.btn-back {
  padding: 0.5rem 1rem;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  color: #64748b;
  transition: all 0.2s ease;
  font-family: inherit;
}

.btn-back:hover {
  background: #f8fafc;
  color: #0f172a;
  border-color: #cbd5e1;
}

.product-info h1 {
  font-size: 1.75rem;
  font-weight: 600;
  color: #0f172a;
  margin: 0 0 0.25rem 0;
  letter-spacing: -0.02em;
}

.product-info p {
  color: #64748b;
  font-size: 0.9375rem;
  margin: 0;
}
</style>
