/* eslint-disable */
<template>
  <div class="reviews-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>评价晒单</span>
          <el-tabs v-model="activeTab" @tab-click="handleTabChange">
            <el-tab-pane label="我的评价" name="my"></el-tab-pane>
            <el-tab-pane label="待评价" name="pending"></el-tab-pane>
          </el-tabs>
        </div>
      </template>
      
      <!-- 我的评价 -->
      <div v-if="activeTab === 'my' && myReviews.length > 0" class="review-list">
        <div v-for="review in myReviews" :key="review.id" class="review-item">
          <div class="product-info">
            <div class="product-img">
              <img :src="review.productPic" :alt="review.productName" @click="goToDetail(review.productId)">
            </div>
            <div class="product-detail">
              <div class="product-name" @click="goToDetail(review.productId)">{{ review.productName }}</div>
              <div class="review-date">评价时间: {{ formatDate(review.createTime) }}</div>
            </div>
          </div>
          
          <div class="review-content">
            <div class="review-rating">
              <span class="rating-label">我的评分：</span>
              <el-rate v-model="review.star" disabled></el-rate>
            </div>
            <div class="review-text">{{ review.content }}</div>
            <div v-if="review.pics && review.pics.length > 0" class="review-images">
              <el-image 
                v-for="(pic, index) in review.pics" 
                :key="index" 
                :src="pic" 
                :preview-src-list="review.pics"
                fit="cover"
                class="review-image"
              ></el-image>
            </div>
          </div>
          
          <div class="review-actions">
            <el-button type="danger" size="small" @click="handleDeleteReview(review.id)">删除评价</el-button>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="myTotal"
            :page-size="pageSize"
            :current-page="myCurrentPage"
            @current-change="handleMyPageChange"
          ></el-pagination>
        </div>
      </div>
      
      <!-- 待评价订单 -->
      <div v-if="activeTab === 'pending' && pendingReviews.length > 0" class="pending-list">
        <div v-for="item in pendingReviews" :key="item.id" class="pending-item">
          <div class="order-info">
            <div class="order-header">
              <span class="order-number">订单号: {{ item.orderNo }}</span>
              <span class="order-date">下单时间: {{ formatDate(item.createTime) }}</span>
            </div>
            <div class="product-info">
              <div class="product-img">
                <img :src="item.productPic" :alt="item.productName">
              </div>
              <div class="product-detail">
                <div class="product-name">{{ item.productName }}</div>
                <div class="product-price">¥{{ item.productPrice.toFixed(2) }}</div>
                <div class="product-specs">{{ item.specs || '默认规格' }}</div>
              </div>
            </div>
          </div>
          <div class="pending-actions">
            <el-button type="primary" @click="openReviewDialog(item)">评价晒单</el-button>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="pendingTotal"
            :page-size="pageSize"
            :current-page="pendingCurrentPage"
            @current-change="handlePendingPageChange"
          ></el-pagination>
        </div>
      </div>
      
      <!-- 空状态 -->
      <el-empty 
        v-if="(activeTab === 'my' && myReviews.length === 0) || 
              (activeTab === 'pending' && pendingReviews.length === 0)" 
        :description="activeTab === 'my' ? '暂无评价记录' : '暂无待评价订单'" 
        :image-size="200"
      ></el-empty>
    </el-card>
    
    <!-- 评价对话框 -->
    <el-dialog 
      title="发表评价" 
      v-model="reviewDialogVisible" 
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form :model="reviewForm" :rules="reviewRules" ref="reviewFormRef" label-width="80px">
        <el-form-item label="商品评分" prop="star">
          <el-rate v-model="reviewForm.star" :colors="rateColors"></el-rate>
        </el-form-item>
        
        <el-form-item label="评价内容" prop="content">
          <el-input 
            v-model="reviewForm.content" 
            type="textarea" 
            :rows="4" 
            placeholder="请分享您对该商品的使用体验和感受..."
          ></el-input>
        </el-form-item>
        
        <el-form-item label="上传图片">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :limit="5"
            :on-change="handleUploadChange"
            :on-remove="handleUploadRemove"
          >
            <template #default>
              <el-icon><Plus /></el-icon>
            </template>
          </el-upload>
          <div class="upload-tip">最多上传5张图片，每张不超过5MB</div>
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="reviewForm.anonymous">匿名评价</el-checkbox>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取 消</el-button>
          <el-button type="primary" @click="submitReview" :loading="submitting">提 交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { http } from '@/utils/request';
import { useRouter } from 'vue-router';
import { Plus } from '@element-plus/icons-vue';

export default {
  name: 'UserReviews',
  components: {
    Plus
  },
  setup() {
    const router = useRouter();
    
    // 评价列表相关
    const activeTab = ref('my'); // 'my' 或 'pending'
    const myReviews = ref([]);
    const pendingReviews = ref([]);
    const pageSize = ref(5);
    
    // 我的评价分页
    const myCurrentPage = ref(1);
    const myTotal = ref(0);
    
    // 待评价分页
    const pendingCurrentPage = ref(1);
    const pendingTotal = ref(0);
    
    // 评分颜色
    const rateColors = { 
      lowColor: '#F56C6C', 
      mediumColor: '#E6A23C', 
      highColor: '#67C23A' 
    };
    
    // 评价对话框
    const reviewDialogVisible = ref(false);
    const reviewFormRef = ref(null);
    const submitting = ref(false);
    
    // 待上传的图片文件
    const uploadFiles = ref([]);
    
    // 评价表单
    const reviewForm = reactive({
      orderId: null,
      orderItemId: null,
      productId: null,
      content: '',
      star: 5,
      anonymous: false,
      pics: []
    });
    
    // 表单验证规则
    const reviewRules = {
      star: [
        { required: true, message: '请选择评分', trigger: 'change' }
      ],
      content: [
        { required: true, message: '请输入评价内容', trigger: 'blur' },
        { min: 5, max: 200, message: '评价内容长度在 5 到 200 个字符', trigger: 'blur' }
      ]
    };
    
    // 加载我的评价
    const loadMyReviews = async () => {
      try {
        const res = await http.get('/review/list/user', {
          pageNum: myCurrentPage.value,
          pageSize: pageSize.value
        });
        if (res.code === 200) {
          myReviews.value = res.data.list;
          myTotal.value = res.data.total;
        } else {
          ElMessage.error(res.message || '获取评价列表失败');
        }
      } catch (error) {
        console.error('获取评价列表失败', error);
        ElMessage.error('网络错误，请稍后重试');
      }
    };
    
    // 加载待评价订单
    const loadPendingReviews = async () => {
      try {
        const res = await http.get('/order/pending-review', {
          pageNum: pendingCurrentPage.value,
          pageSize: pageSize.value
        });
        if (res.code === 200) {
          pendingReviews.value = res.data.list;
          pendingTotal.value = res.data.total;
        } else {
          ElMessage.error(res.message || '获取待评价订单失败');
        }
      } catch (error) {
        console.error('获取待评价订单失败', error);
        ElMessage.error('网络错误，请稍后重试');
      }
    };
    
    // 标签切换
    const handleTabChange = () => {
      if (activeTab.value === 'my') {
        loadMyReviews();
      } else {
        loadPendingReviews();
      }
    };
    
    // 我的评价分页
    const handleMyPageChange = (page) => {
      myCurrentPage.value = page;
      loadMyReviews();
    };
    
    // 待评价分页
    const handlePendingPageChange = (page) => {
      pendingCurrentPage.value = page;
      loadPendingReviews();
    };
    
    // 打开评价对话框
    const openReviewDialog = (item) => {
      // 重置表单
      if (reviewFormRef.value) {
        reviewFormRef.value.resetFields();
      }
      
      // 设置初始数据
      reviewForm.orderId = item.orderId;
      reviewForm.orderItemId = item.id;
      reviewForm.productId = item.productId;
      reviewForm.content = '';
      reviewForm.star = 5;
      reviewForm.anonymous = false;
      reviewForm.pics = [];
      
      // 清空上传文件
      uploadFiles.value = [];
      
      reviewDialogVisible.value = true;
    };
    
    // 关闭对话框
    const handleDialogClose = () => {
      ElMessageBox.confirm('确定要取消评价吗？已填写的内容将不会保存', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        reviewDialogVisible.value = false;
      }).catch(() => {
        // 用户取消关闭
      });
    };
    
    // 上传图片变化
    const handleUploadChange = (file, fileList) => {
      uploadFiles.value = fileList;
    };
    
    // 移除上传图片
    const handleUploadRemove = (file, fileList) => {
      uploadFiles.value = fileList;
    };
    
    // 提交评价
    const submitReview = () => {
      reviewFormRef.value.validate(async (valid) => {
        if (valid) {
          submitting.value = true;
          
          try {
            // 先上传图片，这里模拟上传图片，实际项目需要实现图片上传
            const uploadedPics = [];
            
            if (uploadFiles.value.length > 0) {
              for (const file of uploadFiles.value) {
                // 模拟上传，实际项目中需要发送请求上传图片
                // 模拟图片URL
                const picUrl = `https://example.com/images/${Date.now()}_${file.name}`;
                uploadedPics.push(picUrl);
              }
            }
            
            // 提交评价数据
            const reviewData = {
              orderId: reviewForm.orderId,
              orderItemId: reviewForm.orderItemId,
              productId: reviewForm.productId,
              content: reviewForm.content,
              star: reviewForm.star,
              anonymous: reviewForm.anonymous,
              pics: uploadedPics
            };
            
            const res = await http.post('/review/submit', reviewData);
            
            if (res.code === 200) {
              ElMessage.success('评价提交成功');
              reviewDialogVisible.value = false;
              
              // 刷新待评价列表
              loadPendingReviews();
            } else {
              ElMessage.error(res.message || '评价提交失败');
            }
          } catch (error) {
            console.error('评价提交失败', error);
            ElMessage.error('网络错误，请稍后重试');
          } finally {
            submitting.value = false;
          }
        }
      });
    };
    
    // 删除评价
    const handleDeleteReview = (id) => {
      ElMessageBox.confirm('确定要删除该评价吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await http.delete(`/review/delete/${id}`);
          if (res.code === 200) {
            ElMessage.success('删除评价成功');
            loadMyReviews();
          } else {
            ElMessage.error(res.message || '删除评价失败');
          }
        } catch (error) {
          console.error('删除评价失败', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 去商品详情页
    const goToDetail = (productId) => {
      router.push(`/product/${productId}`);
    };
    
    // 格式化日期
    const formatDate = (dateStr) => {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hour = String(date.getHours()).padStart(2, '0');
      const minute = String(date.getMinutes()).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hour}:${minute}`;
    };
    
    onMounted(() => {
      loadMyReviews();
    });
    
    return {
      activeTab,
      myReviews,
      pendingReviews,
      pageSize,
      myCurrentPage,
      myTotal,
      pendingCurrentPage,
      pendingTotal,
      rateColors,
      reviewDialogVisible,
      reviewFormRef,
      reviewForm,
      reviewRules,
      submitting,
      handleTabChange,
      handleMyPageChange,
      handlePendingPageChange,
      openReviewDialog,
      handleDialogClose,
      handleUploadChange,
      handleUploadRemove,
      submitReview,
      handleDeleteReview,
      goToDetail,
      formatDate
    };
  }
};
</script>

<style scoped>
.reviews-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header .el-tabs {
  margin-left: auto;
}

.review-list, .pending-list {
  margin-top: 20px;
}

.review-item, .pending-item {
  padding: 20px;
  margin-bottom: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  transition: all 0.3s;
}

.review-item:hover, .pending-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.product-info {
  display: flex;
  margin-bottom: 15px;
}

.product-img {
  width: 80px;
  height: 80px;
  margin-right: 15px;
  overflow: hidden;
}

.product-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.3s;
}

.product-img img:hover {
  transform: scale(1.05);
}

.product-detail {
  flex: 1;
}

.product-name {
  font-weight: bold;
  margin-bottom: 5px;
  cursor: pointer;
  color: #303133;
}

.product-name:hover {
  color: #409EFF;
}

.review-date, .order-date {
  color: #909399;
  font-size: 12px;
}

.review-content {
  margin-bottom: 15px;
}

.review-rating {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.rating-label {
  margin-right: 10px;
  font-weight: bold;
}

.review-text {
  line-height: 1.6;
  margin-bottom: 10px;
}

.review-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.review-image {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  cursor: pointer;
}

.review-actions, .pending-actions {
  display: flex;
  justify-content: flex-end;
}

.order-info {
  margin-bottom: 15px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px dashed #ebeef5;
}

.order-number {
  font-weight: bold;
}

.product-price {
  color: #F56C6C;
  font-size: 16px;
  font-weight: bold;
  margin: 5px 0;
}

.product-specs {
  color: #909399;
  font-size: 12px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}
</style> 