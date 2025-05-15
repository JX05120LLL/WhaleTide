<template>
<!-- eslint-disable -->
  <div class="register-page">
    <app-header></app-header>
    
    <div class="container">
      <div class="register-container">
        <div class="logo">
          <h1>WhaleTide</h1>
          <p>欢迎加入我们的平台</p>
        </div>
        
        <el-form 
          :model="formData" 
          :rules="rules" 
          ref="registerFormRef" 
          label-position="top"
          @submit.prevent="submitForm">
          
          <!-- 用户名 -->
          <el-form-item label="用户名" prop="username">
            <el-input 
              v-model="formData.username"
              placeholder="4-20个字符">
            </el-input>
          </el-form-item>
          
          <!-- 密码 -->
          <el-form-item label="密码" prop="password">
            <el-input 
              v-model="formData.password"
              type="password"
              placeholder="请输入至少6位密码">
            </el-input>
          </el-form-item>
          
          <!-- 确认密码 -->
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input 
              v-model="formData.confirmPassword"
              type="password"
              placeholder="请再次输入密码">
            </el-input>
          </el-form-item>
          
          <!-- 手机号 (可选) -->
          <el-form-item label="手机号 (可选)" prop="mobile">
            <el-input 
              v-model="formData.mobile"
              placeholder="请输入11位手机号">
            </el-input>
          </el-form-item>
          
          <!-- 邮箱 (可选) -->
          <el-form-item label="邮箱 (可选)" prop="email">
            <el-input 
              v-model="formData.email"
              placeholder="请输入有效邮箱">
            </el-input>
          </el-form-item>
          
          <!-- 协议同意 -->
          <el-form-item prop="agreeTerms">
            <el-checkbox v-model="formData.agreeTerms">
              <span class="policy-text">
                我已阅读并同意 
                <el-link type="primary" @click.prevent="showTerms">《用户协议》</el-link> 和 
                <el-link type="primary" @click.prevent="showPrivacy">《隐私政策》</el-link>
              </span>
            </el-checkbox>
          </el-form-item>
          
          <!-- 注册按钮 -->
          <el-form-item>
            <el-button 
              type="primary" 
              @click="submitForm"
              :loading="isSubmitting"
              class="register-button">
              {{ isSubmitting ? '注册中...' : '立即注册' }}
            </el-button>
          </el-form-item>
          
          <!-- 登录链接 -->
          <div class="text-center mt-3">
            已有账号？<el-link type="primary" @click="gotoLogin">立即登录</el-link>
          </div>
        </el-form>
      </div>
    </div>
    
    <app-footer></app-footer>
    
    <!-- 用户协议对话框 -->
    <el-dialog
      title="用户协议"
      v-model="termsVisible"
      width="50%">
      <div class="terms-content">
        <h3>鲸浪商城用户服务协议</h3>
        <p>欢迎您使用鲸浪商城服务！</p>
        <p>为使用鲸浪商城服务，您应当阅读并遵守《鲸浪商城用户服务协议》（以下简称"本协议"）。本协议是用户（您）与鲸浪商城之间关于用户使用鲸浪商城服务所订立的协议。</p>
        <p>...</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="termsVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 隐私政策对话框 -->
    <el-dialog
      title="隐私政策"
      v-model="privacyVisible"
      width="50%">
      <div class="policy-content">
        <h3>鲸浪商城隐私政策</h3>
        <p>鲸浪商城非常重视用户的隐私和个人信息保护。为了更好地保障您的个人权益，在您使用我们的服务前，请您仔细阅读我们的隐私政策。</p>
        <p>...</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="privacyVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/Header.vue'
import AppFooter from '@/components/Footer.vue'
import api from '@/api'

export default {
  name: 'RegisterPage',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
    const router = useRouter()
    const registerFormRef = ref(null)
    const isSubmitting = ref(false)
    const termsVisible = ref(false)
    const privacyVisible = ref(false)
    
    // 表单数据
    const formData = reactive({
      username: '',
      password: '',
      confirmPassword: '',
      mobile: '',
      email: '',
      agreeTerms: false
    })
    
    // 表单校验规则
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 4, max: 20, message: '用户名长度需在4-20个字符之间', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度至少6位', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        { 
          validator: (rule, value, callback) => {
            if (value !== formData.password) {
              callback(new Error('两次密码输入不一致'));
            } else {
              callback();
            }
          }, 
          trigger: 'blur' 
        }
      ],
      mobile: [
        { 
          pattern: /^1[3-9]\d{9}$/, 
          message: '手机号格式不正确', 
          trigger: 'blur' 
        }
      ],
      email: [
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
      ],
      agreeTerms: [
        { 
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error('请阅读并同意用户协议和隐私政策'));
            } else {
              callback();
            }
          }, 
          trigger: 'change' 
        }
      ]
    }
    
    // 显示用户协议
    const showTerms = () => {
      termsVisible.value = true
    }
    
    // 显示隐私政策
    const showPrivacy = () => {
      privacyVisible.value = true
    }
    
    // 跳转到登录页
    const gotoLogin = () => {
      router.push('/login')
    }
    
    // 提交表单
    const submitForm = () => {
      if (!registerFormRef.value) {
        ElMessage.warning('表单引用错误，请稍后重试')
        return
      }
      
      registerFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            isSubmitting.value = true
            
            // 调用注册API
            const response = await api.user.register({
                username: formData.username,
                password: formData.password,
              phone: formData.mobile || undefined,
                email: formData.email || undefined
            })
            
            if (response.code === 200) {
              ElMessage({
                type: 'success',
                message: '注册成功，请登录'
              })
              
              // 注册成功后跳转到登录页
              router.push('/login')
            } else {
              ElMessage.error(response.message || '注册失败，请稍后重试')
            }
              
              isSubmitting.value = false
          } catch (error) {
            console.error('注册失败:', error)
            ElMessage.error(error.message || '注册失败，请稍后重试')
            isSubmitting.value = false
          }
        } else {
          ElMessage.warning('请正确填写注册信息')
        }
      })
    }
    
    return {
      registerFormRef,
      formData,
      rules,
      isSubmitting,
      termsVisible,
      privacyVisible,
      showTerms,
      showPrivacy,
      gotoLogin,
      submitForm
    }
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f9ff 0%, #e1f0ff 100%);
  position: relative;
  overflow: hidden;
}

/* 添加海洋波浪背景 */
.register-page::before {
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

.container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.register-container {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 5px 30px rgba(0, 120, 215, 0.15);
  padding: 40px;
  width: 580px;
  position: relative;
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

.register-container:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 40px rgba(0, 120, 215, 0.2);
}

/* 添加装饰元素 */
.register-container::before {
  content: '';
  position: absolute;
  top: -100px;
  right: -100px;
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(0, 120, 215, 0.1) 0%, rgba(255, 255, 255, 0) 70%);
  border-radius: 50%;
  z-index: 0;
}

.register-container::after {
  content: '';
  position: absolute;
  bottom: -80px;
  left: -80px;
  width: 160px;
  height: 160px;
  background: radial-gradient(circle, rgba(0, 120, 215, 0.08) 0%, rgba(255, 255, 255, 0) 70%);
  border-radius: 50%;
  z-index: 0;
}

.logo {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
  z-index: 1;
}

.logo h1 {
  font-size: 32px;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 5px;
  position: relative;
  display: inline-block;
}

.logo h1::after {
  content: '';
  position: absolute;
  bottom: -6px;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
  border-radius: 3px;
}

.logo p {
  color: #666;
  font-size: 16px;
  margin-top: 15px;
}

/* 表单样式 */
.el-form {
  position: relative;
  z-index: 1;
}

.el-form :deep(.el-form-item__label) {
  color: #555;
  font-weight: 500;
  font-size: 15px;
}

.el-form :deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
}

.el-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(0, 120, 215, 0.1);
}

.el-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset, 0 2px 12px rgba(0, 120, 215, 0.1);
}

.policy-text {
  font-size: 14px;
  color: #666;
}

.register-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-dark));
  border: none;
  border-radius: 8px;
  margin-top: 10px;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0, 120, 215, 0.25);
}

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 120, 215, 0.35);
}

.register-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 120, 215, 0.25);
}

.text-center {
  text-align: center;
}

.mt-3 {
  margin-top: 15px;
}

/* 对话框样式 */
.terms-content, .policy-content {
  max-height: 400px;
  overflow-y: auto;
  padding: 15px;
  line-height: 1.6;
}

.terms-content h3, .policy-content h3 {
  color: var(--primary-color);
  margin-bottom: 15px;
}

.terms-content p, .policy-content p {
  margin-bottom: 10px;
}

@media (max-width: 768px) {
  .register-container {
    width: 95%;
    max-width: 400px;
    padding: 30px 20px;
  }
  
  .logo h1 {
    font-size: 26px;
  }
  
  .logo p {
    font-size: 14px;
  }
}
</style> 