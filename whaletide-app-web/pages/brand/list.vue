<template>
	<view class="content">
		<image src="/static/recommend_brand_banner.png" class="banner-image"></image>
		<view class="section-tit">相关品牌</view>
		<view class="goods-list">
			<view v-for="(item, index) in brandList" :key="index" class="goods-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="item.logo" mode="aspectFit"></image>
				</view>
				<text class="title clamp">{{item.name}}</text>
				<text class="title2">商品数量：{{item.productCount}}</text>
			</view>
		</view>
		<uni-load-more :status="loadingType"></uni-load-more>
	</view>
</template>

<script>
	import {
		fetchBrandRecommendList
	} from '@/api/brand.js';
	import { getFullImageUrl } from '@/utils/requestUtil.js';
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				loadingType: 'more', //加载更多状态
				brandList: [],
				queryParams: {
					pageNum: 1,
					pageSize: 10
				},
				loading: false
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
			if (this.loadingType == 'nomore' || this.loading) {
				return;
			}
			this.queryParams.pageNum++;
			this.loadingType = 'loading';
			this.loadData();
		},
		methods: {
			//加载商品 ，带下拉刷新和上滑加载
			async loadData(type = 'add', loading) {
				this.loading = true;
				try {
					const response = await fetchBrandRecommendList(this.queryParams);
					console.log("品牌列表原始数据:", response);
					
					// 处理图片路径
					const brands = response.data && response.data.list ? response.data.list : [];
					brands.forEach(item => {
						// 确保logo是完整URL
						if (item.logo) {
							item.logo = getFullImageUrl(item.logo);
						}
						console.log("处理后的品牌图片:", item.logo);
					});
					
					// 添加到列表
					if (this.queryParams.pageNum === 1) {
						this.brandList = brands;
					} else {
						this.brandList = [...this.brandList, ...brands];
					}
					
					this.loadingType = brands.length < this.queryParams.pageSize ? 'nomore' : 'more';
					console.log("处理后的品牌列表:", this.brandList);
				} catch (error) {
					console.error("加载品牌列表失败:", error);
					uni.showToast({
						title: '加载品牌列表失败',
						icon: 'none'
					});
					this.loadingType = 'more';
				} finally {
					this.loading = false;
					if (type === 'refresh') {
						if (loading == 1) {
							uni.hideLoading()
						} else {
							uni.stopPullDownRefresh();
						}
					}
				}
			},
			//详情
			navToDetailPage(item) {
				let id = item.id;
				uni.navigateTo({
					url: `/pages/brand/brandDetail?id=${id}`
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
			height: 150upx;
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
		}
	}
</style>
