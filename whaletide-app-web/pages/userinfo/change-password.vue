<template>
	<view class="container">
		<view class="form-section">
			<view class="form-item">
				<text class="label">旧密码</text>
				<input 
					type="password" 
					v-model="formData.oldPassword" 
					placeholder="请输入当前密码" 
					class="input" 
					password
				/>
			</view>
			<view class="form-item">
				<text class="label">新密码</text>
				<input 
					type="password" 
					v-model="formData.newPassword" 
					placeholder="请输入新密码" 
					class="input" 
					password
				/>
			</view>
			<view class="form-item">
				<text class="label">确认密码</text>
				<input 
					type="password" 
					v-model="confirmPassword" 
					placeholder="请再次输入新密码" 
					class="input" 
					password
				/>
			</view>
		</view>
		
		<view class="action-section">
			<button class="confirm-btn" @click="changePassword">确认修改</button>
		</view>
	</view>
</template>

<script>
	import { updatePassword } from '@/api/member.js';
	
	export default {
		data() {
			return {
				formData: {
					oldPassword: '',
					newPassword: ''
				},
				confirmPassword: ''
			};
		},
		methods: {
			// 验证密码格式
			validatePassword(password) {
				// 不再验证密码格式，直接返回true
				return true;
			},
			
			// 修改密码
			changePassword() {
				// 表单验证
				if(!this.formData.oldPassword) {
					uni.showToast({
						title: '请输入当前密码',
						icon: 'none'
					});
					return;
				}
				if(!this.formData.newPassword) {
					uni.showToast({
						title: '请输入新密码',
						icon: 'none'
					});
					return;
				}
				if(!this.confirmPassword) {
					uni.showToast({
						title: '请再次输入新密码',
						icon: 'none'
					});
					return;
				}
				if(this.formData.newPassword !== this.confirmPassword) {
					uni.showToast({
						title: '两次输入的密码不一致',
						icon: 'none'
					});
					return;
				}
				
				uni.showLoading({
					title: '提交中...'
				});
				
				// 调用修改密码API
				updatePassword(this.formData).then(response => {
					if(response.code === 200) {
						uni.showToast({
							title: '密码修改成功，请重新登录'
						});
						
						// 延迟执行退出登录操作
						setTimeout(() => {
							// 退出登录，清除本地存储的信息
							this.$store.commit('logout');
							// 跳转到登录页
							uni.reLaunch({
								url: '/pages/public/login'
							});
						}, 1500);
					}
				}).catch(error => {
					let message = '修改失败';
					if(error.response && error.response.data && error.response.data.message) {
						message = error.response.data.message;
					}
					uni.showToast({
						title: message,
						icon: 'none'
					});
				}).finally(() => {
					uni.hideLoading();
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: $page-color-base;
	}
	
	.container {
		padding: 20upx;
	}
	
	.form-section {
		background: #fff;
		padding: 30upx;
		border-radius: 10upx;
		margin-top: 20upx;
		
		.form-item {
			display: flex;
			align-items: center;
			padding: 20upx 0;
			border-bottom: 1upx solid #eee;
			
			&:last-of-type {
				border-bottom: none;
			}
			
			.label {
				width: 180upx;
				font-size: 30upx;
				color: $font-color-dark;
			}
			
			.input {
				flex: 1;
				font-size: 30upx;
				color: $font-color-dark;
			}
		}
	}
	
	.action-section {
		padding: 30upx;
		
		.confirm-btn {
			background: $uni-color-primary;
			color: #fff;
			border-radius: 10upx;
			font-size: 32upx;
			padding: 20upx 0;
			text-align: center;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style> 