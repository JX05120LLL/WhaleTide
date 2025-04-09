<template>
	<view class="container">
		<view class="left-bottom-sign"></view>
		<view class="back-btn yticon icon-zuojiantou-up" @click="navBack"></view>
		<view class="right-top-sign"></view>
		<!-- 设置白色背景防止软键盘把下部绝对定位元素顶上来盖住输入框等 -->
		<view class="wrapper">
			<view class="left-top-sign">LOGIN</view>
			<view class="welcome">
				欢迎回来！
			</view>
			<view class="input-content">
				<view class="input-item">
					<text class="tit">用户名</text>
					<input type="text" v-model="username" placeholder="请输入用户名" maxlength="11"/>
				</view>
				<view class="input-item">
					<text class="tit">密码</text>
					<input type="text" v-model="password" placeholder="请输入不少于6位的密码" placeholder-class="input-empty" maxlength="20"
					 password @confirm="toLogin" />
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin" :disabled="logining">登录</button>
			<button class="confirm-btn2" @click="toRegist">忘记密码</button>
		</view>
		<view class="register-section">
			还没有账号?
			<text @click="toRegist">马上注册</text>
		</view>
	</view>
</template>

<script>
	import {
		mapMutations
	} from 'vuex';
	import {
		memberLogin,memberInfo
	} from '@/api/member.js';
	export default {
		data() {
			return {
				username: '',
				password: '',
				logining: false
			}
		},
		onLoad() {
			this.username = uni.getStorageSync('username') || '';
			this.password = uni.getStorageSync('password') || '';
		},
		methods: {
			...mapMutations(['login']),
			navBack() {
				uni.navigateBack();
			},
			toRegist() {
				uni.navigateTo({
					url: '/pages/public/register'
				});
			},
			async toLogin() {
				this.logining = true;
				
				// 先清除可能存在的旧token
				uni.removeStorageSync('Authorization');
				uni.removeStorageSync('FullAuthorization');
				
				memberLogin({
					username: this.username,
					password: this.password
				}).then(response => {
					// 打印完整响应用于调试
					console.log('登录响应完整数据:', JSON.stringify(response));
					
					if(response.code === 200) {
						// 存储用户token
						let token = response.data.token;
						let tokenHead = response.data.tokenHead || 'Bearer ';
						
						// 检查token格式并记录
						console.log('服务器返回token:', token);
						console.log('token格式:', { 
							length: token.length,
							hasDots: token.includes('.'),
							dotParts: token.split('.').length
						});
						
						// 如果token已经包含Bearer前缀，移除它，保持一致性
						if(token.startsWith('Bearer ')) {
							token = token.substring(7);
							console.log('移除Bearer前缀后的token:', token);
						}
						
						// 单独存储原始token和完整的授权头
						const cleanToken = token.trim();
						uni.setStorageSync('Authorization', cleanToken);
						uni.setStorageSync('FullAuthorization', tokenHead + cleanToken);
						
						// 记录存储值
						console.log('存储的token:', cleanToken);
						console.log('存储的完整授权头:', tokenHead + cleanToken);
						
						// 存储用户名和密码，便于自动重新登录
						uni.setStorageSync('username', this.username);
						uni.setStorageSync('password', this.password);
						
						// 登录成功后延迟一下再获取用户信息，确保token已存储完成
						setTimeout(() => {
							memberInfo().then(response => {
								this.login(response.data);
								uni.switchTab({url:'/pages/index/index'});
							}).catch(error => {
								console.error('获取用户信息失败:', error);
								// 处理获取用户信息失败的情况
								uni.showToast({
									title: '获取用户信息失败，请重试',
									icon: 'none'
								});
								this.logining = false;
							});
						}, 300);
					} else {
						console.error('登录失败: 服务器返回的认证信息格式不正确');
						this.logining = false;
						uni.showToast({
							title: '登录失败: 服务器返回的认证信息格式不正确',
							icon: 'none'
						});
					}
				}).catch(error => {
					console.error('登录失败:', error);
					uni.showToast({
						title: '用户名或密码错误',
						icon: 'none'
					});
					this.logining = false;
				});
			},
		},

	}
</script>

<style lang="scss">
	page {
		background: #FFF5F8;
	}
	
	.container {
		padding-top: 115px;
		position: relative;
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		background: linear-gradient(170deg, #FFF5F8 0%, #FFF 60%);
	}
	
	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 40upx;
		border-radius: 20upx;
		box-shadow: 0 10upx 30upx rgba(255, 76, 124, 0.1);
		margin: 0 30upx;
	}
	
	.back-btn {
		position: absolute;
		left: 40upx;
		z-index: 9999;
		padding-top: var(--status-bar-height);
		top: 40upx;
		font-size: 40upx;
		color: #FF4C7C;
	}
	
	.left-top-sign {
		font-size: 120upx;
		color: #FF4C7C;
		position: relative;
		left: -16upx;
		font-weight: bold;
		opacity: 0.1;
		text-shadow: 0 5upx 10upx rgba(255, 76, 124, 0.3);
	}
	
	.right-top-sign {
		position: absolute;
		top: 80upx;
		right: -30upx;
		z-index: 95;
		width: 200upx;
		height: 200upx;
		border-radius: 50%;
		background: linear-gradient(145deg, #FF85A2 0%, #FF4C7C 80%);
		box-shadow: 0 10upx 30upx rgba(255, 76, 124, 0.2);
		opacity: 0.8;
	}
	
	.left-bottom-sign {
		position: absolute;
		left: -5upx;
		bottom: -40upx;
		z-index: 95;
		width: 140upx;
		height: 140upx;
		border-radius: 40upx;
		background: linear-gradient(45deg, #FF85A2 0%, #FF4C7C 80%);
		transform: rotate(45deg);
		box-shadow: 0 10upx 30upx rgba(255, 76, 124, 0.2);
		opacity: 0.8;
	}
	
	.welcome {
		position: relative;
		left: 50upx;
		top: -90upx;
		font-size: 46upx;
		color: #FF4C7C;
		text-shadow: 1px 0px 1px rgba(255, 76, 124, 0.3);
		font-weight: bold;
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
		background: #FFF5F8;
		height: 120upx;
		border-radius: 10upx;
		margin-bottom: 50upx;
		box-shadow: 0 4upx 16upx rgba(255, 76, 124, 0.08);
		transition: all 0.3s;
		
		&:hover {
			box-shadow: 0 8upx 20upx rgba(255, 76, 124, 0.15);
		}
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.tit {
			height: 50upx;
			line-height: 56upx;
			font-size: 30upx;
			color: #606266;
		}
		
		input {
			height: 60upx;
			font-size: 30upx;
			color: #303133;
			width: 100%;
		}
	}
	
	.confirm-btn {
		width: 570upx;
		height: 100upx;
		line-height: 100upx;
		border-radius: 50upx;
		margin: 70upx auto 40upx;
		font-size: 32upx;
		color: #fff;
		background: linear-gradient(to right, #FF4C7C, #FF85A2);
		box-shadow: 0 8upx 20upx rgba(255, 76, 124, 0.3);
		transition: all 0.3s;
		
		&:active {
			transform: translateY(3upx);
			box-shadow: 0 4upx 10upx rgba(255, 76, 124, 0.3);
		}
	}
	
	.confirm-btn2 {
		width: 570upx;
		height: 100upx;
		line-height: 100upx;
		border-radius: 50upx;
		margin: 70upx auto;
		font-size: 32upx;
		color: #FF4C7C;
		background: transparent;
		border: 1px solid #FF4C7C;
		box-shadow: 0 4upx 10upx rgba(255, 76, 124, 0.1);
		transition: all 0.3s;
		
		&:active {
			transform: translateY(3upx);
			background: rgba(255, 76, 124, 0.05);
		}
	}
	
	.forget-section {
		font-size: 28upx;
		color: #FF4C7C;
		text-align: center;
		margin-top: 40upx;
	}
	
	.register-section {
		position: absolute;
		left: 0;
		bottom: 50upx;
		width: 100%;
		font-size: 28upx;
		color: #606266;
		text-align: center;
		
		text {
			color: #FF4C7C;
			margin-left: 10upx;
			font-weight: bold;
		}
	}
</style>
