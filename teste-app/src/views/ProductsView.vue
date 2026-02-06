<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useProductStore } from '@/stores/productStore'
import ProductList from '@/components/product/ProductList.vue'
import ProductFormWizard from '@/components/product/ProductFormWizard.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import type { Product } from '@/services/productService'

const router = useRouter()
const productStore = useProductStore()
const showModal = ref(false)
const showConfirmDelete = ref(false)
const productToDelete = ref<number | null>(null)
const deleteError = ref<string | null>(null)
const wizardRef = ref<InstanceType<typeof ProductFormWizard> | null>(null)

onMounted(() => {
  productStore.fetchProducts()
})

function openCreateModal() {
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

async function handleCreate(product: Product) {
  try {
    await productStore.createProduct(product)
    closeModal()
  } catch (error) {
    // Error is handled in the store
  }
}

async function handleCreateAndLinkMaterials(product: Product) {
  try {
    const createdProduct = await productStore.createProduct(product)
    if (createdProduct && createdProduct.id && wizardRef.value) {
      wizardRef.value.goToStep2(createdProduct.id)
    }
  } catch (error) {
    // Error is handled in the store
  }
}

async function handleUpdate(id: number, product: Product) {
  try {
    await productStore.updateProduct(id, product)
  } catch (error) {
    // Error is handled in the store
  }
}

function handleDelete(id: number) {
  productToDelete.value = id
  deleteError.value = null
  showConfirmDelete.value = true
}

async function confirmDelete() {
  if (productToDelete.value !== null) {
    deleteError.value = null
    try {
      await productStore.deleteProduct(productToDelete.value)
      showConfirmDelete.value = false
      productToDelete.value = null
    } catch (error) {
      deleteError.value = productStore.error
    }
  }
}

function cancelDelete() {
  showConfirmDelete.value = false
  productToDelete.value = null
  deleteError.value = null
}

function handleManageMaterials(id: number) {
  router.push(`/products/${id}/materials`)
}

async function handlePageChange(page: number) {
  await productStore.fetchProducts(page, productStore.pageSize)
}

async function handlePageSizeChange(size: number) {
  await productStore.fetchProducts(0, size)
}

async function handleSearch(search: string, sortBy: string, sortDirection: string) {
  await productStore.fetchProducts(0, productStore.pageSize, search, sortBy, sortDirection)
}
</script>

<template>
  <div class="products-view">
    <ProductList
      :products="productStore.products"
      :loading="productStore.loading"
      :error="productStore.error"
      :current-page="productStore.currentPage"
      :page-size="productStore.pageSize"
      :total-pages="productStore.totalPages"
      :total-elements="productStore.totalElements"
      :search-term="productStore.searchTerm"
      :sort-by="productStore.sortBy"
      :sort-direction="productStore.sortDirection"
      @create="openCreateModal"
      @update="handleUpdate"
      @delete="handleDelete"
      @manage-materials="handleManageMaterials"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
      @search="handleSearch"
    />

    <ProductFormWizard
      v-if="showModal"
      ref="wizardRef"
      :loading="productStore.loading"
      :error="productStore.error"
      @close="closeModal"
      @save="handleCreate"
      @save-and-link-materials="handleCreateAndLinkMaterials"
    />

    <ConfirmDialog
      v-if="showConfirmDelete"
      title="Delete Product"
      message="Are you sure you want to delete this product? This action cannot be undone."
      confirm-text="Delete"
      cancel-text="Cancel"
      :error="deleteError"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
    />
  </div>
</template>

<style scoped>
.products-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
