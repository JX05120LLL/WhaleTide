<template>
	<view class="container">
		<view class="list-cell b-b m-t" @click="navTo('/pages/userinfo/userinfo')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">个人资料</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell b-b" @click="navTo('/pages/userinfo/change-password')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">修改密码</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell b-b" @click="navTo('/pages/address/address')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">收货地址</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell" @click="navTo('实名认证')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">实名认证</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		
		<view class="list-cell m-t">
			<text class="cell-tit">消息推送</text>
			<switch checked color="#fa436a" @change="switchChange" />
		</view>
		<view class="list-cell m-t b-b" @click="navTo('清除缓存')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">清除缓存</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell b-b" @click="navTo('/pages/about/about')" hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">关于whaletide-app-web</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell">
			<text class="cell-tit">检查更新</text>
			<text class="cell-tip">当前版本 1.0.0</text>
			<text class="cell-more yticon icon-you"></text>
		</view>
		<view class="list-cell log-out-btn" @click="toLogout">
			<text class="cell-tit">退出登录</text>
		</view>
	</view>
</template>

<script>
	import {  
	    mapMutations  
	} from 'vuex';
	export default {
		data() {
			return {
				
			};
		},
		methods:{
			...mapMutations(['logout']),

			navTo(url){
				if(url.indexOf("pages") != -1){
					uni.navigateTo({
						url: url
					});
				} else {
					// 对于非页面路径的情况，只显示提示信息
					this.$api.msg(`跳转到${url}`);
				}
			},
			navToOuter(url){
				// #ifdef H5
				window.location.href = url;
				// #endif
				// #ifndef H5
				uni.setClipboardData({
					data: url,
					success: () => {
						uni.showToast({
							title: '链接已复制',
							icon: 'none'
						});
					}
				});
				// #endif
			},
			//退出登录
			toLogout(){
				uni.showModal({
				    content: '确定要退出登录么',
				    success: (e)=>{
				    	if(e.confirm){
				    		// 调用后端登出接口
				    		uni.request({
				    			url: 'http://localhost:8085/sso/logout',
				    			method: 'POST',
				    			header: {
				    				'Authorization': uni.getStorageSync('FullAuthorization')
				    			},
				    			success: (res) => {
				    				// 清除所有存储的用户信息和token
				    				uni.removeStorageSync('Authorization');
				    				uni.removeStorageSync('FullAuthorization');
				    				uni.removeStorageSync('username');
				    				uni.removeStorageSync('password');
				    				// 清除其他可能存在的信息
				    				try {
				    					uni.clearStorageSync();
				    				} catch(e) {
				    					console.error('清除缓存失败', e);
				    				}
				    				
				    				// 调用vuex的登出操作
				    				this.logout();
				    				
				    				// 显示提示
				    				uni.showToast({
				    					title: '已退出登录',
				    					icon: 'success'
				    				});
				    				
				    				// 返回首页或登录页
				    				setTimeout(()=>{
				    					uni.reLaunch({
				    						url: '/pages/public/login'
				    					});
				    				}, 500);
				    			},
				    			fail: (err) => {
				    				console.error('登出失败:', err);
				    				// 即使后端登出失败，也执行本地登出操作
				    				uni.removeStorageSync('Authorization');
				    				uni.removeStorageSync('FullAuthorization');
				    				uni.removeStorageSync('username');
				    				uni.removeStorageSync('password');
				    				this.logout();
				    				uni.showToast({
				    					title: '已退出登录',
				    					icon: 'success'
				    				});
				    				setTimeout(()=>{
				    					uni.reLaunch({
				    						url: '/pages/public/login'
				    					});
				    				}, 500);
				    			}
				    		});
				    	}
				    }
				});
			},
			//switch
			switchChange(e){
				let statusTip = e.detail.value ? '打开': '关闭';
				this.$api.msg(`${statusTip}消息推送`);
			},

		}
	}
</script>

<style lang='scss'>
	page{
		background: $page-color-base;
	}
	.list-cell{
		display:flex;
		align-items:baseline;
		padding: 20upx $page-row-spacing;
		line-height:60upx;
		position:relative;
		background: #fff;
		justify-content: center;
		&.log-out-btn{
			margin-top: 40upx;
			.cell-tit{
				color: $uni-color-primary;
				text-align: center;
				margin-right: 0;
			}
		}
		&.cell-hover{
			background:#fafafa;
		}
		&.b-b:after{
			left: 30upx;
		}
		&.m-t{
			margin-top: 16upx; 
		}
		.cell-more{
			align-self: baseline;
			font-size:$font-lg;
			color:$font-color-light;
			margin-left:10upx;
		}
		.cell-tit{
			flex: 1;
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			margin-right:10upx;
		}
		.cell-tip{
			font-size: $font-base;
			color: $font-color-light;
		}
		switch{
			transform: translateX(16upx) scale(.84);
		}
	}
</style>
