<template>
	<view class="content">
		<scroll-view scroll-y class="left-aside">
			<view v-for="item in flist" :key="item.id" class="f-item b-b" :class="{active: item.id === currentId}" @click="tabtap(item)">
				{{item.name}}
			</view>
		</scroll-view>
		<scroll-view scroll-with-animation scroll-y class="right-aside">
			<view class="s-list">
				<view @click="navToList(item.id)" class="s-item" v-for="item in slist" :key="item.id">
					<image :src="item.icon||'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20190519/default.png'"></image>
					<text>{{item.name}}</text>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import {
		fetchProductCateList
	} from '@/api/home.js';
	export default {
		data() {
			return {
				currentId: 0,
				flist: [],
				slist: []
			}
		},
		onLoad() {
			this.loadData();
		},
		methods: {
			async loadData() {
				fetchProductCateList(0).then(response => {
					console.log("一级分类原始数据:", response);
					
					// 处理不同的响应格式
					let categoryData = [];
					
					// 检查各种可能的响应格式
					if (Array.isArray(response)) {
						console.log("响应直接是数组");
						categoryData = response;
					} else if (response && response.data && Array.isArray(response.data)) {
						console.log("响应包含data数组");
						categoryData = response.data;
					} else if (response && response.list && Array.isArray(response.list)) {
						console.log("响应包含list数组");
						categoryData = response.list;
					} else {
						console.warn("未识别的响应格式:", response);
						uni.showToast({
							title: '获取分类失败',
							icon: 'none'
						});
						return;
					}
					
					// 更新一级分类列表
					this.flist = categoryData;
					
					// 如果有数据，加载二级分类
					if (this.flist.length > 0) {
						this.currentId = this.flist[0].id;
						this.loadSecondCategories(this.currentId);
					}
				}).catch(error => {
					console.error("加载一级分类失败:", error);
					uni.showToast({
						title: '获取分类失败',
						icon: 'none'
					});
				});
			},
			// 加载二级分类
			loadSecondCategories(parentId) {
				fetchProductCateList(parentId).then(response => {
					console.log("二级分类原始数据:", response);
					
					// 处理不同的响应格式
					let categoryData = [];
					
					// 检查各种可能的响应格式
					if (Array.isArray(response)) {
						console.log("响应直接是数组");
						categoryData = response;
					} else if (response && response.data && Array.isArray(response.data)) {
						console.log("响应包含data数组");
						categoryData = response.data;
					} else if (response && response.list && Array.isArray(response.list)) {
						console.log("响应包含list数组");
						categoryData = response.list;
					} else {
						console.warn("未识别的响应格式:", response);
						uni.showToast({
							title: '获取子分类失败',
							icon: 'none'
						});
						return;
					}
					
					// 更新二级分类列表
					this.slist = categoryData;
				}).catch(error => {
					console.error("加载二级分类失败:", error);
					uni.showToast({
						title: '获取子分类失败',
						icon: 'none'
					});
				});
			},
			//一级分类点击
			tabtap(item) {
				this.currentId = item.id;
				this.loadSecondCategories(this.currentId);
			},
			navToList(sid) {
				uni.navigateTo({
					url: `/pages/product/list?fid=${this.currentId}&sid=${sid}`
				})
			}
		}
	}
</script>

<style lang='scss'>
	page,
	.content {
		height: 100%;
		background-color: #f8f8f8;
	}

	.content {
		display: flex;
	}

	.left-aside {
		flex-shrink: 0;
		width: 200upx;
		height: 100%;
		background-color: #fff;
	}

	.f-item {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 100upx;
		font-size: 28upx;
		color: $font-color-base;
		position: relative;

		&.active {
			color: $base-color;
			background: #f8f8f8;

			&:before {
				content: '';
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				height: 36upx;
				width: 8upx;
				background-color: $base-color;
				border-radius: 0 4px 4px 0;
				opacity: .8;
			}
		}
	}

	.right-aside {
		flex: 1;
		overflow: hidden;
		padding-left: 20upx;
	}

	.s-list {
		margin-top: 20upx;
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		background: #fff;
		padding-top: 12upx;

		&:after {
			content: '';
			flex: 99;
			height: 0;
		}
	}

	.s-item {
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		width: 176upx;
		font-size: 26upx;
		color: #666;
		padding-bottom: 20upx;

		image {
			width: 140upx;
			height: 140upx;
		}
	}
</style>
