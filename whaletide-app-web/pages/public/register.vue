<template>
	<view class="container">
		<view class="left-bottom-sign"></view>
		<view class="back-btn yticon icon-zuojiantou-up" @click="navBack"></view>
		<view class="right-top-sign"></view>
		<view class="wrapper">
			<view class="left-top-sign">REGISTER</view>
			<view class="welcome">
				欢迎注册！
			</view>
			<form autocomplete="off">
				<view class="input-content">
					<view class="input-item">
						<text class="tit">手机号</text>
						<input 
							type="number" 
							v-model="mobile" 
							placeholder="请输入手机号" 
							maxlength="11"
							@input="onMobileInput"
							autocomplete="off"
						/>
					</view>
					<view class="input-item">
						<text class="tit">验证码</text>
						<view class="code-box">
							<input
								type="number"
								v-model="code"
								placeholder="请输入验证码"
								maxlength="6"
								class="code-input"
								autocomplete="off"
							/>
							<button 
								class="code-btn" 
								:disabled="codeDisabled"
								@click="getCode"
							>
								{{ codeBtnText }}
							</button>
						</view>
					</view>
					<view class="input-item">
						<text class="tit">密码</text>
						<view class="password-container">
							<input 
								type="text" 
								v-model="password" 
								placeholder="请输入不少于6位的密码" 
								placeholder-class="input-empty" 
								maxlength="20"
								:password="!passwordVisible"
								autocomplete="new-password"
							/>
							<text 
								class="password-eye yticon" 
								:class="passwordVisible ? 'icon-yanjing' : 'icon-biyan'" 
								@click="togglePasswordVisibility"
							></text>
						</view>
					</view>
				</view>
				<button class="confirm-btn" @click="submitRegister" :disabled="registering">立即注册</button>
			</form>
			<view class="register-section">
				已有账号?
				<text @click="navBack">立即登录</text>
			</view>
		</view>
	</view>
</template>

<script>
	import { getSmsCode, memberRegister } from '@/api/member.js';
	export default {
		data() {
			return {
				mobile: '',
				code: '',
				password: '',
				codeDisabled: false,
				codeBtnText: '获取验证码',
				countdown: 60,
				registering: false,
				passwordVisible: false
			}
		},
		created() {
			// 创建时就确保密码为空
			this.password = '';
		},
		mounted() {
			// 组件挂载后清空密码
			this.forceResetPassword();
		},
		onLoad() {
			// 确保初始化时表单为空
			this.resetForm();
			// 使用延时确保DOM完全加载后再清空
			setTimeout(() => this.forceResetPassword(), 200);
		},
		onShow() {
			// 页面显示时重置表单
			this.resetForm();
			// 使用延时确保DOM完全加载后再清空
			setTimeout(() => this.forceResetPassword(), 200);
		},
		watch: {
			// 监控password，一旦有自动填充就清空
			password(val) {
				if (val && this.autoFilled) {
					this.$nextTick(() => {
						this.password = '';
						this.autoFilled = false;
					});
				}
			}
		},
		methods: {
			// 重置表单数据
			resetForm() {
				this.mobile = '';
				this.code = '';
				this.password = '';
				this.codeDisabled = false;
				this.codeBtnText = '获取验证码';
				this.countdown = 60;
				this.passwordVisible = false;
				
				// 设置表单元素的autocomplete属性
				this.$nextTick(() => {
					const formElement = document.querySelector('form');
					if (formElement) {
						formElement.setAttribute('autocomplete', 'off');
					}
					
					const inputs = document.querySelectorAll('input');
					inputs.forEach(input => {
						if (input.type === 'text' && input.hasAttribute('password')) {
							input.value = '';
							input.setAttribute('autocomplete', 'new-password');
						} else {
							input.setAttribute('autocomplete', 'off');
						}
					});
				});
			},
			// 强制重置密码输入框
			forceResetPassword() {
				// 重置数据
				this.password = '';
				
				// 尝试直接操作DOM
				this.$nextTick(() => {
					// 找到密码输入框并清空
					const pwdInputs = document.querySelectorAll('input[type="text"]');
					pwdInputs.forEach(input => {
						if (input.hasAttribute('password') || input.name === 'password') {
							input.value = '';
						}
					});
				});
			},
			navBack() {
				uni.navigateBack();
			},
			onMobileInput(e) {
				this.mobile = e.detail.value.replace(/[^\d]/g, '');
			},
			async getCode() {
				if (!this.validateMobile()) return;
				
				this.codeDisabled = true;
				try {
					await getSmsCode(this.mobile);
					
					uni.showToast({
						title: '验证码已发送，请查收短信',
						icon: 'none',
						duration: 3000
					});
					
					const timer = setInterval(() => {
						if (this.countdown <= 0) {
							clearInterval(timer);
							this.codeBtnText = '重新获取';
							this.countdown = 60;
							this.codeDisabled = false;
						} else {
							this.codeBtnText = `${this.countdown}s后重发`;
							this.countdown--;
						}
					}, 1000);
				} catch (error) {
					console.error('获取验证码失败:', error);
					uni.showToast({
						title: error.message || '获取验证码失败，请稍后重试',
						icon: 'none',
						duration: 3000
					});
					this.codeDisabled = false;
				}
			},
			validateMobile() {
				const reg = /^1[3-9]\d{9}$/;
				if (!reg.test(this.mobile)) {
					uni.showToast({ title: '手机号格式错误', icon: 'none' });
					return false;
				}
				return true;
			},
			async submitRegister() {
				if (!this.validateForm()) return;
				
				this.registering = true;
				try {
					const registerData = {
						mobile: this.mobile,
						code: this.code,
						password: this.password
					};
					
					const result = await memberRegister(registerData);
					
					uni.showToast({
						title: '注册成功',
						icon: 'success',
						complete: () => {
							setTimeout(() => {
								// 直接跳转到登录页面
								uni.redirectTo({
									url: '/pages/public/login'
								});
							}, 1500);
						}
					});
				} catch (error) {
					console.error('注册失败:', error);
					
					// 提供更具体的错误信息
					let errorMsg = '注册失败，请稍后重试';
					if (error.response && error.response.data && error.response.data.message) {
						errorMsg = error.response.data.message;
					} else if (error.message) {
						errorMsg = error.message;
					}
					
					uni.showToast({
						title: errorMsg,
						icon: 'none',
						duration: 3000
					});
				} finally {
					this.registering = false;
				}
			},
			validateForm() {
				if (!this.validateMobile()) return false;
				if (!this.code) {
					uni.showToast({ title: '请输入验证码', icon: 'none' });
					return false;
				}
				
				// 检查密码长度
				if (this.password.length < 6) {
					uni.showToast({ 
						title: '密码长度至少6位', 
						icon: 'none',
						duration: 2500
					});
					return false;
				}
				
				return true;
			},
			togglePasswordVisibility() {
				this.passwordVisible = !this.passwordVisible;
			}
		}
	}
</script>

<style lang='scss'>
	.container {
		padding-top: 115px;
		position: relative;
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		background: #fff;
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 120upx;
	}

	.back-btn {
		position: absolute;
		left: 40upx;
		z-index: 9999;
		padding-top: var(--status-bar-height);
		top: 40upx;
		font-size: 40upx;
		color: $font-color-dark;
	}

	.left-top-sign {
		font-size: 120upx;
		color: $page-color-base;
		position: relative;
		left: -16upx;
	}

	.right-top-sign {
		position: absolute;
		top: 80upx;
		right: -30upx;
		z-index: 95;

		&:before,
		&:after {
			display: block;
			content: "";
			width: 400upx;
			height: 80upx;
			background: #b4f3e2;
		}

		&:before {
			transform: rotate(50deg);
			border-radius: 0 50px 0 0;
		}

		&:after {
			position: absolute;
			right: -198upx;
			top: 0;
			transform: rotate(-50deg);
			border-radius: 50px 0 0 0;
		}
	}

	.left-bottom-sign {
		position: absolute;
		left: -270upx;
		bottom: -320upx;
		border: 100upx solid #d0d1fd;
		border-radius: 50%;
		padding: 180upx;
	}

	.welcome {
		position: relative;
		left: 50upx;
		top: -90upx;
		font-size: 46upx;
		color: #555;
		text-shadow: 1px 0px 1px rgba(0, 0, 0, .3);
	}

	.input-content {
		padding: 0 60upx;
	}

	.input-item {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: center;
		padding: 0 30upx;
		background: $page-color-light;
		height: 120upx;
		border-radius: 4px;
		margin-bottom: 50upx;

		.tit {
			height: 50upx;
			line-height: 56upx;
			font-size: $font-sm+2upx;
			color: $font-color-base;
		}

		input {
			height: 60upx;
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			width: 100%;
		}

		.code-box {
			width: 100%;
			display: flex;
			align-items: center;
			justify-content: space-between;
			
			.code-input {
				flex: 1;
				margin-right: 20upx;
			}
			
			.code-btn {
				width: 200upx;
				height: 60upx;
				line-height: 60upx;
				font-size: $font-sm;
				color: $uni-color-primary;
				background: transparent;
				padding: 0;
				
				&:after {
					border: none;
				}
				
				&[disabled] {
					color: $font-color-disabled;
				}
			}
		}

		.password-container {
			width: 100%;
			display: flex;
			align-items: center;
			position: relative;
			
			input {
				flex: 1;
				height: 60upx;
				font-size: $font-base + 2upx;
				color: $font-color-dark;
			}
			
			.password-eye {
				position: absolute;
				right: 10upx;
				font-size: 36upx;
				color: $font-color-base;
			}
		}
	}

	.confirm-btn {
		width: 630upx;
		height: 76upx;
		line-height: 76upx;
		border-radius: 50px;
		margin: 70upx auto 40upx;
		background: $uni-color-primary;
		color: #fff;
		font-size: $font-lg;
		display: block;

		&:after {
			border-radius: 100px;
		}
	}

	.register-section {
		text-align: center;
		font-size: $font-sm+2upx;
		color: $font-color-base;
		margin-top: 30upx;

		text {
			color: $font-color-spec;
			margin-left: 10upx;
		}
	}
</style> 