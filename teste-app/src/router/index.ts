import { createRouter, createWebHistory } from 'vue-router'
import ProductsView from '../views/ProductsView.vue'
import RawMaterialsView from '../views/RawMaterialsView.vue'
import ProductionSuggestionsView from '../views/ProductionSuggestionsView.vue'
import ProductMaterialsView from '../views/ProductMaterialsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'products',
      component: ProductsView,
    },
    {
      path: '/raw-materials',
      name: 'raw-materials',
      component: RawMaterialsView,
    },
    {
      path: '/production-suggestions',
      name: 'production-suggestions',
      component: ProductionSuggestionsView,
    },
    {
      path: '/products/:id/materials',
      name: 'product-materials',
      component: ProductMaterialsView,
    },
  ],
})

export default router
