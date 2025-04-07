<template>
	<view class="app">
		<view class="price-box">
			<text>支付金额</text>
			<text class="price">{{orderInfo.payAmount}}</text>
		</view>

		<view class="pay-type-list">
			<view class="type-item b-b" @click="changePayType(1)">
				<text class="icon yticon icon-alipay"></text>
				<view class="con">
					<text class="tit">支付宝支付</text>
					<text>推荐使用支付宝支付</text>
				</view>
				<label class="radio">
					<radio value="" color="#fa436a" :checked='payType == 1' />
				</label>
			</view>
			<view class="type-item b-b" @click="changePayType(2)">
				<text class="icon yticon icon-weixinzhifu"></text>
				<view class="con">
					<text class="tit">微信支付</text>
				</view>
				<label class="radio">
					<radio value="" color="#fa436a" :checked='payType == 2' />
				</label>
			</view>
		</view>

		<text class="mix-btn" @click="confirm">确认支付</text>
	</view>
</template>

<script>
	import {
		fetchOrderDetail,
		payOrderSuccess
	} from '@/api/order.js';
	import { API_BASE_URL, USE_ALIPAY } from '@/utils/appConfig.js';
	export default {
		data() {
			return {
				orderId: null,
				payType: 1,
				orderInfo: {}
			};
		},
		onLoad(options) {
			this.orderId = options.orderId;
			fetchOrderDetail(this.orderId).then(response => {
				this.orderInfo = response.data;
			});
		},
		methods: {
			//选择支付方式
			changePayType(type) {
				this.payType = type;
			},
			//确认支付
			confirm: async function() {
				if(USE_ALIPAY){
					if(this.payType!=1){
						uni.showToast({
							title:"暂不支持微信支付！",
							icon:"none"
						})
						return;
					}
					window.location.href = API_BASE_URL+"/alipay/webPay?outTradeNo=" + this.orderInfo.orderSn + "&subject=" + this.orderInfo.receiverName + "的商品订单" + "&totalAmount=" + this.orderInfo.totalAmount
				}else{
					// 在非支付宝模式下，显示正在模拟支付的提示
					uni.showLoading({
						title: '正在模拟支付...'
					});
					
					// 处理请求数据
					const payData = {
						orderId: this.orderId,
						payType: this.payType
					};
					
					console.log('支付请求数据:', JSON.stringify(payData));
					
					try {
						// 尝试调用后端支付接口
						const response = await payOrderSuccess(payData);
						console.log('支付响应:', response);
						
						uni.hideLoading();
						uni.showToast({
							title: '支付成功',
							icon: 'success',
							duration: 1500
						});
						
						// 延迟一下再跳转，让用户看到成功提示
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/money/paySuccess'
							});
						}, 1500);
					} catch (error) {
						console.error('支付失败:', error);
						uni.hideLoading();
						uni.showToast({
							title: '支付失败',
							icon: 'none',
							duration: 1500
						});
					}
				}
			},
		}
	}
</script>

<style lang='scss'>
	.app {
		width: 100%;
	}

	.price-box {
		background-color: #fff;
		height: 265upx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		font-size: 28upx;
		color: #909399;

		.price {
			font-size: 50upx;
			color: #303133;
			margin-top: 12upx;

			&:before {
				content: '￥';
				font-size: 40upx;
			}
		}
	}

	.pay-type-list {
		margin-top: 20upx;
		background-color: #fff;
		padding-left: 60upx;

		.type-item {
			height: 120upx;
			padding: 20upx 0;
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding-right: 60upx;
			font-size: 30upx;
			position: relative;
		}

		.icon {
			width: 100upx;
			font-size: 52upx;
		}

		.icon-erjiye-yucunkuan {
			color: #fe8e2e;
		}

		.icon-weixinzhifu {
			color: #36cb59;
		}

		.icon-alipay {
			color: #01aaef;
		}

		.tit {
			font-size: $font-lg;
			color: $font-color-dark;
			margin-bottom: 4upx;
		}

		.con {
			flex: 1;
			display: flex;
			flex-direction: column;
			font-size: $font-sm;
			color: $font-color-light;
		}
	}

	.mix-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 630upx;
		height: 80upx;
		margin: 80upx auto 30upx;
		font-size: $font-lg;
		color: #fff;
		background-color: $base-color;
		border-radius: 10upx;
		box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
	}
</style>
