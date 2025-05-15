<template>
<!-- eslint-disable -->
  <div class="login-page">
    <!-- 顶部导航 -->
    <div class="login-header">
      <div class="header-content">
        <router-link to="/" class="logo-link">
          <div class="logo-icon">W</div>
          <span>鲸浪商城</span>
        </router-link>
        
        <div class="header-right">
          <a href="#" class="header-link">用户协议</a>
          <a href="#" class="header-link">隐私政策</a>
          <a href="#" class="header-link">帮助中心</a>
        </div>
      </div>
    </div>
    
    <!-- 主体内容区域 -->
    <div class="login-body">
      <div class="login-container">
        <!-- 左侧背景图 -->
        <div class="login-left">
          <img :src="loginBgImage" alt="登录背景" class="login-bg-image"/>
        </div>
        
        <!-- 右侧登录表单 -->
        <div class="login-right">
          <!-- 登录/注册 Tab 切换 -->
          <div class="tabs-container">
            <div 
              class="tab" 
              :class="{ 'active': activeTab === 'login' }"
              @click="activeTab = 'login'"
            >
              登录
            </div>
            <div 
              class="tab" 
              :class="{ 'active': activeTab === 'register' }"
              @click="activeTab = 'register'"
            >
              注册
            </div>
          </div>
          
          <!-- 登录表单 -->
          <div v-if="activeTab === 'login'" class="tab-content">
            <el-form 
              :model="loginForm" 
              :rules="loginRules" 
              ref="loginFormRef" 
              label-position="top"
              @submit.prevent="submitLoginForm"
              class="login-form">
              
              <!-- 用户名/邮箱/手机号 -->
              <el-form-item prop="account">
                <el-input 
                  v-model="loginForm.account"
                  placeholder="邮箱/手机号码/用户名"
                  prefix-icon="el-icon-user">
                </el-input>
              </el-form-item>
              
              <!-- 密码 -->
              <el-form-item prop="password">
                <el-input 
                  v-model="loginForm.password"
                  type="password"
                  placeholder="密码"
                  show-password
                  prefix-icon="el-icon-lock">
                </el-input>
              </el-form-item>
              
              <!-- 记住密码 -->
              <div class="login-options">
                <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
                <el-link type="primary" @click="gotoForgetPassword">忘记密码?</el-link>
              </div>
              
              <!-- 登录按钮 -->
              <el-button 
                type="primary" 
                @click="submitLoginForm"
                :loading="isSubmitting"
                class="submit-button">
                {{ isSubmitting ? '登录中...' : '登录' }}
              </el-button>
              
              <!-- 其他登录方式 -->
              <div class="other-login-options">
                <div class="helper-links">
                  <a href="#" class="helper-link">忘记密码?</a>
                  <a href="#" class="helper-link">手机号登录</a>
                </div>
                
                <div class="other-login-title">其他方式登录</div>
                <div class="other-login-methods">
                  <a href="#" class="other-login-icon"><i class="el-icon-s-custom"></i></a>
                  <a href="#" class="other-login-icon"><i class="el-icon-chat-dot-round"></i></a>
                  <a href="#" class="other-login-icon"><i class="el-icon-bell"></i></a>
                  <a href="#" class="other-login-icon"><i class="el-icon-s-promotion"></i></a>
                </div>
              </div>
            </el-form>
          </div>
          
          <!-- 注册表单 -->
          <div v-if="activeTab === 'register'" class="tab-content">
            <el-form 
              :model="registerForm" 
              :rules="registerRules" 
              ref="registerFormRef" 
              label-position="top"
              @submit.prevent="submitRegisterForm"
              class="register-form">
              
              <!-- 用户名/邮箱/手机号 -->
              <el-form-item prop="account">
                <el-input 
                  v-model="registerForm.account"
                  placeholder="邮箱/手机号码/用户名"
                  prefix-icon="el-icon-user">
                </el-input>
              </el-form-item>
              
              <!-- 密码 -->
              <el-form-item prop="password">
                <el-input 
                  v-model="registerForm.password"
                  type="password"
                  placeholder="密码"
                  show-password
                  prefix-icon="el-icon-lock">
                </el-input>
              </el-form-item>
              
              <!-- 用户协议 -->
              <div class="agreement-container">
                <el-checkbox v-model="registerForm.agreement">已阅读并同意鲸浪商城用户协议和隐私政策</el-checkbox>
              </div>
              
              <!-- 注册按钮 -->
              <el-button 
                type="primary" 
                @click="submitRegisterForm"
                :loading="isSubmitting"
                :disabled="!registerForm.agreement"
                class="submit-button">
                {{ isSubmitting ? '注册中...' : '注册' }}
              </el-button>
            </el-form>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 页脚 -->
    <div class="login-footer">
      <p>&copy; 2023 鲸浪商城 版权所有</p>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import api from '@/api'
// 导入背景图片
import loginBgImage from '@/assets/login-bg.jpg'

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const store = useStore()
    const loginFormRef = ref(null)
    const registerFormRef = ref(null)
    const isSubmitting = ref(false)
    const activeTab = ref('login')
    
    // 登录表单数据
    const loginForm = reactive({
      account: '',
      password: '',
      remember: false
    })
    
    // 注册表单数据
    const registerForm = reactive({
      account: '',
      password: '',
      agreement: false
    })
    
    // 登录表单规则
    const loginRules = {
      account: [
        { required: true, message: '请输入账号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
      ]
    }
    
    // 注册表单规则
    const registerRules = {
      account: [
        { required: true, message: '请输入账号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
      ]
    }
    
    // 提交登录表单
    const submitLoginForm = async () => {
      if (!loginFormRef.value) return
      
      try {
        // 表单验证
        await loginFormRef.value.validate()
        
        isSubmitting.value = true
        
        // 使用Vuex的login action
        await store.dispatch('login', {
          username: loginForm.account,
          password: loginForm.password
        })
        
        // 记住用户信息
        if (loginForm.remember) {
          localStorage.setItem('username', loginForm.account)
        } else {
          localStorage.removeItem('username')
        }
        
        ElMessage({
          message: '登录成功',
          type: 'success'
        })
        
        // 添加一个短暂延迟，确保store状态完全更新
        setTimeout(() => {
          // 根据重定向参数或默认跳转到首页
          const redirectUrl = route.query.redirect || '/'
          console.log('登录成功，即将重定向到:', redirectUrl)
          router.push(redirectUrl)
        }, 500) // 增加延迟确保状态更新
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error(error.message || '登录失败，请检查账号和密码')
      } finally {
        isSubmitting.value = false
      }
    }
    
    // 提交注册表单
    const submitRegisterForm = async () => {
      if (!registerFormRef.value) return
      
      try {
        // 表单验证
        await registerFormRef.value.validate()
        
        if (!registerForm.agreement) {
          ElMessage.warning('请阅读并同意用户协议和隐私政策')
          return
        }
        
        isSubmitting.value = true
        
        // 调用注册API
        const response = await api.user.register({
          username: registerForm.account,
          password: registerForm.password
        })
        
        // 注册成功处理
        if (response.code === 200) {
          ElMessage({
            message: '注册成功，正在自动登录...',
            type: 'success'
          })
          
          // 自动登录
          try {
            await store.dispatch('login', {
              username: registerForm.account,
              password: registerForm.password
            })
            
            // 添加短暂延迟确保状态更新
            setTimeout(() => {
              // 跳转到首页
              router.push('/')
            }, 300)
          } catch (loginError) {
            console.error('自动登录失败:', loginError)
            
            // 如果自动登录失败，切换到登录tab让用户手动登录
            activeTab.value = 'login'
            loginForm.account = registerForm.account
            loginForm.password = ''
            ElMessage.warning('自动登录失败，请手动登录')
          }
        } else {
          ElMessage.error(response.message || '注册失败，请稍后重试')
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请检查输入信息')
      } finally {
        isSubmitting.value = false
      }
    }
    
    // 跳转到忘记密码页面
    const gotoForgetPassword = () => {
      router.push('/forget-password')
    }
    
    return {
      loginBgImage,
      activeTab,
      loginForm,
      registerForm,
      loginRules,
      registerRules,
      isSubmitting,
      loginFormRef,
      registerFormRef,
      submitLoginForm,
      submitRegisterForm,
      gotoForgetPassword
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f9ff 0%, #e1f0ff 100%);
  position: relative;
  overflow: hidden;
}

/* 添加海洋波浪背景 */
.login-page::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 300px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z' fill='rgba(0, 120, 215, 0.1)'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  z-index: 0;
  opacity: 0.7;
}

/* 顶部导航样式 */
.login-header {
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  padding: 15px 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 2;
}

.header-content {
  max-width: 1226px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  font-size: 22px;
  font-weight: bold;
  color: white;
  transition: transform 0.3s;
}

.logo-link:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #4aa3f5, #0078d7);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 10px;
  box-shadow: 0 3px 8px rgba(0, 120, 215, 0.4);
  transition: transform 0.3s ease;
}

.logo-link:hover .logo-icon {
  transform: rotate(10deg);
}

.header-right {
  display: flex;
}

.header-link {
  margin-left: 20px;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
  position: relative;
}

.header-link:hover {
  color: #fff;
}

.header-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background-color: white;
  transition: width 0.3s;
}

.header-link:hover::after {
  width: 100%;
}

/* 主体内容样式 */
.login-body {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  position: relative;
  z-index: 1;
}

.login-container {
  display: flex;
  width: 920px;
  min-height: 520px;
  background-color: #fff;
  box-shadow: 0 5px 30px rgba(0, 120, 215, 0.15);
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.5s, box-shadow 0.5s;
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-container:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 40px rgba(0, 120, 215, 0.2);
}

.login-left {
  width: 45%;
  overflow: hidden;
  position: relative;
}

.login-left::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(0, 120, 215, 0.6), rgba(0, 94, 166, 0.8));
  z-index: 1;
}

.login-bg-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 10s ease;
}

.login-container:hover .login-bg-image {
  transform: scale(1.1);
}

.login-right {
  width: 55%;
  padding: 40px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.login-right::before {
  content: '';
  position: absolute;
  top: -150px;
  right: -150px;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(0, 120, 215, 0.1) 0%, rgba(255, 255, 255, 0) 70%);
  border-radius: 50%;
  z-index: 0;
}

/* 标签样式 */
.tabs-container {
  display: flex;
  margin-bottom: 30px;
  border-bottom: 1px solid #e0e0e0;
  position: relative;
  z-index: 1;
}

.tab {
  padding: 0 0 10px 0;
  margin-right: 30px;
  font-size: 20px;
  color: #999;
  cursor: pointer;
  position: relative;
  transition: all 0.3s;
}

.tab.active {
  color: var(--primary-color);
  font-weight: 500;
}

.tab.active::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -1px;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
  border-radius: 3px 3px 0 0;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    width: 0;
    left: 50%;
    transform: translateX(-50%);
  }
  to {
    width: 100%;
    left: 0;
    transform: translateX(0);
  }
}

.tab-content {
  padding-top: 20px;
  position: relative;
  z-index: 1;
  animation: fadeIn 0.4s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.login-form, .register-form {
  width: 100%;
}

.login-form :deep(.el-input__wrapper),
.register-form :deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
}

.login-form :deep(.el-input__wrapper:hover),
.register-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(0, 120, 215, 0.1);
}

.login-form :deep(.el-input__wrapper.is-focus),
.register-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset, 0 2px 12px rgba(0, 120, 215, 0.1);
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px 0;
}

.submit-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  border: none;
  border-radius: 8px;
  margin: 25px 0 15px;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0, 120, 215, 0.25);
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 120, 215, 0.35);
}

.submit-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 120, 215, 0.25);
}

.other-login-options {
  margin-top: 30px;
}

.helper-links {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;
}

.helper-link {
  color: var(--primary-color);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
}

.helper-link:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

.other-login-title {
  font-size: 14px;
  color: #999;
  text-align: center;
  margin-bottom: 15px;
  position: relative;
}

.other-login-title::before,
.other-login-title::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 30%;
  height: 1px;
  background-color: #e0e0e0;
}

.other-login-title::before {
  left: 0;
}

.other-login-title::after {
  right: 0;
}

.other-login-methods {
  display: flex;
  justify-content: center;
  align-items: center;
}

.other-login-icon {
  margin: 0 10px;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  color: #666;
  font-size: 20px;
  transition: all 0.3s;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.other-login-icon:hover {
  background-color: #e6f1fc;
  color: var(--primary-color);
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 5px 15px rgba(0, 120, 215, 0.2);
}

.agreement-container {
  margin: 20px 0;
  font-size: 14px;
}

.login-footer {
  padding: 15px 0;
  background: linear-gradient(90deg, var(--primary-dark), var(--primary-color));
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  position: relative;
  z-index: 2;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

/* 响应式布局 */
@media (max-width: 992px) {
  .login-container {
    width: 700px;
  }
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    width: 95%;
    max-width: 400px;
    height: auto;
  }
  
  .login-left {
    width: 100%;
    height: 180px;
  }
  
  .login-right {
    width: 100%;
    padding: 25px 20px;
  }
  
  .header-right {
    display: none;
  }
}
</style> 