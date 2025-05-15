/* eslint-disable */
<template>
  <div class="security-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>账号安全</span>
        </div>
      </template>
      
      <div class="security-menu">
        <div class="security-item" @click="activeTab = 'password'">
          <div class="security-item-left">
            <el-icon><Lock /></el-icon>
            <span class="title">修改登录密码</span>
          </div>
          <el-icon><ArrowRight /></el-icon>
        </div>
        
        <div class="security-item" @click="activeTab = 'phone'">
          <div class="security-item-left">
            <el-icon><Iphone /></el-icon>
            <span class="title">绑定手机号</span>
            <el-tag v-if="userInfo.phone" size="small" type="success">已绑定</el-tag>
            <el-tag v-else size="small" type="warning">未绑定</el-tag>
          </div>
          <el-icon><ArrowRight /></el-icon>
        </div>
        
        <div class="security-item" @click="activeTab = 'email'">
          <div class="security-item-left">
            <el-icon><Message /></el-icon>
            <span class="title">绑定邮箱</span>
            <el-tag v-if="userInfo.email" size="small" type="success">已绑定</el-tag>
            <el-tag v-else size="small" type="warning">未绑定</el-tag>
          </div>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </el-card>
    
    <!-- 修改密码表单 -->
    <el-card v-if="activeTab === 'password'" class="box-card form-card">
      <template #header>
        <div class="card-header">
          <span>修改登录密码</span>
        </div>
      </template>
      
      <el-form 
        :model="passwordForm" 
        :rules="passwordRules" 
        ref="passwordFormRef" 
        label-position="top"
        class="password-form"
      >
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input 
            v-model="passwordForm.oldPassword" 
            type="password" 
            placeholder="请输入当前密码"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            v-model="passwordForm.newPassword" 
            type="password" 
            placeholder="请输入新密码"
            show-password
          ></el-input>
          <div class="form-tip">密码长度8-20位，必须包含字母、数字</div>
        </el-form-item>
        
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input 
            v-model="passwordForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入新密码"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="updatePassword" :loading="submitting">
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 绑定手机号表单 -->
    <el-card v-if="activeTab === 'phone'" class="box-card form-card">
      <template #header>
        <div class="card-header">
          <span>绑定手机号</span>
        </div>
      </template>
      
      <el-form 
        :model="phoneForm" 
        :rules="phoneRules" 
        ref="phoneFormRef" 
        label-position="top"
        class="phone-form"
      >
        <el-form-item label="手机号码" prop="phone">
          <el-input 
            v-model="phoneForm.phone" 
            placeholder="请输入手机号码"
            maxlength="11"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="验证码" prop="code">
          <div class="code-input">
            <el-input 
              v-model="phoneForm.code" 
              placeholder="请输入验证码"
              maxlength="6"
            ></el-input>
            <el-button 
              type="primary" 
              :disabled="codeDisabled" 
              @click="sendCode"
            >
              {{ codeButtonText }}
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="bindPhone" :loading="submitting">
            确认绑定
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 绑定邮箱表单 -->
    <el-card v-if="activeTab === 'email'" class="box-card form-card">
      <template #header>
        <div class="card-header">
          <span>绑定邮箱</span>
        </div>
      </template>
      
      <el-form 
        :model="emailForm" 
        :rules="emailRules" 
        ref="emailFormRef" 
        label-position="top"
        class="email-form"
      >
        <el-form-item label="邮箱地址" prop="email">
          <el-input 
            v-model="emailForm.email" 
            placeholder="请输入邮箱地址"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="验证码" prop="code">
          <div class="code-input">
            <el-input 
              v-model="emailForm.code" 
              placeholder="请输入验证码"
              maxlength="6"
            ></el-input>
            <el-button 
              type="primary" 
              :disabled="emailCodeDisabled" 
              @click="sendEmailCode"
            >
              {{ emailCodeButtonText }}
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="bindEmail" :loading="submitting">
            确认绑定
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { http } from '@/utils/request';
import { useStore } from 'vuex';
import { Lock, Iphone, Message, ArrowRight } from '@element-plus/icons-vue';

export default {
  name: 'UserSecurity',
  components: {
    Lock,
    Iphone,
    Message,
    ArrowRight
  },
  setup() {
    const store = useStore();
    
    // 用户信息
    const userInfo = computed(() => store.state.user.userInfo || {});
    
    // 当前激活的表单
    const activeTab = ref('password');
    
    // 表单引用
    const passwordFormRef = ref(null);
    const phoneFormRef = ref(null);
    const emailFormRef = ref(null);
    
    // 提交状态
    const submitting = ref(false);
    
    // 修改密码表单
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    });
    
    // 密码校验规则
    const validatePass = (rule, value, callback) => {
      const pattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/;
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (!pattern.test(value)) {
        callback(new Error('密码必须包含字母和数字，长度8-20位'));
      } else {
        if (passwordForm.confirmPassword !== '') {
          passwordFormRef.value.validateField('confirmPassword');
        }
        callback();
      }
    };
    
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    
    const passwordRules = {
      oldPassword: [
        { required: true, message: '请输入当前密码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, trigger: 'blur', validator: validatePass }
      ],
      confirmPassword: [
        { required: true, trigger: 'blur', validator: validateConfirmPass }
      ]
    };
    
    // 修改密码
    const updatePassword = () => {
      passwordFormRef.value.validate(async (valid) => {
        if (valid) {
          submitting.value = true;
          try {
            const res = await http.post('/user/password/update', passwordForm);
            if (res.code === 200) {
              ElMessage.success('密码修改成功，请重新登录');
              setTimeout(() => {
                store.dispatch('logout');
                window.location.href = '/login';
              }, 1500);
            } else {
              ElMessage.error(res.message || '密码修改失败');
            }
          } catch (error) {
            console.error('密码修改失败:', error);
            ElMessage.error('网络错误，请稍后重试');
          } finally {
            submitting.value = false;
          }
        }
      });
    };
    
    // 手机号表单
    const phoneForm = reactive({
      phone: '',
      code: ''
    });
    
    const phoneRules = {
      phone: [
        { required: true, message: '请输入手机号码', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { pattern: /^\d{6}$/, message: '验证码格式不正确', trigger: 'blur' }
      ]
    };
    
    // 验证码倒计时
    const countdown = ref(0);
    const codeButtonText = computed(() => {
      return countdown.value > 0 ? `${countdown.value}秒后重新获取` : '获取验证码';
    });
    const codeDisabled = computed(() => {
      return countdown.value > 0;
    });
    
    // 发送验证码
    const sendCode = async () => {
      // 校验手机号
      try {
        await phoneFormRef.value.validateField('phone');
        
        // 发送验证码请求
        try {
          const res = await http.post('/user/send-code', { phone: phoneForm.phone });
          if (res.code === 200) {
            ElMessage.success('验证码已发送');
            // 开始倒计时
            countdown.value = 60;
            const timer = setInterval(() => {
              countdown.value--;
              if (countdown.value <= 0) {
                clearInterval(timer);
              }
            }, 1000);
          } else {
            ElMessage.error(res.message || '验证码发送失败');
          }
        } catch (error) {
          console.error('验证码发送失败:', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      } catch (error) {
        // 手机号验证失败
        return;
      }
    };
    
    // 绑定手机号
    const bindPhone = () => {
      phoneFormRef.value.validate(async (valid) => {
        if (valid) {
          submitting.value = true;
          try {
            const res = await http.post('/user/bind-phone', phoneForm);
            if (res.code === 200) {
              ElMessage.success('手机号绑定成功');
              // 更新用户信息
              store.dispatch('getUserInfo');
              // 清空表单
              phoneFormRef.value.resetFields();
            } else {
              ElMessage.error(res.message || '手机号绑定失败');
            }
          } catch (error) {
            console.error('手机号绑定失败:', error);
            ElMessage.error('网络错误，请稍后重试');
          } finally {
            submitting.value = false;
          }
        }
      });
    };
    
    // 邮箱表单
    const emailForm = reactive({
      email: '',
      code: ''
    });
    
    const emailRules = {
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { pattern: /^\d{6}$/, message: '验证码格式不正确', trigger: 'blur' }
      ]
    };
    
    // 邮箱验证码倒计时
    const emailCountdown = ref(0);
    const emailCodeButtonText = computed(() => {
      return emailCountdown.value > 0 ? `${emailCountdown.value}秒后重新获取` : '获取验证码';
    });
    const emailCodeDisabled = computed(() => {
      return emailCountdown.value > 0;
    });
    
    // 发送邮箱验证码
    const sendEmailCode = async () => {
      // 校验邮箱
      try {
        await emailFormRef.value.validateField('email');
        
        // 发送验证码请求
        try {
          const res = await http.post('/user/send-email-code', { email: emailForm.email });
          if (res.code === 200) {
            ElMessage.success('验证码已发送到邮箱');
            // 开始倒计时
            emailCountdown.value = 60;
            const timer = setInterval(() => {
              emailCountdown.value--;
              if (emailCountdown.value <= 0) {
                clearInterval(timer);
              }
            }, 1000);
          } else {
            ElMessage.error(res.message || '验证码发送失败');
          }
        } catch (error) {
          console.error('验证码发送失败:', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      } catch (error) {
        // 邮箱验证失败
        return;
      }
    };
    
    // 绑定邮箱
    const bindEmail = () => {
      emailFormRef.value.validate(async (valid) => {
        if (valid) {
          submitting.value = true;
          try {
            const res = await http.post('/user/bind-email', emailForm);
            if (res.code === 200) {
              ElMessage.success('邮箱绑定成功');
              // 更新用户信息
              store.dispatch('getUserInfo');
              // 清空表单
              emailFormRef.value.resetFields();
            } else {
              ElMessage.error(res.message || '邮箱绑定失败');
            }
          } catch (error) {
            console.error('邮箱绑定失败:', error);
            ElMessage.error('网络错误，请稍后重试');
          } finally {
            submitting.value = false;
          }
        }
      });
    };
    
    onMounted(() => {
      // 如果用户信息未加载，从服务器获取
      if (!userInfo.value.id) {
        store.dispatch('getUserInfo');
      }
    });
    
    return {
      userInfo,
      activeTab,
      passwordFormRef,
      phoneFormRef,
      emailFormRef,
      passwordForm,
      phoneForm,
      emailForm,
      passwordRules,
      phoneRules,
      emailRules,
      submitting,
      codeButtonText,
      codeDisabled,
      emailCodeButtonText,
      emailCodeDisabled,
      updatePassword,
      sendCode,
      bindPhone,
      sendEmailCode,
      bindEmail
    };
  }
};
</script>

<style scoped>
.security-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.security-menu {
  display: flex;
  flex-direction: column;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: background-color 0.3s;
}

.security-item:last-child {
  border-bottom: none;
}

.security-item:hover {
  background-color: #f5f7fa;
}

.security-item-left {
  display: flex;
  align-items: center;
}

.security-item-left .el-icon {
  margin-right: 10px;
  font-size: 20px;
  color: #409eff;
}

.security-item-left .title {
  margin-right: 10px;
  font-size: 16px;
}

.form-card {
  max-width: 500px;
}

.password-form,
.phone-form,
.email-form {
  padding: 10px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.code-input {
  display: flex;
}

.code-input .el-input {
  margin-right: 10px;
}

.code-input .el-button {
  white-space: nowrap;
}
</style> 