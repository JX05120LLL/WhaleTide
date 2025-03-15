<template>
  <div class="login-container">
    <el-card class="login-form-layout">
      <el-form autoComplete="on"
               :model="loginForm"
               :rules="loginRules"
               ref="loginForm"
               label-position="left">
        <div style="text-align: center">
          <svg-icon icon-class="login-mall" style="width: 56px;height: 56px;color: #409EFF"></svg-icon>
        </div>
        <h2 class="login-title color-main">WhaleTide-admin-web</h2>
        <el-form-item prop="username">
          <el-input name="username"
                    type="text"
                    v-model="loginForm.username"
                    autoComplete="on"
                    placeholder="请输入用户名">
          <span slot="prefix">
            <svg-icon icon-class="user" class="color-main"></svg-icon>
          </span>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input name="password"
                    :type="pwdType"
                    @keyup.enter.native="handleLogin"
                    v-model="loginForm.password"
                    autoComplete="on"
                    placeholder="请输入密码">
          <span slot="prefix">
            <svg-icon icon-class="password" class="color-main"></svg-icon>
          </span>
            <span slot="suffix" @click="showPwd">
            <svg-icon icon-class="eye" class="color-main"></svg-icon>
          </span>
          </el-input>
        </el-form-item>
        <el-form-item class="login-button-container">
          <div class="button-row">
            <el-button type="primary" 
                      class="login-button" 
                      :loading="loading" 
                      @click.native.prevent="handleLogin">
              登录
            </el-button>
            <el-button type="primary" 
                      class="login-button" 
                      @click.native.prevent="handleTry">
              体验账号
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
    <img :src="login_center_bg" class="login-center-layout">
    
    <!-- 微信二维码弹窗 -->
    <el-dialog
      title="扫码添加微信获取体验账号"
      :visible.sync="dialogVisible"
      width="300px"
      center>
      <div class="qrcode-container">
        <img :src="wechatQrcode" class="wechat-qrcode" alt="微信二维码">
        <p class="qrcode-tip">请扫描上方二维码添加微信</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="dialogVisible = false">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {isvalidUsername} from '@/utils/validate';
  import {setSupport,getSupport,setCookie,getCookie} from '@/utils/support';
  import login_center_bg from '@/assets/images/login_center_bg.png'
  // 这里需要导入您的微信二维码图片，请确保图片路径正确
  import wechatQrcode from '@/assets/images/wechat_qrcode.png'

  export default {
    name: 'login',
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!isvalidUsername(value)) {
          callback(new Error('请输入正确的用户名'))
        } else {
          callback()
        }
      };
      const validatePass = (rule, value, callback) => {
        if (value.length < 3) {
          callback(new Error('密码不能小于3位'))
        } else {
          callback()
        }
      };
      return {
        loginForm: {
          username: '',
          password: '',
        },
        loginRules: {
          username: [{required: true, trigger: 'blur', validator: validateUsername}],
          password: [{required: true, trigger: 'blur', validator: validatePass}]
        },
        loading: false,
        pwdType: 'password',
        login_center_bg,
        wechatQrcode,
        dialogVisible: false
      }
    },
    created() {
      this.loginForm.username = getCookie("username");
      this.loginForm.password = getCookie("password");
      if(this.loginForm.username === undefined||this.loginForm.username==null||this.loginForm.username===''){
        this.loginForm.username = 'admin';
      }
      if(this.loginForm.password === undefined||this.loginForm.password==null){
        this.loginForm.password = '';
      }
    },
    methods: {
      showPwd() {
        if (this.pwdType === 'password') {
          this.pwdType = ''
        } else {
          this.pwdType = 'password'
        }
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            this.$store.dispatch('Login', this.loginForm).then(() => {
              this.loading = false;
              setCookie("username",this.loginForm.username,15);
              setCookie("password",this.loginForm.password,15);
              this.$router.push({path: '/'})
            }).catch(() => {
              this.loading = false
            })
          } else {
            console.log('参数验证不合法！');
            return false
          }
        })
      },
      handleTry(){
        // 显示微信二维码弹窗
        this.dialogVisible = true;
      }
    }
  }
</script>

<style scoped>
  .login-container {
    height: 100vh;
    background-color: #f5f7fa;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .login-form-layout {
    width: 420px;
    border-top: 10px solid #409EFF;
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px 35px 35px;
    z-index: 1;
    background-color: #fff;
    margin: 0 auto;
  }

  .login-title {
    text-align: center;
    margin: 15px 0 25px;
    font-size: 24px;
    font-weight: bold;
  }

  .login-button-container {
    margin-top: 40px;
    margin-bottom: 0;
  }
  
  .button-row {
    display: flex;
    justify-content: space-between;
  }
  
  .login-button {
    width: 180px;
    height: 40px;
    border-radius: 4px;
    font-size: 16px;
  }

  .el-form-item {
    margin-bottom: 22px;
  }

  .login-center-layout {
    background: #409EFF;
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
    margin-top: 200px;
    position: fixed;
    bottom: 0;
    z-index: 0;
  }
  
  /* 二维码样式 */
  .qrcode-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
  }
  
  .wechat-qrcode {
    width: 200px;
    height: 200px;
    object-fit: contain;
  }
  
  .qrcode-tip {
    margin-top: 15px;
    color: #606266;
    font-size: 14px;
  }
</style>
