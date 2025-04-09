<template>
	<view class="content">
		<view class="search-box">
			<view class="input-box">
				<text class="iconfont icon-search"></text>
				<input 
					type="text" 
					:placeholder="placeholder" 
					@input="inputChange" 
					@confirm="search"
					confirm-type="search" 
					v-model="keyword">
				</input>
			</view>
			<view class="search-btn" @click="search">搜索</view>
		</view>
		
		<view class="search-history" v-if="historyList.length && !keyword">
			<view class="header">
				<text>搜索历史</text>
				<text class="clear-btn" @click="clearHistory">清除</text>
			</view>
			<view class="list">
				<view 
					class="item" 
					v-for="(item, index) in historyList" 
					:key="index"
					@click="searchByKeyword(item)"
				>
					{{item}}
				</view>
			</view>
		</view>
		
		<view class="search-keywords" v-if="!keyword">
			<view class="header">
				<text>热门搜索</text>
			</view>
			<view class="list">
				<view 
					class="item" 
					:class="hotSearchIndex === index ? 'active' : ''"
					v-for="(item, index) in hotSearchList" 
					:key="index"
					@click="searchByKeyword(item)"
				>
					{{item}}
				</view>
			</view>
		</view>
		
		<view class="search-suggestions" v-if="keyword && !isSearched">
			<view 
				class="item" 
				v-for="(item, index) in suggestList" 
				:key="index"
				@click="searchByKeyword(item.name)"
			>
				<text class="keyword">{{keyword}}</text>{{item.name.replace(keyword, '')}}
			</view>
		</view>
		
		<view class="search-results" v-if="searchResults.length">
			<view 
				class="product-card"
				v-for="(item, index) in searchResults" 
				:key="item.id"
				@click="navToDetailPage(item)"
			>
				<image class="product-image" :src="item.pic" mode="aspectFill"></image>
				<view class="product-info">
					<text class="product-name">{{item.name}}</text>
					<text class="product-price">¥{{item.price}}</text>
				</view>
			</view>
		</view>
		
		<view class="no-results" v-if="isSearched && !searchResults.length">
			<text>没有找到相关商品</text>
		</view>
		
		<view class="loading" v-if="loading">
			<uni-load-more status="loading" :content-text="loadingText"></uni-load-more>
		</view>
	</view>
</template>

<script>
	import {getSearchHistory, saveSearchHistory, clearSearchHistory} from '@/utils/storage';
	import {searchProducts, getHotKeywords, getSuggestions} from '@/api/product';
	
	export default {
		data() {
			return {
				placeholder: '请输入搜索关键词',
				keyword: '',
				historyList: [],
				hotSearchList: [],
				hotSearchIndex: 0,
				suggestList: [],
				searchResults: [],
				isSearched: false,
				loading: false,
				loadingText: {
					contentdown: "上拉显示更多",
					contentrefresh: "正在加载...",
					contentnomore: "没有更多数据了"
				}
			};
		},
		onLoad(options) {
			this.loadHistory();
			this.loadHotKeywords();
		},
		methods: {
			loadHistory() {
				this.historyList = getSearchHistory() || [];
			},
			async loadHotKeywords() {
				try {
					const res = await getHotKeywords();
					if (res.code === 200) {
						this.hotSearchList = res.data || [];
					}
				} catch (e) {
					console.error('获取热门搜索关键词失败', e);
				}
			},
			async inputChange(e) {
				const keyword = e.detail.value;
				this.keyword = keyword;
				this.isSearched = false;
				
				if(!keyword) {
					this.suggestList = [];
					return;
				}
				
				try {
					const res = await getSuggestions(keyword);
					if (res.code === 200) {
						this.suggestList = res.data || [];
					}
				} catch (e) {
					console.error('获取搜索建议失败', e);
					this.suggestList = [];
				}
			},
			async search() {
				if(!this.keyword) return;
				
				// 添加到搜索历史
				if(!this.historyList.includes(this.keyword)) {
					this.historyList.unshift(this.keyword);
					// 只保留10个历史记录
					if(this.historyList.length > 10) {
						this.historyList.pop();
					}
					saveSearchHistory(this.historyList);
				}
				
				this.loading = true;
				this.isSearched = true;
				
				try {
					const res = await searchProducts({
						keyword: this.keyword,
						pageNum: 1,
						pageSize: 20
					});
					
					if (res.code === 200) {
						this.searchResults = res.data.list || [];
					} else {
						this.searchResults = [];
						uni.showToast({
							title: res.message || '搜索失败',
							icon: 'none'
						});
					}
				} catch (e) {
					console.error('搜索商品失败', e);
					this.searchResults = [];
					uni.showToast({
						title: '搜索失败，请稍后重试',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			searchByKeyword(keyword) {
				this.keyword = keyword;
				this.search();
			},
			clearHistory() {
				uni.showModal({
					title: '提示',
					content: '是否清除搜索历史？',
					success: (res) => {
						if(res.confirm) {
							this.historyList = [];
							clearSearchHistory();
						}
					}
				})
			},
			navToDetailPage(item) {
				uni.navigateTo({
					url: `/pages/product/product?id=${item.id}`
				})
			}
		}
	}
</script>

<style lang="scss">
	.content {
		padding: 30rpx;
	}
	
	.search-box {
		display: flex;
		align-items: center;
		padding: 10rpx 0;
		margin-bottom: 30rpx;
		
		.input-box {
			display: flex;
			align-items: center;
			flex: 1;
			height: 70rpx;
			background: #f5f5f5;
			border-radius: 35rpx;
			padding: 0 30rpx;
			
			.icon-search {
				font-size: 34rpx;
				color: #909399;
				margin-right: 10rpx;
			}
			
			input {
				flex: 1;
				font-size: 28rpx;
				color: #303133;
			}
		}
		
		.search-btn {
			margin-left: 20rpx;
			height: 70rpx;
			line-height: 70rpx;
			font-size: 28rpx;
			color: #303133;
		}
	}
	
	.search-history, .search-keywords {
		margin-bottom: 30rpx;
		
		.header {
			display: flex;
			justify-content: space-between;
			height: 80rpx;
			line-height: 80rpx;
			font-size: 28rpx;
			color: #303133;
			
			.clear-btn {
				color: #909399;
			}
		}
		
		.list {
			display: flex;
			flex-wrap: wrap;
			
			.item {
				padding: 0 30rpx;
				height: 60rpx;
				line-height: 60rpx;
				background: #f5f5f5;
				margin-right: 20rpx;
				margin-bottom: 20rpx;
				border-radius: 30rpx;
				font-size: 26rpx;
				color: #606266;
				
				&.active {
					background: #e1f3d8;
					color: #67c23a;
				}
			}
		}
	}
	
	.search-suggestions {
		.item {
			height: 80rpx;
			line-height: 80rpx;
			font-size: 28rpx;
			color: #606266;
			border-bottom: 1px solid #f5f5f5;
			
			.keyword {
				color: #303133;
				font-weight: bold;
			}
		}
	}
	
	.search-results {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		
		.product-card {
			width: 48%;
			margin-bottom: 30rpx;
			background: #fff;
			border-radius: 12rpx;
			overflow: hidden;
			box-shadow: 0 6rpx 8rpx rgba(0,0,0,0.05);
			
			.product-image {
				width: 100%;
				height: 300rpx;
			}
			
			.product-info {
				padding: 20rpx;
				
				.product-name {
					font-size: 26rpx;
					color: #303133;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					line-height: 1.4;
					margin-bottom: 10rpx;
				}
				
				.product-price {
					font-size: 30rpx;
					color: #fa436a;
					font-weight: bold;
				}
			}
		}
	}
	
	.no-results {
		padding: 100rpx 0;
		text-align: center;
		color: #909399;
		font-size: 28rpx;
	}
	
	.loading {
		padding: 20rpx 0;
		text-align: center;
	}
</style> 