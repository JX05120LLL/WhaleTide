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
			<view class="input-content">
				<view class="input-item">
					<text class="tit">手机号</text>
					<input 
						type="number" 
						v-model="mobile" 
						placeholder="请输入手机号" 
						maxlength="11"
						@input="onMobileInput"
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
					<input 
						type="text" 
						v-model="password" 
						placeholder="8-18位不含特殊字符的数字、字母组合" 
						placeholder-class="input-empty" 
						maxlength="20"
						password
					/>
				</view>
			</view>
			<button class="confirm-btn" @click="submitRegister" :disabled="registering">立即注册</button>
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
				registering: false
			}
		},
		methods: {
			navBack() {
				uni.navigateBack();
			},
			onMobileInput(e) {
				this.mobile = e.detail.value.replace(/[^\d]/g, '');
			},
			async getCode() {
				if (!this.validateMobile()) return;
				
				this.codeDisabled = true;
				await getSmsCode(this.mobile);
				
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
					await memberRegister({
						mobile: this.mobile,
						code: this.code,
						password: this.password
					});
					
					uni.showToast({
						title: '注册成功',
						icon: 'success',
						complete: () => {
							setTimeout(() => {
								this.navBack();
							}, 1500);
						}
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
				if (!/^[a-zA-Z0-9]{8,18}$/.test(this.password)) {
					uni.showToast({ title: '密码格式不正确', icon: 'none' });
					return false;
				}
				return true;
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
		padding-bottom: 120upx;  /* 增加底部间距 */
	}

	.left-top-sign {
		font-size: 120upx;
		color: $page-color-base;
		position: relative;
		left: -16upx;
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
	}

	.confirm-btn {
		width: 630upx;
		height: 76upx;
		line-height: 76upx;
		border-radius: 50px;
		margin: 70upx auto 40upx;  /* 修改为上下间距模式 */
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
		margin-top: 30upx;  /* 增加顶部间距 */

		text {
			color: $font-color-spec;
			margin-left: 10upx;
		}
	}
</style>