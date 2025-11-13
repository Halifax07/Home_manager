<template>
  <div class="modern-sidebar-logo" :class="{'collapse':collapse}">
    <transition name="logoFade">
      <router-link v-if="collapse" key="collapse" class="logo-link" to="/">
        <div class="logo-icon">
          <i class="el-icon-s-finance"></i>
        </div>
      </router-link>
      <router-link v-else key="expand" class="logo-link" to="/">
        <div class="logo-icon">
          <i class="el-icon-s-finance"></i>
        </div>
        <div class="logo-content">
          <h1 class="logo-title">{{ title }}</h1>
          <p class="logo-subtitle">Financial Management</p>
        </div>
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'ModernSidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: '家庭财务管理'
    }
  }
}
</script>

<style lang="scss" scoped>
.logoFade-enter-active {
  transition: all 0.3s ease;
}

.logoFade-enter,
.logoFade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.modern-sidebar-logo {
  position: relative;
  width: 100%;
  height: 80px;
  background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(255, 152, 0, 0.3);
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
    pointer-events: none;
  }

  .logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 20px;
    text-decoration: none;
    transition: all 0.3s ease;
    
    &:hover {
      transform: scale(1.02);
      
      .logo-icon {
        transform: rotate(5deg) scale(1.1);
        background: linear-gradient(135deg, #ffffff 0%, #f0f0f0 100%);
      }
      
      .logo-title {
        color: #ffffff;
        text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
      }
    }
  }

  .logo-icon {
    width: 48px;
    height: 48px;
    background: linear-gradient(135deg, #ffffff 0%, #f5f5f5 100%);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    margin-right: 16px;
    
    i {
      font-size: 24px;
      color: #ff9800;
    }
  }

  .logo-content {
    flex: 1;
    
    .logo-title {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #ffffff;
      line-height: 1.2;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      transition: all 0.3s ease;
    }
    
    .logo-subtitle {
      margin: 2px 0 0 0;
      font-size: 11px;
      color: rgba(255, 255, 255, 0.8);
      font-weight: 400;
      letter-spacing: 0.5px;
      text-transform: uppercase;
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
  }

  &.collapse {
    .logo-link {
      justify-content: center;
      padding: 0;
    }
    
    .logo-icon {
      margin-right: 0;
      width: 40px;
      height: 40px;
      
      i {
        font-size: 20px;
      }
    }
    
    .logo-content {
      display: none;
    }
  }
}
</style>
