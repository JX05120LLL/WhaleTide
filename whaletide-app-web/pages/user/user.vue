<template>  
    <view class="user-page">  
		<!-- 艺术风格背景 -->
		<view class="brand-background">
			<view class="brand-logo">Whale Tide</view>
			<view class="brand-pattern"></view>
		</view>
		
		<!-- 用户信息盒子 -->
		<view class="user-info-box">
			<view class="portrait-box">
				<image class="portrait" :src="userInfo.icon || '/static/missing-face.png'"></image>
			</view>
			<view class="info-box">
				<text class="username">{{ userInfo.nickname || userInfo.userName || userInfo.name || userInfo.username || (userInfo.id ? '用户'+userInfo.id : '用户') }}</text>
			</view>
		</view>
		
		<!-- 会员卡 -->
		<view class="vip-card-box" @click="navTo('/pages/member/index')">
			<view class="vip-btn">
				立即开通
			</view>
			<view class="vip-content">
				<view class="vip-icon">黄金会员</view>
			</view>
		</view>
		
		<!-- 积分信息 -->
		<view class="stats-section">
			<view class="stats-item">
				<text class="stats-num">{{userInfo.integration || '暂无'}}</text>
				<text class="stats-title">积分</text>
			</view>
			<view class="stats-item">
				<text class="stats-num">{{userInfo.growth || '暂无'}}</text>
				<text class="stats-title">成长值</text>
			</view>
			<view class="stats-item">
				<text class="stats-num">{{couponCount || '暂无'}}</text>
				<text class="stats-title">优惠券</text>
			</view>
		</view>
		
		<!-- 订单区域 -->
		<view class="orders-section">
			<view class="section-title">全部订单</view>
			<view class="orders-flex">
				<view class="order-item" @click="navTo('/pages/order/order?state=0')">
					<text class="order-icon icon-list"></text>
					<text class="order-text">全部订单</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/order?state=1')">
					<text class="order-icon icon-pay"></text>
					<text class="order-text">待付款</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/order?state=2')">
					<text class="order-icon icon-package"></text>
					<text class="order-text">待收货</text>
				</view>
				<view class="order-item" @click="navTo('/pages/after-sale/index')">
					<text class="order-icon icon-return"></text>
					<text class="order-text">退款/售后</text>
				</view>
			</view>
		</view>
		
		<!-- 功能列表 -->
		<view class="function-list">
			<view class="function-item" @click="navTo('/pages/address/address')">
				<text class="function-icon location-icon"></text>
				<text class="function-text">地址管理</text>
				<text class="arrow-icon">›</text>
			</view>
			<view class="function-item" @click="navTo('/pages/user/readHistory')">
				<text class="function-icon footprint-icon"></text>
				<text class="function-text">我的足迹</text>
				<text class="arrow-icon">›</text>
			</view>
			<view class="function-item" @click="navTo('/pages/user/brandAttention')">
				<text class="function-icon follow-icon"></text>
				<text class="function-text">我的关注</text>
				<text class="arrow-icon">›</text>
			</view>
			<view class="function-item" @click="navTo('/pages/user/productCollection')">
				<text class="function-icon collect-icon"></text>
				<text class="function-text">我的收藏</text>
				<text class="arrow-icon">›</text>
			</view>
			<view class="function-item">
				<text class="function-icon evaluation-icon"></text>
				<text class="function-text">我的评价</text>
				<text class="arrow-icon">›</text>
			</view>
			<view class="function-item" @click="navTo('/pages/set/set')">
				<text class="function-icon setting-icon"></text>
				<text class="function-text">设置</text>
				<text class="arrow-icon">›</text>
			</view>
		</view>
		
		<!-- 底部导航 -->
		<view class="bottom-nav">
			<view class="nav-item">
				<view class="nav-icon home-icon"></view>
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item">
				<view class="nav-icon category-icon"></view>
				<text class="nav-text">分类</text>
			</view>
			<view class="nav-item">
				<view class="nav-icon cart-icon"></view>
				<text class="nav-text">购物车</text>
			</view>
			<view class="nav-item active">
				<view class="nav-icon user-icon"></view>
				<text class="nav-text">我的</text>
			</view>
		</view>
    </view>  
</template>  
<script>  
	import listCell from '@/components/mix-list-cell';
	import {
		fetchMemberCouponList
	} from '@/api/coupon.js';
	import {  
		mapState 
	} from 'vuex';
	import requestUtil from '@/utils/requestUtil';
	let startY = 0, moveY = 0, pageAtTop = true;
    export default {
		components: {
			listCell
		},
		data(){
			return {
				coverTransform: 'translateY(0px)',
				coverTransition: '0s',
				moving: false,
				userInfo: {},
				couponCount: null
			}
		},
		onLoad(){
			if(this.hasLogin){
				// 初始加载时获取用户信息
				this.getUserInfo();
			}
		},
		onShow(){
			if(this.hasLogin){
				try {
					// 获取用户信息
					this.getUserInfo();
					
					// 获取优惠券信息
					fetchMemberCouponList(0).then(response => {
						console.log('优惠券API响应:', response);
						
						// 处理不同的响应格式
						let coupons = null;
						
						if (!response) {
							console.error('优惠券API响应为空');
							return;
						}
						
						// 检查各种可能的响应格式
						if (response.data && Array.isArray(response.data)) {
							coupons = response.data;
						} else if (response.list && Array.isArray(response.list)) {
							coupons = response.list;
						} else if (response.records && Array.isArray(response.records)) {
							coupons = response.records;
						} else if (Array.isArray(response)) {
							coupons = response;
						} else if (response.data && response.data.list && Array.isArray(response.data.list)) {
							coupons = response.data.list;
						} else if (response.data && response.data.records && Array.isArray(response.data.records)) {
							coupons = response.data.records;
						} else {
							console.error('无法识别的优惠券数据格式:', response);
							return;
						}
						
						if (coupons && coupons.length > 0) {
							this.couponCount = coupons.length;
						} else {
							this.couponCount = 0;
						}
					}).catch(error => {
						console.error('获取优惠券列表失败:', error);
						this.couponCount = 0;
					});
				} catch (error) {
					console.error('获取用户信息失败:', error);
				}
			} else {
				this.couponCount = null;
			}
		},
		// #ifndef MP
		onNavigationBarButtonTap(e) {
			const index = e.index;
			if (index === 0) {
				this.navTo('/pages/set/set');
			}else if(index === 1){
				// #ifdef APP-PLUS
				const pages = getCurrentPages();
				const page = pages[pages.length - 1];
				const currentWebview = page.$getAppWebview();
				currentWebview.hideTitleNViewButtonRedDot({
					index
				});
				// #endif
				uni.navigateTo({
					url: '/pages/notice/notice'
				})
			}
		},
		// #endif
        computed: {
			...mapState(['hasLogin'])
		},
        methods: {
			/**
			 * 获取用户信息
			 */
			getUserInfo() {
				requestUtil({
					url: '/sso/info',
					method: 'GET'
				}).then(response => {
					if(response.code === 200 && response.data) {
						// 更新本地用户信息
						this.userInfo = response.data;
						console.log('API获取到的用户信息:', this.userInfo);
						
						// 更新用户的积分和成长值
						if(this.userInfo.integration !== undefined) {
							this.$set(this.userInfo, 'integration', this.userInfo.integration);
						}
						if(this.userInfo.growth !== undefined) {
							this.$set(this.userInfo, 'growth', this.userInfo.growth);
						}
					} else {
						console.error('获取用户信息失败:', response);
					}
				}).catch(error => {
					console.error('获取用户信息API错误:', error);
				});
			},
			/**
			 * 统一跳转接口,拦截未登录路由
			 */
			navTo(url){
				if(!this.hasLogin){
					url = '/pages/public/login';
				}
				uni.navigateTo({  
					url
				})  
			},
			
			/**
			 *  触摸开始
			 */
			coverTouchstart(e){
				if(pageAtTop === false){
					return;
				}
				this.coverTransition = 'transform .1s linear';
				startY = e.touches[0].clientY;
			},
			coverTouchmove(e){
				moveY = e.touches[0].clientY;
				let moveDistance = moveY - startY;
				if(moveDistance < 0){
					this.moving = false;
					return;
				}
				this.moving = true;
				if(moveDistance >= 80 && moveDistance < 100){
					moveDistance = 80;
				}
				
				if(moveDistance > 0 && moveDistance <= 80){
					this.coverTransform = `translateY(${moveDistance}px)`;
				}
			},
			coverTouchend(){
				if(this.moving === false){
					return;
				}
				this.moving = false;
				this.coverTransition = 'transform 0.3s cubic-bezier(.21,1.93,.53,.64)';
				this.coverTransform = 'translateY(0px)';
			},
			/**
			 * 获取用户名，按优先级返回昵称、用户名或默认用户
			 */
			getUserName() {
				// 检查用户信息是否已加载
				console.log('当前用户信息:', this.userInfo);
				
				// 防止用户信息为空或未定义
				if (!this.userInfo) return '用户';
				
				// 按优先级返回用户名
				if (this.userInfo.nickname) return this.userInfo.nickname;
				if (this.userInfo.username) return this.userInfo.username;
				if (this.userInfo.id) return '用户' + this.userInfo.id;
				
				// 默认返回
				return '用户';
			}
        }  
    }  
</script>  
<style lang='scss'>
	.user-page {
		width: 100%;
		min-height: 100vh;
		position: relative;
		background-color: #fff6f9;
		overflow: hidden;
	}

	.brand-background {
		position: relative;
		width: 100%;
		height: 240upx;
		background: linear-gradient(to right, #ff85a2, #ff5a8a);
		border-bottom-left-radius: 30% 20%;
		border-bottom-right-radius: 30% 20%;
		overflow: hidden;
		z-index: 1;
		
		.brand-logo {
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			font-family: 'Times New Roman', serif;
			font-size: 60upx;
			font-weight: bold;
			font-style: italic;
			color: white;
			letter-spacing: 4upx;
			text-shadow: 0 4upx 8upx rgba(0, 0, 0, 0.3);
			z-index: 3;
		}
		
		.brand-pattern {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			background-image: radial-gradient(circle at 70% 60%, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0.15) 30%, transparent 60%);
			z-index: 2;
			
			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 0;
				width: 100%;
				height: 40%;
				background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.2));
			}
		}
	}

	.user-info-box {
		position: relative;
		z-index: 10;
		background: #ffffff;
		border-radius: 16upx;
		margin: 30upx 30upx 0;
		padding: 24upx;
		display: flex;
		align-items: center;
		box-shadow: 0 4upx 12upx rgba(0, 0, 0, 0.05);
		
		.portrait-box {
			width: 84upx;
			height: 84upx;
			border-radius: 50%;
			overflow: hidden;
			margin-right: 20upx;
			border: 2upx solid #f5f5f5;
			
			.portrait {
				width: 100%;
				height: 100%;
			}
		}
		
		.info-box {
			flex: 1;
			
			.username {
				font-size: 28upx;
				color: #333;
				font-weight: 500;
			}
		}
	}

	.vip-card-box {
		margin: 20upx 30upx 0;
		position: relative;
		height: 90upx;
		overflow: hidden;
		border-radius: 16upx;
		background: linear-gradient(to right, #ffb199, #ff6b9b);
		display: flex;
		align-items: center;
		padding: 0 24upx;
		box-shadow: 0 4upx 12upx rgba(0, 0, 0, 0.05);
		
		.vip-btn {
			position: absolute;
			right: 24upx;
			top: 50%;
			transform: translateY(-50%);
			color: #fff;
			font-size: 24upx;
			background: rgba(255, 255, 255, 0.3);
			padding: 6upx 16upx;
			border-radius: 30upx;
		}
		
		.vip-content {
			display: flex;
			align-items: center;
		}
		
		.vip-icon {
			color: #fff;
			font-size: 28upx;
			&:before {
				content: "❤️";
				margin-right: 8upx;
			}
		}
		
		&:active {
			opacity: 0.9;
		}
	}

	.stats-section {
		display: flex;
		justify-content: space-between;
		margin: 30upx 30upx 0;
		background-color: #fff;
		padding: 30upx 0;
		border-radius: 16upx;
		box-shadow: 0 4upx 12upx rgba(0, 0, 0, 0.05);
		
		.stats-item {
			flex: 1;
			display: flex;
			flex-direction: column;
			align-items: center;
			
			.stats-num {
				color: #ff6b9b;
				font-size: 32upx;
				font-weight: bold;
				margin-bottom: 8upx;
			}
			
			.stats-title {
				color: #999;
				font-size: 28upx;
			}
		}
	}

	.orders-section {
		margin: 30upx 30upx 0;
		background-color: #fff;
		border-radius: 16upx;
		box-shadow: 0 4upx 12upx rgba(0, 0, 0, 0.05);
		overflow: hidden;
		
		.section-title {
			padding: 20upx 30upx;
			font-size: 28upx;
			color: #333;
			position: relative;
			border-bottom: 1px solid #f8f8f8;
		}
		
		.orders-flex {
			display: flex;
			justify-content: space-around;
			padding: 30upx 0;
		}
		
		.order-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 25%;
			position: relative;
			
			.order-icon {
				font-size: 52upx;
				color: #ff6b9b;
				margin-bottom: 10upx;
			}
			
			.order-text {
				font-size: 24upx;
				color: #333;
			}
			
			&:active {
				opacity: 0.7;
			}
		}
	}

	.function-list {
		margin: 30upx 30upx 120upx;
		background-color: #fff;
		border-radius: 16upx;
		box-shadow: 0 4upx 12upx rgba(0, 0, 0, 0.05);
		overflow: hidden;
		
		.function-item {
			display: flex;
			align-items: center;
			padding: 28upx 30upx;
			border-bottom: 1upx solid #f8f8f8;
			
			&:active {
				background-color: #fafafa;
			}
			
			&:last-child {
				border-bottom: none;
			}
			
			.function-icon {
				width: 40upx;
				text-align: center;
				margin-right: 20upx;
				font-size: 36upx;
			}
			
			.function-text {
				flex: 1;
				font-size: 28upx;
				color: #333;
			}
			
			.arrow-icon {
				font-size: 36upx;
				color: #ccc;
			}
			
			.location-icon {
				color: #ff5a8a;
			}
			
			.location-icon:before {
				content: "📍";
			}
			
			.footprint-icon {
				color: #5677fc;
			}
			
			.footprint-icon:before {
				content: "👣";
			}
			
			.follow-icon {
				color: #ff0000;
			}
			
			.follow-icon:before {
				content: "❤️";
			}
			
			.collect-icon {
				color: #ffb400;
			}
			
			.collect-icon:before {
				content: "⭐";
			}
			
			.evaluation-icon {
				color: #ff9c2b;
			}
			
			.evaluation-icon:before {
				content: "📝";
			}
			
			.setting-icon {
				color: #8a8a8a;
			}
			
			.setting-icon:before {
				content: "⚙️";
			}
		}
	}

	.bottom-nav {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		height: 100upx;
		background-color: #fff;
		display: flex;
		border-top: 1upx solid #eee;
		
		.nav-item {
			flex: 1;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			
			.nav-icon {
				font-size: 40upx;
				color: #999;
				margin-bottom: 4upx;
			}
			
			.nav-text {
				font-size: 24upx;
				color: #999;
			}
			
			&.active {
				.nav-icon, .nav-text {
					color: #ff6b9b;
				}
			}
			
			.home-icon:before {
				content: "🏠";
			}
			
			.category-icon:before {
				content: "📋";
			}
			
			.cart-icon:before {
				content: "🛒";
			}
			
			.user-icon:before {
				content: "👤";
			}
		}
	}

	.icon-list:before {
		content: "📃";
	}
	
	.icon-pay:before {
		content: "💰";
	}
	
	.icon-package:before {
		content: "📦";
	}
	
	.icon-return:before {
		content: "↩️";
	}
</style>