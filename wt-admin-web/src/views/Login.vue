<template>
  <div class="login-container" :class="{ 'login-success': loginSuccess }">
    <div class="login-box" :class="{ 'animate-success': loginSuccess }">
      <div class="login-logo">
        <h1 class="logo-text">Whale Tide</h1>
        <p class="logo-desc">后台管理系统</p>
      </div>
      
      <el-form 
        ref="loginForm" 
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form" 
        autocomplete="on"
      >
        <el-form-item prop="username">
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="请输入用户名"
            name="username"
            type="text"
            tabindex="1"
            autocomplete="on"
            prefix-icon="el-icon-user"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            ref="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
            name="password"
            :type="passwordVisible ? 'text' : 'password'"
            tabindex="2"
            autocomplete="on"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="handleLogin"
          >
            <el-button 
              slot="suffix" 
              icon="el-icon-view" 
              type="text"
              class="password-toggle"
              @click="passwordVisible = !passwordVisible"
            ></el-button>
          </el-input>
        </el-form-item>

        <el-form-item class="login-button-item">
          <el-button 
            :loading="loading" 
            type="primary" 
            class="login-button" 
            @click.native.prevent="handleLogin"
          >
            登录系统
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>© 2025 Whale TideS 管理系统</p>
      </div>
    </div>
    
    <!-- 登录成功动画元素 -->
    <div class="success-animation" v-if="loginSuccess">
      <i class="el-icon-check"></i>
    </div>
  </div>
</template>

<script>
import { resetTokenTipStatus } from '@/permission' // 导入重置令牌提示状态的函数

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordVisible: false,
      loading: false,
      loginSuccess: false, // 登录成功标志
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          const loginData = {
            username: this.loginForm.username.trim(),
            password: this.loginForm.password
          }
          this.$store.dispatch('user/login', loginData)
            .then(() => {
              this.$message.success('登录成功')
              
              // 设置登录成功状态，触发动画
              this.loginSuccess = true
              
              // 重置令牌提示状态
              resetTokenTipStatus && resetTokenTipStatus()
              
              // 延迟跳转，等待动画完成
              setTimeout(() => {
                const redirectPath = this.$route.query.redirect || '/dashboard'
                this.$router.replace({ 
                  path: redirectPath, 
                  query: { noredirect: 'true' } 
                })
                this.loading = false
                
                // 重置状态，以便下次登录
                setTimeout(() => {
                  this.loginSuccess = false
                }, 500)
              }, 800) // 动画持续时间
            })
            .catch(error => {
              let errorMsg = '登录失败，请检查用户名和密码'
              if (error && error.message) {
                if (error.message !== '登录成功') {
                  errorMsg = error.message
                }
              }
              console.error('登录失败:', errorMsg)
              this.$message.error(errorMsg)
              this.loading = false
            })
        } else {
          console.log('错误提交!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100%;
  background-image: linear-gradient(to right, #1a2980, #26d0ce);
  overflow: hidden;
  position: relative;
  transition: all 0.8s ease;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml;charset=utf8,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"%3E%3Cpath fill="%23ffffff" fill-opacity="0.05" d="M0,288L48,272C96,256,192,224,288,197.3C384,171,480,149,576,165.3C672,181,768,235,864,250.7C960,267,1056,245,1152,208C1248,171,1344,117,1392,90.7L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"%3E%3C/path%3E%3C/svg%3E');
    background-size: cover;
    background-position: center bottom;
    transition: opacity 0.8s ease;
  }
  
  // 登录成功时的背景变化
  &.login-success {
    background-image: linear-gradient(to right, #0f5e7c, #12c2e9);
    
    &::before {
      opacity: 0;
    }
  }
}

.login-box {
  width: 400px;
  padding: 30px 40px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  z-index: 10;
  position: relative;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  
  // 登录成功时的动画
  &.animate-success {
    transform: scale(0.8);
    opacity: 0;
    filter: blur(10px);
  }
}

// 登录成功动画元素样式
.success-animation {
  position: absolute;
  z-index: 20;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #fff;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  animation: success-bounce 0.8s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  
  i {
    font-size: 50px;
    color: #67C23A;
    animation: success-fade 0.8s ease;
  }
}

@keyframes success-bounce {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes success-fade {
  0% {
    opacity: 0;
    transform: scale(0.5);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

.login-logo {
  text-align: center;
  margin-bottom: 30px;
  
  .logo-text {
    font-size: 28px;
    font-weight: bold;
    margin: 0;
    color: #1a2980;
    letter-spacing: 1px;
  }
  
  .logo-desc {
    font-size: 16px;
    color: #666;
    margin: 5px 0 0;
  }
}

.login-form {
  margin-bottom: 20px;
  
  .el-form-item {
    margin-bottom: 25px;
  }
  
  .el-input {
    height: 50px;
    
    ::v-deep .el-input__inner {
      height: 50px;
      line-height: 50px;
      padding-left: 45px;
      background-color: #f5f7fa;
      border: none;
      border-radius: 5px;
      color: #333;
      font-size: 15px;
      
      &:focus {
        background-color: #fff;
        box-shadow: 0 0 0 2px rgba(26, 41, 128, 0.2);
      }
    }
    
    ::v-deep .el-input__prefix {
      left: 15px;
      font-size: 18px;
      color: #909399;
    }
    
    ::v-deep .el-input__suffix {
      right: 15px;
    }
  }
  
  .password-toggle {
    background: transparent;
    color: #909399;
    padding: 0;
    
    &:hover, &:focus {
      color: #1a2980;
    }
  }
}

.login-button-item {
  margin-top: 30px;
  margin-bottom: 0;
}

.login-button {
  width: 100%;
  height: 50px;
  border-radius: 5px;
  font-size: 16px;
  background-image: linear-gradient(to right, #1a2980, #26d0ce);
  border: none;
  transition: all 0.3s ease;
  
  &:hover, &:focus {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(26, 41, 128, 0.3);
    background-image: linear-gradient(to right, #253b9d, #29e1de);
  }
}

.login-footer {
  text-align: center;
  margin-top: 30px;
  font-size: 14px;
  color: #909399;
}
</style> 