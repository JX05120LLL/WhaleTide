<template>
	<view class="content">
		<image src="/static/hot_product_banner.png" class="banner-image"></image>
		<view class="section-tit">相关商品</view>
		<view class="goods-list">
			<view v-for="(item, index) in productList" :key="index" class="goods-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="item.pic" mode="aspectFit"></image>
				</view>
				<text class="title clamp">{{item.name}}</text>
				<text class="title2">{{item.subTitle}}</text>
				<view class="price-box">
					<text class="price">{{item.price}}</text>
					<text>已售 {{item.sale}}</text>
				</view>
			</view>
		</view>
		<uni-load-more :status="loadingType"></uni-load-more>

	</view>
</template>

<script>
	import {
		fetchHotProductList
	} from '@/api/home.js';
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import { getFullImageUrl } from '@/utils/requestUtil.js';
	import { API_BASE_URL } from '@/utils/appConfig.js';
	
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				apiBaseUrl: API_BASE_URL,
				loadingType: 'more', //加载更多状态
				productList: [],
				searchParam: {
					pageNum: 1,
					pageSize: 6
				}
			};
		},

		onLoad(options) {
			this.loadData();
		},
		//下拉刷新
		onPullDownRefresh() {
			this.loadData('refresh');
		},
		//加载更多
		onReachBottom() {
			this.searchParam.pageNum++;
			this.loadData();
		},
		methods: {
			//加载商品 ，带下拉刷新和上滑加载
			async loadData(type = 'add', loading) {
				//没有更多直接返回
				if (type === 'add') {
					if (this.loadingType === 'nomore') {
						return;
					}
					this.loadingType = 'loading';
				} else {
					this.loadingType = 'more'
				}

				if (type === 'refresh') {
					this.searchParam.pageNum=1;
					this.productList = [];
				}
				fetchHotProductList(this.searchParam).then(response => {
					console.log("热门产品列表响应:", response);
					
					// 处理分页格式响应
					let productList = [];
					
					// 检查常见的分页列表字段
					if (response && response.list && Array.isArray(response.list)) {
						console.log("使用list字段:", response.list.length);
						productList = response.list;
					} else if (response && response.records && Array.isArray(response.records)) {
						console.log("使用records字段:", response.records.length);
						productList = response.records;
					} else if (response && response.content && Array.isArray(response.content)) {
						console.log("使用content字段:", response.content.length);
						productList = response.content;
					} else if (response && Array.isArray(response)) {
						console.log("响应本身是数组:", response.length);
						productList = response;
					} else if (response && response.data && Array.isArray(response.data)) {
						console.log("使用data字段:", response.data.length);
						productList = response.data;
					} else {
						console.error("无法识别的响应格式:", response);
						productList = [];
					}
					
					// 处理图片URL
					if (productList.length > 0) {
						productList = productList.map(item => {
							if (item) {
								// 确保图片URL是完整的
								if (item.pic) {
									item.pic = getFullImageUrl(item.pic);
								} else if (item.mainImage) {
									item.pic = getFullImageUrl(item.mainImage);
								} else {
									// 如果没有图片，使用默认图片
									item.pic = '/static/temp/product.jpg';
								}
							}
							return item;
						});
					}
					
					if (productList.length === 0) {
						//没有更多了
						this.loadingType = 'nomore';
						this.searchParam.pageNum--;
					} else {
						if (productList.length < this.searchParam.pageSize) {
							this.loadingType = 'nomore';
							this.searchParam.pageNum--;
						} else {
							this.loadingType = 'more';
						}
						this.productList = this.productList.concat(productList);
					}
					
					if (type === 'refresh') {
						if (loading == 1) {
							uni.hideLoading()
						} else {
							uni.stopPullDownRefresh();
						}
					}
				}).catch(error => {
					console.error("加载热门产品失败:", error);
					this.loadingType = 'more';
					if (type === 'refresh') {
						uni.stopPullDownRefresh();
					}
				});
			},
			//详情
			navToDetailPage(item) {
				let id = item.id;
				uni.navigateTo({
					url: `/pages/product/product?id=${id}`
				})
			},
			stopPrevent() {}
		},
	}
</script>

<style lang="scss">
	page,
	.content {
		background: $page-color-base;
	}
	.banner-image{
		width: 100%;
	}
	.section-tit {
		font-size: $font-base+2upx;
		color: $font-color-dark;
		background: #fff;
		margin-top: 16upx;
		text-align: center;
		padding-top: 20upx;
		padding-bottom: 20upx;
	}
	/* 商品列表 */
	.goods-list {
		display: flex;
		flex-wrap: wrap;
		padding: 0 30upx;
		background: #fff;

		.goods-item {
			display: flex;
			flex-direction: column;
			width: 48%;
			padding-bottom: 40upx;

			&:nth-child(2n+1) {
				margin-right: 4%;
			}
		}

		.image-wrapper {
			width: 100%;
			height: 330upx;
			border-radius: 3px;
			overflow: hidden;
			background-color: #fff;

			image {
				width: 100%;
				height: 100%;
				opacity: 1;
			}
		}

		.title {
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 80upx;
		}

		.title2 {
			font-size: $font-sm;
			color: $font-color-light;
			line-height: 40upx;
			height: 80upx;
			overflow: hidden;
			text-overflow: ellipsis;
			display: block;
		}

		.price-box {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding-right: 10upx;
			font-size: 24upx;
			color: $font-color-light;
		}

		.price {
			font-size: $font-lg;
			color: $uni-color-primary;
			line-height: 1;

			&:before {
				content: '￥';
				font-size: 26upx;
			}
		}
	}
</style>
