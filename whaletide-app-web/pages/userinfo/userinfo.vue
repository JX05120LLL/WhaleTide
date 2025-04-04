<template>
	<view>
		<view class="user-section">
			<image class="bg" src="/static/user-bg.jpg"></image>
			<view class="portrait-box">
				<image class="portrait" :src="userInfo.avatar || '/static/missing-face.png'" @click="chooseAvatar"></image>
				<text class="pt-upload-btn yticon icon-paizhao" @click="chooseAvatar"></text>
			</view>
		</view>
		
		<view class="form-section">
			<view class="form-item">
				<text class="label">昵称</text>
				<input type="text" v-model="formData.nickname" placeholder="请输入昵称" class="input"/>
			</view>
			<view class="form-item">
				<text class="label">手机号</text>
				<input type="number" v-model="formData.phone" placeholder="请输入手机号" class="input"/>
			</view>
			<view class="form-item">
				<text class="label">性别</text>
				<view class="input">
					<picker @change="bindGenderChange" :value="genderIndex" :range="genderArray">
						<view class="picker-view">
							{{genderArray[genderIndex]}}
							<text class="yticon icon-you"></text>
						</view>
					</picker>
				</view>
			</view>
			<view class="form-item">
				<text class="label">生日</text>
				<view class="input">
					<picker mode="date" @change="bindDateChange" :value="formData.birthday">
						<view class="picker-view">
							{{formData.birthday || '请选择出生日期'}}
							<text class="yticon icon-you"></text>
						</view>
					</picker>
				</view>
			</view>
			<view class="form-item">
				<text class="label">城市</text>
				<input type="text" v-model="formData.city" placeholder="请输入所在城市" class="input"/>
			</view>
			<view class="form-item">
				<text class="label">职业</text>
				<input type="text" v-model="formData.job" placeholder="请输入职业" class="input"/>
			</view>
		</view>
		
		<view class="action-section">
			<button class="save-btn" @click="updateInfo">保存资料</button>
			<button class="pwd-btn" @click="navToChangePassword">修改密码</button>
		</view>
	</view>
</template>

<script>
	import {  
	    mapState,  
	    mapMutations  
	} from 'vuex';
	import { updateMemberInfo, uploadAvatar } from '@/api/member.js';
	
	export default {
		data() {
			return {
				formData: {
					username: '',
					nickname: '',
					phone: '',
					gender: 0,
					birthday: '',
					city: '',
					job: ''
				},
				genderArray: ['保密', '男', '女'],
				genderIndex: 0
			};
		},
		computed:{
			...mapState(['userInfo']),
		},
		onLoad() {
			this.initFormData();
		},
		methods: {
			...mapMutations(['updateUserInfo']),
			
			// 初始化表单数据
			initFormData() {
				if(this.userInfo) {
					this.formData = {
						username: this.userInfo.username || '',
						nickname: this.userInfo.nickname || '',
						phone: this.userInfo.phone || '',
						gender: this.userInfo.gender || 0,
						birthday: this.userInfo.birthday || '',
						city: this.userInfo.city || '',
						job: this.userInfo.job || ''
					};
					this.genderIndex = this.formData.gender;
				}
			},
			
			// 性别选择
			bindGenderChange(e) {
				this.genderIndex = e.detail.value;
				this.formData.gender = Number(e.detail.value);
			},
			
			// 日期选择
			bindDateChange(e) {
				this.formData.birthday = e.detail.value;
			},
			
			// 选择头像
			chooseAvatar() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						const tempFilePath = res.tempFilePaths[0];
						// 上传头像
						this.uploadFile(tempFilePath);
					}
				});
			},
			
			// 上传文件
			uploadFile(filePath) {
				uni.showLoading({
					title: '上传中...'
				});
				
				// 这里应该是上传文件到服务器的逻辑
				// 实际项目中可能需要调用uni.uploadFile上传文件
				// 这里直接模拟上传成功，传递base64数据
				// 实际项目应根据后端接口调整
				
				uni.getFileSystemManager().readFile({
					filePath: filePath,
					encoding: 'base64',
					success: (res) => {
						let base64Data = 'data:image/jpeg;base64,' + res.data;
						// 调用上传头像接口
						uploadAvatar({
							avatar: base64Data
						}).then(response => {
							if(response.code === 200) {
								// 更新本地存储的用户信息
								let userInfo = this.userInfo;
								userInfo.avatar = response.data;
								this.updateUserInfo(userInfo);
								
								uni.showToast({
									title: '头像上传成功'
								});
							}
						}).catch(() => {
							uni.showToast({
								title: '头像上传失败',
								icon: 'none'
							});
						}).finally(() => {
							uni.hideLoading();
						});
					},
					fail: () => {
						uni.hideLoading();
						uni.showToast({
							title: '图片读取失败',
							icon: 'none'
						});
					}
				});
			},
			
			// 更新用户信息
			updateInfo() {
				// 表单验证
				if(!this.formData.nickname) {
					uni.showToast({
						title: '请输入昵称',
						icon: 'none'
					});
					return;
				}
				if(!this.formData.phone) {
					uni.showToast({
						title: '请输入手机号',
						icon: 'none'
					});
					return;
				}
				
				// 检查token是否为空或格式不正确
				const token = uni.getStorageSync('Authorization');
				if (!token || token.trim() === '') {
					// token为空，重新登录
					uni.showModal({
						title: '登录状态失效',
						content: '您的登录状态已失效，请重新登录',
						showCancel: false,
						success: function(res) {
							if (res.confirm) {
								// 清除存储
								uni.removeStorageSync('Authorization');
								uni.removeStorageSync('FullAuthorization');
								uni.navigateTo({
									url: '/pages/public/login'
								});
							}
						}
					});
					return;
				}
				
				console.log('开始更新用户信息，提交数据:', JSON.stringify(this.formData));
				console.log('当前token值:', token);
				
				// 检查token是否已包含Bearer前缀，如果没有则添加
				const fullAuth = token.startsWith('Bearer ') ? token : 'Bearer ' + token;
				uni.setStorageSync('FullAuthorization', fullAuth);
				
				// 确保原始token不包含Bearer前缀
				const rawToken = token.startsWith('Bearer ') ? token.substring(7) : token;
				uni.setStorageSync('Authorization', rawToken);
				
				uni.showLoading({
					title: '更新中...'
				});
				
				// 尝试重新登录获取新token
				const tryRelogin = () => {
					const username = uni.getStorageSync('username');
					const password = uni.getStorageSync('password');
					
					if (username && password) {
						uni.showLoading({
							title: '重新登录中...'
						});
						
						// 使用保存的用户名密码重新登录
						import('@/api/member.js').then(module => {
							module.memberLogin({
								username: username,
								password: password
							}).then(response => {
								if (response.code === 200) {
									// 重新登录成功，保存新token
									const token = response.data.token;
									const tokenHead = response.data.tokenHead || 'Bearer ';
									
									uni.setStorageSync('Authorization', token);
									uni.setStorageSync('FullAuthorization', tokenHead + token);
									
									uni.hideLoading();
									uni.showToast({
										title: '已重新登录，请再次提交',
										icon: 'none',
										duration: 2000
									});
								} else {
									uni.hideLoading();
									uni.showModal({
										title: '登录失败',
										content: '自动重新登录失败，请手动登录',
										showCancel: false,
										success: function(res) {
											if (res.confirm) {
												uni.navigateTo({
													url: '/pages/public/login'
												});
											}
										}
									});
								}
							}).catch(error => {
								uni.hideLoading();
								uni.showModal({
									title: '登录失败',
									content: '自动重新登录失败，请手动登录',
									showCancel: false,
									success: function(res) {
										if (res.confirm) {
											uni.navigateTo({
												url: '/pages/public/login'
											});
										}
									}
								});
							});
						});
					} else {
						// 没有保存用户名密码，直接跳登录页
						uni.showModal({
							title: '登录状态失效',
							content: '您的登录状态已失效，请重新登录',
							showCancel: false,
							success: function(res) {
								if (res.confirm) {
									uni.navigateTo({
										url: '/pages/public/login'
									});
								}
							}
						});
					}
				};
				
				updateMemberInfo(this.formData).then(response => {
					console.log('更新用户信息响应:', JSON.stringify(response));
					
					if(response.code === 200) {
						// 更新本地存储的用户信息
						let userInfo = this.userInfo;
						Object.assign(userInfo, this.formData);
						this.updateUserInfo(userInfo);
						
						uni.showToast({
							title: '更新成功'
						});
						
						// 延迟返回上一页
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
					}
					uni.hideLoading(); // 确保在成功情况下也关闭loading
				}).catch(error => {
					// 详细记录错误信息
					console.error('更新用户信息失败:', JSON.stringify(error));
					
					// 处理具体错误
					let errorMsg = '更新失败';
					let needRelogin = false;
					
					// 检查是否是token问题
					if (error.data && (error.data.code === 7003 || error.data.code === 500)) {
						if (error.data.message && error.data.message.includes("Token")) {
							errorMsg = '登录已过期: ' + error.data.message;
							needRelogin = true;
						} else {
							errorMsg = error.data.message || '更新失败';
						}
					}
					
					uni.showToast({
						title: errorMsg,
						icon: 'none',
						duration: 2000
					});
					
					if (needRelogin) {
						// 尝试自动重新登录
						setTimeout(() => {
							tryRelogin();
						}, 1000);
					}
					
					uni.hideLoading(); // 确保错误情况下关闭loading
				});
			},
			
			// 跳转到修改密码页面
			navToChangePassword() {
				uni.navigateTo({
					url: '/pages/userinfo/change-password'
				});
			}
		}
	}
</script>

<style lang="scss">
	page{
		background: $page-color-base;
	}
	.user-section{
		display:flex;
		align-items:center;
		justify-content: center;
		height: 300upx;
		padding: 40upx 30upx 0;
		position:relative;
		.bg{
			position:absolute;
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;
			filter: blur(1px);
			opacity: .7;
		}
		.portrait-box{
			width: 160upx;
			height: 160upx;
			border:6upx solid #fff;
			border-radius: 50%;
			position:relative;
			z-index: 2;
		}
		.portrait{
			position: relative;
			width: 100%;
			height: 100%;
			border-radius: 50%;
		}
		.pt-upload-btn{
			position:absolute;
			right: 0;
			bottom: 10upx;
			line-height: 1;
			z-index: 5;
			font-size: 40upx;
			color: #fff;
			padding: 4upx 6upx;
			border-radius: 6upx;
			background: rgba(0,0,0,.4);
		}
	}
	
	.form-section {
		background: #fff;
		padding: 20upx;
		margin-top: 16upx;
		
		.form-item {
			display: flex;
			align-items: center;
			padding: 20upx 0;
			border-bottom: 1upx solid #eee;
			
			&:last-child {
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
			
			.picker-view {
				width: 100%;
				display: flex;
				justify-content: space-between;
				align-items: center;
				
				.yticon {
					font-size: 40upx;
					color: #ccc;
				}
			}
		}
	}
	
	.action-section {
		padding: 30upx;
		
		.save-btn {
			background: $uni-color-primary;
			color: #fff;
			border-radius: 10upx;
			font-size: 32upx;
			padding: 20upx 0;
			margin-bottom: 30upx;
			text-align: center;
			display: flex;
			align-items: center;
			justify-content: center;
		}
		
		.pwd-btn {
			background: #fff;
			color: $uni-color-primary;
			border: 1upx solid $uni-color-primary;
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
