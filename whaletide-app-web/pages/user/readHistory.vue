<template>
	<view class="content">
		<!-- 空白页 -->
		<empty v-if="productList==null||productList.length === 0"></empty>
		<view class="hot-section">
			<view 
				v-for="(item, index) in productList" 
				:key="index" 
				class="product-item" 
				hover-class="item-hover"
				@click="navToProductDetail(item)"
			>
				<!-- 商品图片占位符 -->
				<view class="product-image">
					<text class="yticon icon-shop"></text>
				</view>
				<view class="txt">
					<text class="title">{{item.productName}}</text>
					<view class="hor-txt">
						<text class="view-time">浏览时间: {{item.lastBrowseTime | formatDateTime}}</text>
					</view>
				</view>
				<text class="arrow-right yticon icon-you"></text>
			</view>
		</view>
		<uni-load-more :status="loadingType"></uni-load-more>
	</view>
</template>

<script>
	import empty from "@/components/empty";
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import {
		formatDate
	} from '@/utils/date';
	import {
		fetchReadHistoryList,
		clearReadHistory
	} from '@/api/memberReadHistory.js';
	export default {
		components: {
			uniLoadMore,
			empty
		},
		data() {
			return {
				loadingType: 'more',
				productList: [],
				browseParam: {
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
			this.browseParam.pageNum++;
			this.loadData();
		},
		// #ifndef MP
		onNavigationBarButtonTap(e) {
			const index = e.index;
			let thisObj = this;
			if (index === 0) {
				uni.showModal({
				    title: '提示',
				    content: '是否要清空所有浏览记录？',
				    success: function (res) {
				        if (res.confirm) {
				            clearReadHistory().then(response=>{
								thisObj.loadData('refresh');
							});
				        }
				    }
				});
			}
		},
		// #endif
		filters: {
			formatDateTime(time) {
				if (time == null || time === '') {
					return 'N/A';
				}
				let date = new Date(time);
				return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
			},
		},
		methods: {
			//加载商品浏览历史记录
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
					this.browseParam.pageNum = 1;
					this.productList = [];
				}
				fetchReadHistoryList(this.browseParam).then(response => {
					let dataList = response.data.list;
					if (dataList.length === 0) {
						//没有更多了
						this.loadingType = 'nomore';
						this.browseParam.pageNum--;
					} else {
						if (dataList.length < this.browseParam.pageSize) {
							this.loadingType = 'nomore';
							this.browseParam.pageNum--;
						} else {
							this.loadingType = 'more';
						}
						this.productList = this.productList.concat(dataList);
					}
					if (type === 'refresh') {
						if (loading == 1) {
							uni.hideLoading()
						} else {
							uni.stopPullDownRefresh();
						}
					}
				});
			},
			//跳转到商品详情页面
			navToProductDetail(item) {
				console.log('点击商品浏览记录:', item);
				
				// 检查关键词是否存在
				if (!item || !item.productId) {
					uni.showToast({
						title: '商品信息不存在',
						icon: 'none'
					});
					return;
				}
				
				// 使用item.id作为商品ID - 这里假设id字段可以直接用作商品ID
				// 如果不行，可能需要后端支持直接返回productId字段
				let productId = item.productId;
				
				console.log('跳转到商品详情，ID:', productId);
				
				// 直接跳转到商品详情页
				uni.navigateTo({
					url: `/pages/product/product?id=${productId}`,
					fail: (err) => {
						console.error('导航失败:', err);
						uni.showToast({
							title: '页面跳转失败',
							icon: 'none'
						});
					}
				});
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

	.hot-section {
		display: flex;
		flex-direction: column;
		padding: 0 30upx;
		margin-top: 16upx;
		background: #fff;

		.product-item {
			display: flex;
			flex-direction: row;
			align-items: center;
			width: 100%;
			padding: 30upx 0;
			border-bottom: 1px solid #f8f8f8;
			position: relative;
		}
		
		.item-hover {
			background-color: #f8f8f8;
		}

		.product-image {
			width: 100upx;
			height: 100upx;
			border-radius: 8upx;
			background-color: #f5f5f5;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-right: 20upx;
			
			.yticon {
				color: #ff8080;
				font-size: 50upx;
			}
		}

		.title {
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 50upx;
			margin-bottom: 10upx;
			font-weight: bold;
			width: 500upx;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}

		.view-time {
			font-size: $font-sm;
			color: $font-color-light;
		}

		.txt {
			flex: 1;
			display: flex;
			flex-direction: column;
		}
		
		.hor-txt {
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		.arrow-right {
			font-size: 32upx;
			color: #ccc;
			margin-right: 10upx;
		}
	}
</style>
