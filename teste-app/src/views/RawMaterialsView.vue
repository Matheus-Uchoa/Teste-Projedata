<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'
import RawMaterialList from '@/components/raw-material/RawMaterialList.vue'
import RawMaterialForm from '@/components/raw-material/RawMaterialForm.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import type { RawMaterial } from '@/services/rawMaterialService'

const rawMaterialStore = useRawMaterialStore()
const showModal = ref(false)
const showConfirmDelete = ref(false)
const materialToDelete = ref<number | null>(null)
const deleteError = ref<string | null>(null)

onMounted(() => {
  rawMaterialStore.fetchRawMaterials()
})

function openCreateModal() {
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

async function handleCreate(rawMaterial: RawMaterial) {
  try {
    await rawMaterialStore.createRawMaterial(rawMaterial)
    closeModal()
  } catch (error) {
    // Error is handled in the store
  }
}

async function handleUpdate(id: number, rawMaterial: RawMaterial) {
  try {
    await rawMaterialStore.updateRawMaterial(id, rawMaterial)
  } catch (error) {
    // Error is handled in the store
  }
}

function handleDelete(id: number) {
  materialToDelete.value = id
  deleteError.value = null
  showConfirmDelete.value = true
}

async function confirmDelete() {
  if (materialToDelete.value !== null) {
    deleteError.value = null
    try {
      await rawMaterialStore.deleteRawMaterial(materialToDelete.value)
      showConfirmDelete.value = false
      materialToDelete.value = null
    } catch (error) {
      deleteError.value = rawMaterialStore.error
    }
  }
}

function cancelDelete() {
  showConfirmDelete.value = false
  materialToDelete.value = null
  deleteError.value = null
}

async function handlePageChange(page: number) {
  await rawMaterialStore.fetchRawMaterials(page, rawMaterialStore.pageSize)
}

async function handlePageSizeChange(size: number) {
  await rawMaterialStore.fetchRawMaterials(0, size)
}

async function handleSearch(search: string, sortBy: string, sortDirection: string) {
  await rawMaterialStore.fetchRawMaterials(0, rawMaterialStore.pageSize, search, sortBy, sortDirection)
}
</script>

<template>
  <div class="raw-materials-view">
    <RawMaterialList
      :raw-materials="rawMaterialStore.rawMaterials"
      :loading="rawMaterialStore.loading"
      :error="rawMaterialStore.error"
      :current-page="rawMaterialStore.currentPage"
      :page-size="rawMaterialStore.pageSize"
      :total-pages="rawMaterialStore.totalPages"
      :total-elements="rawMaterialStore.totalElements"
      :search-term="rawMaterialStore.searchTerm"
      :sort-by="rawMaterialStore.sortBy"
      :sort-direction="rawMaterialStore.sortDirection"
      @create="openCreateModal"
      @update="handleUpdate"
      @delete="handleDelete"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
      @search="handleSearch"
    />

    <RawMaterialForm
      v-if="showModal"
      :loading="rawMaterialStore.loading"
      :error="rawMaterialStore.error"
      @close="closeModal"
      @save="handleCreate"
    />

    <ConfirmDialog
      v-if="showConfirmDelete"
      title="Delete Raw Material"
      message="Are you sure you want to delete this raw material? This action cannot be undone."
      confirm-text="Delete"
      cancel-text="Cancel"
      :error="deleteError"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
    />
  </div>
</template>

<style scoped>
.raw-materials-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
