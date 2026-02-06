<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import { CubeIcon, CircleStackIcon, ChartBarIcon, Bars3Icon, XMarkIcon } from '@heroicons/vue/24/outline'

const mobileMenuOpen = ref(false)

function toggleMobileMenu() {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

function closeMobileMenu() {
  mobileMenuOpen.value = false
}
</script>

<template>
  <div class="layout">
    <!-- Mobile Header -->
    <header class="mobile-header">
      <div class="logo">
        <CubeIcon class="logo-icon" />
        <span class="logo-text">Inventory</span>
      </div>
      <button class="menu-toggle" @click="toggleMobileMenu">
        <Bars3Icon v-if="!mobileMenuOpen" class="menu-icon" />
        <XMarkIcon v-else class="menu-icon" />
      </button>
    </header>

    <!-- Sidebar (Desktop) / Mobile Menu -->
    <aside class="sidebar" :class="{ 'mobile-open': mobileMenuOpen }">
      <div class="sidebar-header">
        <div class="logo">
          <CubeIcon class="logo-icon" />
          <span class="logo-text">Inventory</span>
        </div>
      </div>
      <nav class="sidebar-nav">
        <RouterLink to="/" class="nav-item" @click="closeMobileMenu">
          <CubeIcon class="nav-icon" />
          <span>Products</span>
        </RouterLink>
        <RouterLink to="/raw-materials" class="nav-item" @click="closeMobileMenu">
          <CircleStackIcon class="nav-icon" />
          <span>Raw Materials</span>
        </RouterLink>
        <RouterLink to="/production-suggestions" class="nav-item" @click="closeMobileMenu">
          <ChartBarIcon class="nav-icon" />
          <span>Production Suggestions</span>
        </RouterLink>
      </nav>
    </aside>

    <!-- Backdrop for mobile menu -->
    <div v-if="mobileMenuOpen" class="backdrop" @click="closeMobileMenu"></div>

    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  height: 100%;
}

body {
  height: 100%;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  overflow: hidden;
}

#app {
  height: 100%;
}

.layout {
  display: grid;
  grid-template-columns: 220px 1fr;
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
  background: #f8fafc;
}

.sidebar {
  background: #ffffff;
  border-right: 1px solid #e2e8f0;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar-header {
  padding: 1.75rem 1.25rem;
  border-bottom: 1px solid #e2e8f0;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.logo-icon {
  width: 1.75rem;
  height: 1.75rem;
  color: #3b82f6;
}

.logo-text {
  font-size: 1.125rem;
  font-weight: 600;
  color: #0f172a;
  letter-spacing: -0.01em;
}

.sidebar-nav {
  padding: 1.5rem 0.75rem;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.625rem 0.875rem;
  color: #64748b;
  text-decoration: none;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.15s ease;
  margin-bottom: 0.25rem;
}

.nav-icon {
  width: 1.25rem;
  height: 1.25rem;
  flex-shrink: 0;
}

.nav-item:hover {
  background: #f1f5f9;
  color: #334155;
}

.nav-item.router-link-active {
  background: #eff6ff;
  color: #3b82f6;
}

.nav-item.router-link-active .nav-icon {
  color: #3b82f6;
}

.main-content {
  background: #f8fafc;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* Mobile Header - Hidden on desktop */
.mobile-header {
  display: none;
}

/* Backdrop for mobile menu */
.backdrop {
  display: none;
}

/* Responsive Design */
@media (max-width: 768px) {
  .layout {
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr;
  }

  /* Show mobile header */
  .mobile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem 1.25rem;
    background: #ffffff;
    border-bottom: 1px solid #e2e8f0;
    position: sticky;
    top: 0;
    z-index: 100;
  }

  .menu-toggle {
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 0.5rem;
    color: #64748b;
    border-radius: 0.5rem;
    transition: all 0.15s ease;
  }

  .menu-toggle:hover {
    background: #f1f5f9;
    color: #334155;
  }

  .menu-icon {
    width: 1.5rem;
    height: 1.5rem;
  }

  /* Transform sidebar into mobile menu */
  .sidebar {
    position: fixed;
    top: 0;
    left: -220px;
    width: 220px;
    height: 100vh;
    z-index: 200;
    transition: left 0.3s ease;
  }

  .sidebar.mobile-open {
    left: 0;
  }

  .sidebar-header {
    display: block;
  }

  /* Show backdrop when menu is open */
  .backdrop {
    display: block;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 150;
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

  .main-content {
    grid-column: 1;
    grid-row: 2;
  }

  body {
    overflow: auto;
  }
}

@media (max-width: 480px) {
  .logo-text {
    font-size: 1rem;
  }

  .sidebar {
    width: 200px;
    left: -200px;
  }

  .sidebar.mobile-open {
    left: 0;
  }
}
</style>
