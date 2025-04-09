<template>
	<view class="container">
		<!-- 自定义导航栏 -->
		<view class="custom-navbar">
			<!-- 状态栏占位 -->
			<view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
			<!-- 自定义搜索框 -->
			<view class="custom-search-box" @click="navToSearchPage">
				<view class="search-input-box">
					<text class="search-icon">🔍</text>
					<text class="search-placeholder">请输入商品 如：手机</text>
				</view>
				<text class="message-icon" @click.stop="navToMessagePage">💬</text>
			</view>
		</view>
		
		<!-- 小程序头部兼容 -->
		<!-- #ifdef MP -->
		<view class="mp-search-box">
			<input class="ser-input" type="text" v-model="searchKeyword" placeholder="输入关键字搜索" @confirm="handleSearch" @click="navToSearchPage" />
		</view>
		<!-- #endif -->
		
		<!-- 头部轮播 -->
		<view class="carousel-section">
			<!-- 标题栏和状态栏占位符 -->
			<view class="titleNview-placing"></view>
			<!-- 背景色区域 -->
			<view class="titleNview-background" :style="{backgroundColor:titleNViewBackground}"></view>
			<swiper class="carousel" 
				circular 
				autoplay 
				interval="3000" 
				duration="500" 
				@change="swiperChange">
				<swiper-item v-for="(item, index) in advertiseList" :key="index" class="carousel-item" @click="navToAdvertisePage(item)">
					<image :src="item.pic" mode="aspectFit" />
				</swiper-item>
			</swiper>
			<!-- 自定义swiper指示器 -->
			<view class="swiper-dots">
				<text class="num">{{swiperCurrent+1}}</text>
				<text class="sign">/</text>
				<text class="num">{{swiperLength}}</text>
			</view>
		</view>
		<!-- 头部功能区 -->
		<view class="cate-section">
			<view class="cate-item">
				<image src="/static/temp/c3.png"></image>
				<text>专题</text>
			</view>
			<view class="cate-item">
				<image src="/static/temp/c5.png"></image>
				<text>话题</text>
			</view>
			<view class="cate-item">
				<image src="/static/temp/c6.png"></image>
				<text>优选</text>
			</view>
			<view class="cate-item">
				<image src="/static/temp/c7.png"></image>
				<text>特惠</text>
			</view>
		</view>

		<!-- 品牌制造商直供 -->
		<view class="f-header m-t" @click="navToRecommendBrandPage()">
			<image src="/static/icon_home_brand.png"></image>
			<view class="tit-box">
				<text class="tit">品牌制造商直供</text>
				<text class="tit2">工厂直达消费者，剔除品牌溢价</text>
			</view>
			<text class="yticon icon-you"></text>
		</view>

		<view class="guess-section">
			<view v-for="(item, index) in brandList" :key="index" class="guess-item" @click="navToBrandDetailPage(item)">
				<view class="image-wrapper-brand">
					<image :src="item.logo" mode="aspectFit"></image>
				</view>
				<text class="title clamp">{{item.name}}</text>
				<text class="title2">商品数量：{{item.productCount}}</text>
			</view>
		</view>

		<!-- 秒杀专区 -->
		<view class="f-header m-t" v-if="homeFlashPromotion!==null">
			<image src="/static/icon_flash_promotion.png"></image>
			<view class="tit-box">
				<text class="tit">秒杀专区</text>
				<text class="tit2">下一场 {{homeFlashPromotion.nextStartTime | formatTime}} 开始</text>
			</view>
			<view class="tit-box">
				<text class="tit2" style="text-align: right;">本场结束剩余：</text>
				<view style="text-align: right;">
					<text class="hour timer">{{cutDownTime.endHour}}</text>
					<text>:</text>
					<text class="minute timer">{{cutDownTime.endMinute}}</text>
					<text>:</text>
					<text class="second timer">{{cutDownTime.endSecond}}</text>
				</view>
			</view>
			<text class="yticon icon-you" v-show="false"></text>
		</view>

		<view class="guess-section">
			<view v-for="(item, index) in homeFlashPromotion.productList" :key="index" class="guess-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="item.pic" mode="aspectFill"></image>
				</view>
				<text class="title clamp">{{item.name}}</text>
				<text class="title2 clamp">{{item.subTitle}}</text>
				<text class="price">￥{{item.price}}</text>
			</view>
		</view>

		<!-- 新鲜好物 -->
		<view class="f-header m-t" @click="navToNewProudctListPage()">
			<image src="/static/icon_new_product.png"></image>
			<view class="tit-box">
				<text class="tit">新鲜好物</text>
				<text class="tit2">为你寻觅世间好物</text>
			</view>
			<text class="yticon icon-you"></text>
		</view>
		<view class="seckill-section">
			<scroll-view class="floor-list" scroll-x>
				<view class="scoll-wrapper">
					<view v-for="(item, index) in newProductList" :key="index" class="floor-item" @click="navToDetailPage(item)">
						<image :src="item.pic" mode="aspectFill"></image>
						<text class="title clamp">{{item.name}}</text>
						<text class="title2 clamp">{{item.subTitle}}</text>
						<text class="price">￥{{item.price}}</text>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 人气推荐楼层 -->
		<view class="f-header m-t" @click="navToHotProudctListPage()">
			<image src="/static/icon_hot_product.png"></image>
			<view class="tit-box">
				<text class="tit">人气推荐</text>
				<text class="tit2">大家都赞不绝口的</text>
			</view>
			<text class="yticon icon-you"></text>
		</view>

		<view class="hot-section">
			<view v-for="(item, index) in hotProductList" :key="index" class="guess-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="item.pic" mode="aspectFill"></image>
				</view>
				<view class="txt">
					<text class="title clamp">{{item.name}}</text>
					<text class="title2">{{item.subTitle}}</text>
					<text class="price">￥{{item.price}}</text>
				</view>
			</view>
		</view>

		<!-- 猜你喜欢-->
		<view class="f-header m-t">
			<image src="/static/icon_recommend_product.png"></image>
			<view class="tit-box">
				<text class="tit">猜你喜欢</text>
				<text class="tit2">你喜欢的都在这里了</text>
			</view>
			<text class="yticon icon-you" v-show="false"></text>
		</view>

		<view class="guess-section">
			<view v-for="(item, index) in recommendProductList" :key="index" class="guess-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="item.pic" mode="aspectFill" @error="handleImageError(item, index)"></image>
				</view>
				<text class="title clamp">{{item.name}}</text>
				<text class="title2 clamp">{{item.subTitle || ''}}</text>
				<text class="price">￥{{item.price}}</text>
			</view>
		</view>
		<uni-load-more :status="loadingType"></uni-load-more>
	</view>
</template>

<script>
	import {
		fetchContent,
		fetchRecommendProductList
	} from '@/api/home.js';
	import {
		formatDate
	} from '@/utils/date';
	import { getFullImageUrl, extractApiData } from '@/utils/requestUtil.js';
	import { API_BASE_URL } from '@/utils/appConfig.js';
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import { searchProductList } from '@/api/product.js';
	export default {
		components: {
			uniLoadMore	
		},
		data() {
			return {
				statusBarHeight: 0,
				apiBaseUrl: API_BASE_URL,
				titleNViewBackground: '',
				titleNViewBackgroundList: ['rgb(203, 87, 60)', 'rgb(205, 215, 218)'],
				swiperCurrent: 0,
				swiperLength: 0,
				carouselList: [],
				goodsList: [],
				advertiseList: [{
					id: 1,
					name: '广告1',
					pic: 'https://img14.360buyimg.com/n0/jfs/t1/183854/8/33432/254558/63fe2d27Fd5c97f68/d2134c38c30c9789.jpg'
				}, {
					id: 2,
					name: '广告2',
					pic: 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13-ultra6amf8/section01.png'
				}, {
					id: 3,
					name: '广告3',
					pic: 'https://consumer.huawei.com/content/dam/huawei-cbg-site/cn/mkt/plp/phones/series-products/p60-pro-white.png'
				}, {
					id: 4,
					name: '广告4',
					pic: 'https://www.apple.com.cn/v/iphone-15-pro/a/images/overview/welcome/hero_static__cj5vew245zki_large.jpg'
				}],
				brandList: [{
					id: 1, 
					name: '小米',
					logo: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/1a2e4ae612a2c071dd62602ec2583a0a.png',
					productCount: 10
				}],
				homeFlashPromotion: {
					nextStartTime: new Date().getTime(),
					endTime: new Date(new Date().getTime() + 3600000),
					productList: [{
						id: 1,
						name: '小米手机',
						subTitle: '骁龙888处理器',
						pic: 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png',
						price: 3999
					}]
				},
				newProductList: [{
					id: 2,
					name: '小米手机',
					subTitle: '骁龙8处理器 | 6000万像素',
					pic: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/0cf8b19bf9048c6f309e95647a7ffe97.jpg',
					price: 4999
				}, {
					id: 3,
					name: '华为P60 Pro',
					subTitle: '超感知徕卡影像 | XMAGE影像',
					pic: 'https://consumer.huawei.com/content/dam/huawei-cbg-site/cn/mkt/plp/phones/p60-pro-white.png',
					price: 6999
				}],
				hotProductList: [{
					id: 3,
					name: '热门手机',
					subTitle: '畅销产品',
					pic: 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png',
					price: 2999
				}],
				recommendProductList: [{
					id: 4,
					name: '推荐手机',
					subTitle: '为你推荐',
					pic: 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png',
					price: 1999
				}],
				recommendParams: {
					pageNum: 1,
					pageSize: 4
				},
				loadingType:'more',
				searchKeyword: ''
			};
		},
		onLoad() {
			// 获取状态栏高度
			this.statusBarHeight = uni.getSystemInfoSync().statusBarHeight;
			
			// 初始化轮播图长度
			this.swiperLength = this.advertiseList.length;
			
			this.loadData();
		},
		//下拉刷新
		onPullDownRefresh(){
			this.recommendParams.pageNum=1;
			this.loadData();
		},
		//加载更多
		onReachBottom(){
			this.recommendParams.pageNum++;
			this.loadingType = 'loading';
			fetchRecommendProductList(this.recommendParams).then(response => {
				console.log("加载更多响应:", response);
				
				// 检查如果响应是包含list字段的对象
				if (response && response.list) {
					const addProductList = response.list;
					if (addProductList.length === 0) {
						// 没有更多了
						this.recommendParams.pageNum--;
						this.loadingType = 'nomore';
					} else {
						// 处理图片
						const processedList = this.processImages(addProductList);
						this.recommendProductList = this.recommendProductList.concat(processedList);
						this.loadingType = 'more';
					}
				} else {
					// 没有数据或者响应格式不正确
					console.error("加载更多数据格式不正确:", response);
					this.recommendParams.pageNum--;
					this.loadingType = 'nomore';
				}
			}).catch(error => {
				console.error("加载更多失败:", error);
				this.recommendParams.pageNum--;
				this.loadingType = 'more';
			});
		},
		computed: {
			cutDownTime() {
				let endTime = new Date(this.homeFlashPromotion.endTime);
				let endDateTime = new Date();
				let startDateTime = new Date();
				endDateTime.setHours(endTime.getHours());
				endDateTime.setMinutes(endTime.getMinutes());
				endDateTime.setSeconds(endTime.getSeconds());
				let offsetTime = (endDateTime.getTime() - startDateTime.getTime());
				let endHour = Math.floor(offsetTime / (60 * 60 * 1000));
				let offsetMinute = offsetTime % (60 * 60 * 1000);
				let endMinute = Math.floor(offsetMinute / (60 * 1000));
				let offsetSecond = offsetTime % (60 * 1000);
				let endSecond = Math.floor(offsetSecond / 1000);
				return {
					endHour: endHour,
					endMinute: endMinute,
					endSecond: endSecond
				}
			}
		},
		filters: {
			formatTime(time) {
				if (time == null || time === '') {
					return 'N/A';
				}
				let date = new Date(time);
				return formatDate(date, 'hh:mm:ss')
			},
		},
		methods: {
			// 添加一个单独的图片处理函数，供其他方法调用
			processImages(items) {
				if (!items || !Array.isArray(items)) return [];
				
				console.log("处理前的图片数据:", JSON.stringify(items.slice(0, 1)));
				
				return items.map(item => {
					if (item) {
						// 处理图片路径 - 检查多种可能的字段名
						if (item.pic) {
							console.log("原始pic URL:", item.pic);
							item.pic = getFullImageUrl(item.pic);
							console.log("处理后pic URL:", item.pic);
						}
						else if (item.mainImage) {
							console.log("发现mainImage字段:", item.mainImage);
							// 如果存在mainImage但没有pic字段，将mainImage复制到pic
							item.pic = getFullImageUrl(item.mainImage);
							console.log("从mainImage生成pic:", item.pic);
						}
						else {
							// 如果既没有pic也没有mainImage，使用默认图片
							console.warn("商品缺少图片字段:", item.id, item.name);
							item.pic = '/static/temp/product.jpg';
						}
						
						if (item.logo) {
							item.logo = getFullImageUrl(item.logo);
						}
					}
					return item;
				});
			},
			/**
			 * 获取推荐产品列表
			 */
			async getRecommendProductList() {
				try {
					const response = await fetchRecommendProductList();
					console.log('首页推荐产品原始响应:', response);
					
					// 使用工具函数从响应中提取产品列表
					const productList = extractApiData(response, '推荐产品');
					
					if (productList.length === 0) {
						console.log('未找到推荐产品，使用默认数据');
						this.showDefaultRecommendProducts();
						return;
					}
					
					console.log('提取到的推荐产品数量:', productList.length);
					
					// 处理产品数据，确保图片URL是完整的
					const processedList = productList.map(item => {
						// 处理图片URL
						if (item.pic) {
							item.pic = getFullImageUrl(item.pic);
						} else if (item.mainImage) {
							item.pic = getFullImageUrl(item.mainImage);
						} else {
							item.pic = '/static/errorImage.jpg';
						}
						return item;
					});
					
					this.recommendProductList = processedList;
					console.log('处理后的推荐产品:', this.recommendProductList);
				} catch (error) {
					console.error('获取推荐产品失败:', error);
					this.showDefaultRecommendProducts();
				}
			},
			/**
			 * 显示默认推荐产品（网络错误或数据为空时）
			 */
			showDefaultRecommendProducts() {
				this.recommendProductList = [{
					id: 1,
					name: '默认产品',
					pic: '/static/errorImage.jpg',
					price: 0
				}];
			},
			/**
			 * 处理图片加载错误
			 */
			handleImageError(item, index) {
				console.error(`商品[${index}]图片加载失败:`, item.name, item.pic);
				// 设置默认图片
				this.$set(this.recommendProductList[index], 'pic', '/static/errorImage.jpg');
			},
			/**
			 * 处理可能的分页数据结构
			 * @param {Object|Array} data 可能是分页对象或数组的数据
			 * @param {Array} defaultData 默认数据
			 * @param {Function} processor 数据处理函数
			 * @returns {Array} 处理后的数据数组
			 */
			processPageableData(data, defaultData, processor) {
				if (!data) return defaultData;
				
				// 检查是否是对象而非数组
				if (typeof data === 'object' && !Array.isArray(data)) {
					// 检查常见的分页字段
					if (data.list && Array.isArray(data.list)) {
						console.log("使用分页list字段:", data.list.length);
						return data.list.length > 0 ? processor(data.list) : defaultData;
					} else if (data.records && Array.isArray(data.records)) {
						console.log("使用分页records字段:", data.records.length);
						return data.records.length > 0 ? processor(data.records) : defaultData;
					} else if (data.content && Array.isArray(data.content)) {
						console.log("使用分页content字段:", data.content.length);
						return data.content.length > 0 ? processor(data.content) : defaultData;
					} else if (data.data && Array.isArray(data.data)) {
						console.log("使用分页data字段:", data.data.length);
						return data.data.length > 0 ? processor(data.data) : defaultData;
					} else if (data.items && Array.isArray(data.items)) {
						console.log("使用分页items字段:", data.items.length);
						return data.items.length > 0 ? processor(data.items) : defaultData;
					}
					// 没有找到有效字段
					return defaultData;
				}
				
				// 数据本身是数组
				return Array.isArray(data) && data.length > 0 ? processor(data) : defaultData;
			},
			/**
			 * 加载数据
			 */
			async loadData() {
				// 记录初始模拟数据
				const defaultData = {
					advertiseList: this.advertiseList,
					brandList: this.brandList,
					homeFlashPromotion: this.homeFlashPromotion,
					newProductList: this.newProductList,
					hotProductList: this.hotProductList,
					recommendProductList: this.recommendProductList
				};
				
				// 可能的字段名映射，以适应不同的后端命名习惯
				const fieldMappings = {
					advertiseList: ['advertiseList', 'bannerList', 'carousel', 'sliders'],
					brandList: ['brandList', 'brands'],
					homeFlashPromotion: ['homeFlashPromotion', 'flashPromotion', 'seckill'],
					newProductList: ['newProductList', 'newProducts'],
					hotProductList: ['hotProductList', 'hotProducts', 'recommendProducts']
				};
				
				// 通用的字段提取函数，尝试不同的字段名
				const extractField = (data, fieldNames) => {
					if (!data) return null;
					
					for (const name of fieldNames) {
						if (data[name] !== undefined) {
							return data[name];
						}
					}
					return null;
				};
				
				// 处理图片URL的函数 - 使用新的方法
				const processImages = this.processImages;
				
				fetchContent().then(response => {
					console.log("首页内容完整响应(JSON):", JSON.stringify(response));
					
					// 检查响应是否为null/undefined
					if (!response) {
						console.error("首页响应为null或undefined，使用默认数据");
						this.setDefaultData(defaultData);
						uni.stopPullDownRefresh();
						return;
					}
					
					// 根据返回格式调整数据结构
					let responseData = response;
					// 兼容可能的响应格式: response, response.data, response.data.data
					if (response && response.data) {
						console.log("检测到response.data结构");
						responseData = response.data;
					}
					if (responseData && responseData.data) {
						console.log("检测到responseData.data结构");
						responseData = responseData.data;
					}
					
					// 检查响应数据是否正确
					console.log("处理后的响应数据(JSON):", JSON.stringify(responseData));
					
					// 尝试设置数据，如果数据不存在则使用默认数据
					if (responseData) {
						// 广告列表
						const advList = extractField(responseData, fieldMappings.advertiseList) || [];
						console.log("广告列表提取字段:", JSON.stringify(fieldMappings.advertiseList));
						console.log("广告列表(JSON):", JSON.stringify(advList));
						this.advertiseList = this.processPageableData(advList, defaultData.advertiseList, processImages);
						console.log("处理后的广告列表:", this.advertiseList.length);
						this.swiperLength = this.advertiseList.length;
						this.titleNViewBackground = this.titleNViewBackgroundList[0];
						
						// 品牌列表
						const brdList = extractField(responseData, fieldMappings.brandList) || [];
						console.log("品牌列表提取字段:", JSON.stringify(fieldMappings.brandList));
						console.log("品牌列表(JSON):", JSON.stringify(brdList));
						this.brandList = this.processPageableData(brdList, defaultData.brandList, processImages);
						console.log("处理后的品牌列表:", this.brandList.length);
						
						// 秒杀专区
						const flashPromo = extractField(responseData, fieldMappings.homeFlashPromotion);
						console.log("秒杀专区提取字段:", JSON.stringify(fieldMappings.homeFlashPromotion));
						console.log("秒杀专区(JSON):", JSON.stringify(flashPromo));
						this.homeFlashPromotion = flashPromo ? flashPromo : defaultData.homeFlashPromotion;
						if (this.homeFlashPromotion && this.homeFlashPromotion.productList) {
							this.homeFlashPromotion.productList = processImages(this.homeFlashPromotion.productList);
							console.log("处理后的秒杀产品列表:", this.homeFlashPromotion.productList.length);
						}
						
						// 新品列表
						const newList = extractField(responseData, fieldMappings.newProductList) || [];
						console.log("新品列表提取字段:", JSON.stringify(fieldMappings.newProductList));
						console.log("新品列表(JSON):", JSON.stringify(newList));
						this.newProductList = this.processPageableData(newList, defaultData.newProductList, processImages);
						console.log("处理后的新品列表:", this.newProductList.length);
						
						// 热门列表
						const hotList = extractField(responseData, fieldMappings.hotProductList) || [];
						console.log("热门列表提取字段:", JSON.stringify(fieldMappings.hotProductList));
						console.log("热门列表(JSON):", JSON.stringify(hotList));
						this.hotProductList = this.processPageableData(hotList, defaultData.hotProductList, processImages);
						console.log("处理后的热门列表:", this.hotProductList.length);
						
						// 加载推荐列表
						this.getRecommendProductList().then(() => {
							uni.stopPullDownRefresh();
						}).catch(error => {
							console.error("获取推荐商品失败:", error);
							this.showDefaultRecommendProducts();
							uni.stopPullDownRefresh();
						});
					} else {
						console.error("首页数据格式不正确，使用模拟数据:", responseData);
						this.setDefaultData(defaultData);
						uni.showToast({
							title: '使用本地数据',
							icon: 'none'
						});
						uni.stopPullDownRefresh();
					}
				}).catch(error => {
					console.error("加载首页数据失败，使用模拟数据:", error);
					this.setDefaultData(defaultData);
					uni.showToast({
						title: '使用本地数据',
						icon: 'none'
					});
					uni.stopPullDownRefresh();
				});
			},
			//轮播图切换修改背景色
			swiperChange(e) {
				const index = e.detail.current;
				this.swiperCurrent = index;
				let changeIndex = index % this.titleNViewBackgroundList.length;
				this.titleNViewBackground = this.titleNViewBackgroundList[changeIndex];
			},
			//商品详情页
			navToDetailPage(item) {
				let id = item.id;
				uni.navigateTo({
					url: `/pages/product/product?id=${id}`
				})
			},
			//广告详情页
			navToAdvertisePage(item) {
				let id = item.id;
				console.log("navToAdvertisePage",item)
			},
			//品牌详情页
			navToBrandDetailPage(item) {
				let id = item.id;
				uni.navigateTo({
					url: `/pages/brand/brandDetail?id=${id}`
				})
			},
			//推荐品牌列表页
			navToRecommendBrandPage() {
				uni.navigateTo({
					url: `/pages/brand/list`
				})
			},
			//新鲜好物列表页
			navToNewProudctListPage() {
				uni.navigateTo({
					url: `/pages/product/newProductList`
				})
			},
			//人气推荐列表页
			navToHotProudctListPage() {
				uni.navigateTo({
					url: `/pages/product/hotProductList`
				})
			},
			// 添加设置默认数据的方法
			setDefaultData(defaultData) {
				// 确保使用默认模拟数据
				this.advertiseList = defaultData.advertiseList;
				this.swiperLength = this.advertiseList.length;
				this.titleNViewBackground = this.titleNViewBackgroundList[0];
				
				this.brandList = defaultData.brandList;
				this.homeFlashPromotion = defaultData.homeFlashPromotion;
				this.newProductList = defaultData.newProductList;
				this.hotProductList = defaultData.hotProductList;
				this.recommendProductList = defaultData.recommendProductList;
				
				console.log("已设置所有默认数据");
			},
			// 跳转到搜索页面
			navToSearchPage() {
				uni.navigateTo({
					url: '/pages/product/search'
				});
			},
			// 跳转到消息页面
			navToMessagePage() {
				uni.navigateTo({
					url: '/pages/notice/notice'
				});
			},
			// 处理搜索
			handleSearch() {
				// 首页不再支持搜索，直接跳转到搜索页面
				this.navToSearchPage();
			},
		},
		// #ifndef MP
		//点击导航栏 buttons 时触发
		onNavigationBarButtonTap(e) {
			const index = e.index;
			if (index === 0) {
				this.$api.msg('点击了扫描');
			} else if (index === 1) {
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
		}
		// #endif
	}
</script>

<style lang="scss">
	/* #ifdef MP */
	.mp-search-box {
		position: absolute;
		left: 0;
		top: 30upx;
		z-index: 9999;
		width: 100%;
		padding: 0 80upx;

		.ser-input {
			flex: 1;
			height: 56upx;
			line-height: 56upx;
			text-align: center;
			font-size: 28upx;
			color: $font-color-base;
			border-radius: 20px;
			background: rgba(255, 255, 255, .6);
		}
	}

	page {
		.cate-section {
			position: relative;
			z-index: 5;
			border-radius: 16upx 16upx 0 0;
			margin-top: -20upx;
		}

		.carousel-section {
			padding: 0;

			.titleNview-placing {
				padding-top: 0;
				height: 0;
			}

			.carousel {
				.carousel-item {
					padding: 0;
				}
			}

			.swiper-dots {
				left: 45upx;
				bottom: 40upx;
			}
		}
	}

	/* #endif */

	/* 自定义导航栏样式 */
	.custom-navbar {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		z-index: 999;
		box-shadow: 0 2px 10px rgba(255, 76, 124, 0.2);
	}
	
	.status-bar {
		background: linear-gradient(to right, #FF4C7C, #FF85A2);
		width: 100%;
	}
	
	/* 自定义搜索框样式 */
	.custom-search-box {
		width: 100%;
		padding: 15upx 30upx;
		background: linear-gradient(to right, #FF4C7C, #FF85A2);
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	
	.search-input-box {
		flex: 1;
		height: 70upx;
		background-color: rgba(255, 255, 255, 0.9);
		border-radius: 35upx;
		display: flex;
		align-items: center;
		padding: 0 30upx;
		margin-right: 20upx;
		box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
		border: 1px solid rgba(255, 255, 255, 0.8);
	}
	
	.search-icon {
		font-size: 34upx;
		color: #FF4C7C;
		margin-right: 10upx;
	}
	
	.search-placeholder {
		font-size: 28upx;
		color: #999;
	}
	
	.message-icon {
		font-size: 40upx;
		color: #FFFFFF;
		text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
	}

	/* 内容区样式 */
	.container {
		padding-top: 120upx; /* 为导航栏预留空间 */
		background-color: #FFF5F8;
		min-height: 100vh;
		position: relative;
		
		&::before, &::after {
			content: '';
			position: absolute;
			width: 300upx;
			height: 300upx;
			border-radius: 50%;
			z-index: -1;
		}
		
		&::before {
			background: radial-gradient(rgba(255, 76, 124, 0.1), rgba(255, 76, 124, 0));
			top: 400upx;
			left: -100upx;
		}
		
		&::after {
			background: radial-gradient(rgba(255, 76, 124, 0.08), rgba(255, 76, 124, 0));
			bottom: 300upx;
			right: -100upx;
		}
	}

	page {
		background: #FFF5F8;
	}

	.m-t {
		margin-top: 16upx;
	}

	/* 头部 轮播图 */
	.carousel-section {
		position: relative;
		padding-top: 0;
		margin: 20upx;
		border-radius: 16upx;
		overflow: hidden;
		box-shadow: 0 8px 20px rgba(255, 76, 124, 0.15);

		.titleNview-placing {
			height: 0; /* 不再需要为系统导航栏预留空间 */
			padding-top: 0;
			box-sizing: content-box;
		}

		.titleNview-background {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 426upx;
			transition: .4s;
			background: linear-gradient(to right, #FF4C7C, #FF85A2) !important;
		}
	}

	.carousel {
		width: 100%;
		height: 400upx; /* 增加轮播图高度 */

		.carousel-item {
			width: 100%;
			height: 100%;
			padding: 0;
			overflow: hidden;
		}

		image {
			width: 100%;
			height: 100%;
			border-radius: 0;
		}
	}

	.swiper-dots {
		display: flex;
		position: absolute;
		left: 60upx;
		bottom: 15upx;
		width: 72upx;
		height: 36upx;
		background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAABkCAYAAADDhn8LAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTMyIDc5LjE1OTI4NCwgMjAxNi8wNC8xOS0xMzoxMzo0MCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTk4MzlBNjE0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTk4MzlBNjA0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0E3RUNERkE0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6Q0E3RUNERkI0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4Gh5BPAAACTUlEQVR42uzcQW7jQAwFUdN306l1uWwNww5kqdsmm6/2MwtVCp8CosQtP9vg/2+/gY+DRAMBgqnjIp2PaCxCLLldpPARRIiFj1yBbMV+cHZh9PURRLQNhY8kgWyL/WDtwujjI8hoE8rKLqb5CDJaRMJHokC6yKgSCR9JAukmokIknCQJpLOIrJFwMsBJELFcKHwM9BFkLBMKFxNcBCHlQ+FhoocgpVwwnv0Xn30QBJGMC0QcaBVJiAMiec/dcwKuL4j1QMsVCXFAJE4s4NQA3K/8Y6DzO4g40P7UcmIBJxbEesCKWBDg8wWxHrAiFgT4fEGsB/CwIhYE+AeBAAdPLOcV8HRmWRDAiQVcO7GcV8CLM8uCAE4sQCDAlHcQ7x+ABQEEAggEEAggEEAggEAAgQACASAQQCCAQACBAAIBBAIIBBAIIBBAIABe4e9iAe/xd7EAJxYgEGDeO4j3EODp/cOCAE4sYMyJ5cwCHs4rCwI4sYBxJ5YzC84rCwKcXxArAuthQYDzC2JF0H49LAhwYUGsCFqvx5EF2T07dMaJBetx4cRyaqFtHJ8EIhK0i8OJBQxcECuCVutxJhCRoE0cZwMRyRcFefa/ffZBVPogePihhyCnbBhcfMFFEFM+DD4m+ghSlgmDkwlOgpAl4+BkkJMgZdk4+EgaSCcpVX7bmY9kgXQQU+1TgE0c+QJZUUz1b2T4SBbIKmJW+3iMj2SBVBWz+leVfCQLpIqYbp8b85EskIxyfIOfK5Sf+wiCRJEsllQ+oqEkQfBxmD8BBgA5hVjXyrBNUQAAAABJRU5ErkJggg==);
		background-size: 100% 100%;

		.num {
			width: 36upx;
			height: 36upx;
			border-radius: 50px;
			font-size: 24upx;
			color: #fff;
			text-align: center;
			line-height: 36upx;
		}

		.sign {
			position: absolute;
			top: 0;
			left: 50%;
			line-height: 36upx;
			font-size: 12upx;
			color: #fff;
			transform: translateX(-50%);
		}
	}

	/* 分类 */
	.cate-section {
		display: flex;
		justify-content: space-around;
		align-items: center;
		flex-wrap: wrap;
		padding: 30upx 22upx;
		background: #fff;
		margin: 20upx;
		border-radius: 16upx;
		box-shadow: 0 8px 16upx rgba(255, 76, 124, 0.12);

		.cate-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			font-size: $font-sm + 2upx;
			color: $font-color-dark;
		}

		image {
			width: 90upx;
			height: 90upx;
			margin-bottom: 14upx;
			border-radius: 50%;
			opacity: .9;
			box-shadow: 0 6upx 16upx rgba(255, 76, 124, 0.35);
			transition: all 0.3s;
			border: 2px solid rgba(255, 76, 124, 0.1);
			
			&:hover {
				transform: translateY(-5upx) scale(1.05);
				box-shadow: 0 10upx 26upx rgba(255, 76, 124, 0.5);
			}
		}
	}

	.ad-1 {
		width: 100%;
		height: 210upx;
		padding: 10upx 0;
		background: #fff;

		image {
			width: 100%;
			height: 100%;
		}
	}

	/* 秒杀专区 */
	.seckill-section {
		padding: 20upx;
		background: #fff;
		border-radius: 16upx;
		box-shadow: 0 8upx 20upx rgba(255, 76, 124, 0.12);
		margin: 20upx;
		
		.bd {
			width: 100%;
			white-space: nowrap;
		}
		
		.floor-list {
			display: flex;
			flex-wrap: wrap;
			justify-content: center;
			padding: 10upx 0;
		}
		
		.scoll-wrapper {
			padding: 10upx 0;
			display: flex;
			align-items: flex-start;
			
			.floor-item {
				width: 300upx;
				height: 430upx;
				margin-left: 30upx;
				display: flex;
				flex-direction: column;
				padding: 15upx;
				border-radius: 16upx;
				background: #ffffff;
				box-shadow: 0 8upx 16upx rgba(255, 76, 124, 0.08);
				transition: all 0.3s;
				border: 1px solid rgba(255, 76, 124, 0.05);
				
				&:hover {
					box-shadow: 0 12upx 24upx rgba(255, 76, 124, 0.2);
					transform: translateY(-8upx);
					border-color: rgba(255, 76, 124, 0.1);
				}
				
				image {
					width: 270upx;
					height: 270upx;
					border-radius: 12upx;
					object-fit: cover;
				}
				
				.price {
					color: #FF4C7C;
					font-weight: bold;
					padding: 10upx 20upx;
					font-size: 32upx;
					background: linear-gradient(to right, #FF4C7C, #FF85A2);
					-webkit-background-clip: text;
					-webkit-text-fill-color: transparent;
				}
				
				.title {
					padding: 10upx 20upx 0;
					font-size: 28upx;
					font-weight: bold;
				}
				
				.title2 {
					font-size: $font-sm;
					color: $font-color-light;
					line-height: 40upx;
					padding: 0 20upx 20upx;
				}
			}
		}
	}

	/* 通用标题栏样式 */
	.f-header {
		display: flex;
		align-items: center;
		height: 120upx;
		padding: 6upx 30upx 8upx;
		background: #fff;
		margin: 20upx 20upx 0;
		border-radius: 16upx 16upx 0 0;
		box-shadow: 0 4px 16upx rgba(255, 76, 124, 0.1);
		border-bottom: 2px solid rgba(255, 76, 124, 0.1);

		image {
			flex-shrink: 0;
			width: 70upx;
			height: 70upx;
			margin-right: 20upx;
			border-radius: 50%;
			border: 2px solid rgba(255, 76, 124, 0.1);
			padding: 5upx;
			background: rgba(255, 76, 124, 0.05);
		}

		.tit-box {
			flex: 1;
			display: flex;
			flex-direction: column;
		}

		.tit {
			font-size: $font-lg + 2upx;
			background: linear-gradient(to right, #FF4C7C, #FF85A2);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
			font-weight: bold;
		}

		.tit2 {
			font-size: $font-sm;
			color: $font-color-light;
		}

		.icon-you {
			font-size: $font-lg + 2upx;
			color: #FF4C7C;
		}
	}

	/* 分类推荐楼层 */
	.hot-section {
		display: flex;
		flex-wrap: wrap;
		padding: 10upx 20upx 30upx;
		background: #fff;
		margin: 0 20upx 20upx;
		border-radius: 0 0 16upx 16upx;
		box-shadow: 0 8px 20upx rgba(255, 76, 124, 0.1);

		.guess-item {
			display: flex;
			flex-direction: row;
			width: 100%;
			padding: 20upx;
			margin-bottom: 20upx;
			background: #fff;
			border-radius: 16upx;
			box-shadow: 0 8upx 16upx rgba(255, 76, 124, 0.08);
			transition: all 0.3s;
			border: 1px solid rgba(255, 76, 124, 0.05);
			
			&:hover {
				transform: translateY(-8upx);
				box-shadow: 0 12upx 24upx rgba(255, 76, 124, 0.16);
				border-color: rgba(255, 76, 124, 0.1);
			}
		}

		.image-wrapper {
			width: 30%;
			height: 220upx;
			border-radius: 16upx;
			overflow: hidden;

			image {
				width: 100%;
				height: 100%;
				opacity: 1;
				transition: all 0.4s;
				
				&:hover {
					transform: scale(1.08);
				}
			}
		}

		.title {
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 60upx;
			font-weight: bold;
		}

		.title2 {
			font-size: $font-sm;
			color: $font-color-light;
			line-height: 40upx;
			max-height: 80upx;
			overflow: hidden;
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
		}

		.price {
			font-size: $font-lg;
			background: linear-gradient(to right, #FF4C7C, #FF85A2);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
			line-height: 60upx;
			font-weight: bold;
		}

		.txt {
			width: 70%;
			display: flex;
			flex-direction: column;
			padding-left: 30upx;
			justify-content: space-between;
			position: relative;
			
			&::after {
				content: '';
				position: absolute;
				bottom: 10upx;
				left: 30upx;
				width: 80upx;
				height: 6upx;
				background: linear-gradient(to right, #FF4C7C, #FF85A2);
				border-radius: 3upx;
			}
		}
	}
	
	/* 品牌专区 */
	.guess-section .image-wrapper-brand {
		width: 100%;
		height: 150upx;
		border-radius: 16upx;
		overflow: hidden;
		box-shadow: 0 6upx 16upx rgba(255, 76, 124, 0.1);
		border: 1px solid rgba(255, 76, 124, 0.05);
		transition: all 0.3s;
		
		&:hover {
			transform: translateY(-5upx);
			box-shadow: 0 10upx 20upx rgba(255, 76, 124, 0.2);
		}
		
		image {
			width: 100%;
			height: 100%;
			opacity: 1;
			transition: all 0.4s;
			
			&:hover {
				transform: scale(1.08);
			}
		}
	}
	
	/* 秒杀专区的计时器 */
	.timer {
		display: inline-block;
		width: 40upx;
		height: 36upx;
		text-align: center;
		line-height: 36upx;
		margin-right: 8upx;
		font-size: $font-sm+2upx;
		color: #fff;
		border-radius: 6upx;
		background: linear-gradient(to right, #FF4C7C, #FF85A2);
		box-shadow: 0 4px 8upx rgba(255, 76, 124, 0.3);
	}

	/* 猜你喜欢 */
	.guess-section {
		display: flex;
		flex-wrap: wrap;
		padding: 10upx 20upx 30upx;
		background: #fff;
		margin: 0 20upx 20upx;
		border-radius: 0 0 16upx 16upx;
		box-shadow: 0 8px 20upx rgba(255, 76, 124, 0.1);

		.guess-item {
			display: flex;
			flex-direction: column;
			width: 48%;
			padding-bottom: 20upx;
			background: #fff;
			margin-top: 20upx;
			border-radius: 16upx;
			overflow: hidden;
			box-shadow: 0 8upx 16upx rgba(255, 76, 124, 0.08);
			transition: all 0.3s;
			border: 1px solid rgba(255, 76, 124, 0.05);

			&:nth-child(2n+1) {
				margin-right: 4%;
			}
			
			&:hover {
				transform: translateY(-8upx);
				box-shadow: 0 12upx 24upx rgba(255, 76, 124, 0.16);
				border-color: rgba(255, 76, 124, 0.1);
			}
		}

		.image-wrapper {
			width: 100%;
			height: 330upx;
			border-radius: 0;
			overflow: hidden;
			position: relative;
			
			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 0;
				width: 100%;
				height: 60upx;
				background: linear-gradient(to top, rgba(255, 76, 124, 0.06), transparent);
			}

			image {
				width: 100%;
				height: 100%;
				opacity: 1;
				transition: all 0.4s;
				
				&:hover {
					transform: scale(1.08);
				}
			}
		}
		
		.title {
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 80upx;
			padding: 0 20upx;
		}

		.title2 {
			font-size: $font-sm;
			color: $font-color-light;
			padding: 0 20upx;
		}

		.price {
			font-size: $font-lg;
			background: linear-gradient(to right, #FF4C7C, #FF85A2);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
			line-height: 1;
			padding: 20upx;
			font-weight: bold;
		}
	}

	/* 动画淡入效果 */
	@keyframes fadeIn {
		from { opacity: 0; transform: translateY(20upx); }
		to { opacity: 1; transform: translateY(0); }
	}

	.f-header {
		animation: fadeIn 0.8s ease-out;
	}

	.seckill-section, .cate-section, .carousel-section {
		animation: fadeIn 0.8s ease-out;
	}

	.guess-section, .hot-section {
		animation: fadeIn 0.8s ease-out 0.2s backwards;
	}

	/* 标题装饰 */
	.f-header .tit {
		position: relative;
		
		&::after {
			content: '';
			position: absolute;
			bottom: -6upx;
			left: 0;
			width: 40upx;
			height: 4upx;
			background: linear-gradient(to right, #FF4C7C, transparent);
			border-radius: 2upx;
		}
	}

	/* 卡片阴影样式美化 */
	.floor-item, .guess-item, .cate-item image {
		position: relative;
		overflow: visible;
		
		&::before {
			content: '';
			position: absolute;
			bottom: -8upx;
			left: 10%;
			width: 80%;
			height: 20upx;
			background: radial-gradient(rgba(255, 76, 124, 0.3), rgba(255, 76, 124, 0));
			border-radius: 50%;
			z-index: -1;
			opacity: 0;
			transition: opacity 0.3s;
		}
		
		&:hover::before {
			opacity: 0.5;
		}
	}
</style>

