<template>
	<view class="container">
		<view class="app-info">
			<image class="logo" src="/static/logo.png" mode="aspectFit"></image>
			<text class="app-name">WhaleTide App</text>
			<text class="version">版本 1.0.0</text>
		</view>
		
		<view class="info-section">
			<view class="section-title">应用信息</view>
			<view class="list-cell">
				<text class="cell-tit">应用名称</text>
				<text class="cell-tip">WhaleTide App</text>
			</view>
			<view class="list-cell">
				<text class="cell-tit">应用类型</text>
				<text class="cell-tip">商城购物平台</text>
			</view>
			<view class="list-cell">
				<text class="cell-tit">技术支持</text>
				<text class="cell-tip">WhaleTide 技术团队</text>
			</view>
		</view>
		
		<view class="info-section">
			<view class="section-title">联系我们</view>
			<view class="list-cell">
				<text class="cell-tit">官方网站</text>
				<text class="cell-tip">www.whaletide.com</text>
			</view>
			<view class="list-cell">
				<text class="cell-tit">客服电话</text>
				<text class="cell-tip">400-123-4567</text>
			</view>
			<view class="list-cell">
				<text class="cell-tit">电子邮箱</text>
				<text class="cell-tip">support@whaletide.com</text>
			</view>
		</view>
		
		<view class="copyright">
			<text>Copyright © 2024 WhaleTide All Rights Reserved</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			};
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	page {
		background: $page-color-base;
	}
	
	.container {
		padding: 30upx;
	}
	
	.app-info {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 50upx 0;
		
		.logo {
			width: 180upx;
			height: 180upx;
			margin-bottom: 20upx;
		}
		
		.app-name {
			font-size: 36upx;
			font-weight: bold;
			color: $font-color-dark;
			margin-bottom: 10upx;
		}
		
		.version {
			font-size: 28upx;
			color: $font-color-light;
		}
	}
	
	.info-section {
		background: #fff;
		border-radius: 10upx;
		margin-bottom: 30upx;
		padding: 20upx 0;
		
		.section-title {
			font-size: 30upx;
			font-weight: bold;
			color: $font-color-dark;
			padding: 10upx 30upx 20upx;
			border-bottom: 1upx solid #f5f5f5;
		}
	}
	
	.list-cell {
		display: flex;
		align-items: center;
		padding: 20upx 30upx;
		
		.cell-tit {
			width: 180upx;
			font-size: 28upx;
			color: $font-color-dark;
		}
		
		.cell-tip {
			flex: 1;
			font-size: 28upx;
			color: $font-color-light;
			text-align: right;
		}
	}
	
	.copyright {
		text-align: center;
		font-size: 24upx;
		color: $font-color-light;
		padding: 30upx 0;
	}
</style> 