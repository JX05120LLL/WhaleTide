/* eslint-disable */
<template>
  <div class="history-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>浏览历史</span>
          <div class="actions">
            <el-button type="danger" @click="handleClear">清空历史</el-button>
            <el-popover placement="bottom" :width="100" trigger="click">
              <template #reference>
                <el-button>时间排序</el-button>
              </template>
              <div class="sort-options">
                <div 
                  class="sort-item" 
                  :class="{ active: sortBy === 'newest' }"
                  @click="sortBy = 'newest'"
                >
                  最近浏览
                </div>
                <div 
                  class="sort-item" 
                  :class="{ active: sortBy === 'oldest' }"
                  @click="sortBy = 'oldest'"
                >
                  最早浏览
                </div>
              </div>
            </el-popover>
          </div>
        </div>
      </template>
      
      <div v-if="historyList.length > 0" class="history-list">
        <div v-for="item in sortedHistoryList" :key="item.id" class="history-item">
          <div class="product-info" @click="goToDetail(item.productId)">
            <div class="product-img">
              <img :src="formatImageUrl(item.productPic)" :alt="item.productName">
            </div>
            <div class="product-detail">
              <div class="product-name">{{ item.productName }}</div>
              <div class="product-price">¥{{ item.productPrice.toFixed(2) }}</div>
              <div class="view-time">
                浏览时间: {{ formatDate(item.viewTime) }}
              </div>
            </div>
          </div>
          <div class="actions">
            <el-button type="primary" size="small" @click="addToCart(item)">加入购物车</el-button>
            <el-button type="danger" size="small" @click="handleDelete(item.id)">删除记录</el-button>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
      </div>
      
      <!-- 空状态 -->
      <el-empty v-else description="暂无浏览记录" :image-size="200">
        <el-button type="primary" @click="goToHome">去逛逛</el-button>
      </el-empty>
    </el-card>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { http } from '@/utils/request';
import { staticBaseURL } from '@/utils/request';
import { useRouter } from 'vue-router';

export default {
  name: 'UserHistory',
  setup() {
    const router = useRouter();
    
    // 浏览记录列表
    const historyList = ref([]);
    const sortBy = ref('newest'); // 'newest' or 'oldest'
    
    // 分页
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);
    
    // 处理图片URL
    const formatImageUrl = (path) => {
      if (!path) return '/images/placeholder.png';
      
      // 检查路径是否已经包含完整URL
      if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
      }
      
      // 对于以/images开头的路径，将扩展名从.jpg改为.png (如果存在)
      if (path.startsWith('/images/')) {
        // 确保图片扩展名为.png
        if (path.endsWith('.jpg')) {
          return path.replace('.jpg', '.png');
        }
        return `http://localhost:8080${path}`;
      }
      
      // 处理相对路径，确保以/开头
      const normalizedPath = path.startsWith('/') ? path : `/${path}`;
      
      // 完整URL
      let fullPath = `http://localhost:8080${normalizedPath}`;
      
      // 确保图片扩展名为.png
      if (fullPath.endsWith('.jpg')) {
        fullPath = fullPath.replace('.jpg', '.png');
      }
      
      return fullPath;
    };
    
    // 排序后的列表
    const sortedHistoryList = computed(() => {
      // 确保historyList存在并且是数组
      if (!historyList.value || !Array.isArray(historyList.value)) {
        return [];
      }
      
      if (sortBy.value === 'newest') {
        return [...historyList.value].sort((a, b) => 
          new Date(b.viewTime) - new Date(a.viewTime)
        );
      } else {
        return [...historyList.value].sort((a, b) => 
          new Date(a.viewTime) - new Date(b.viewTime)
        );
      }
    });
    
    // 加载浏览记录
    const loadHistoryList = async () => {
      try {
        const res = await http.get('/history/list', {
          pageNum: currentPage.value,
          pageSize: pageSize.value
        });
        if (res.code === 200) {
          historyList.value = res.data.list;
          total.value = res.data.total;
        } else {
          ElMessage.error(res.message || '获取浏览记录失败');
        }
      } catch (error) {
        console.error('获取浏览记录失败', error);
        ElMessage.error('网络错误，请稍后重试');
      }
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
    
    // 去商品详情页
    const goToDetail = (productId) => {
      router.push(`/product/${productId}`);
    };
    
    // 去首页
    const goToHome = () => {
      router.push('/');
    };
    
    // 加入购物车
    const addToCart = async (item) => {
      try {
        const cartData = {
          productId: item.productId,
          quantity: 1
        };
        const res = await http.post('/cart/add', cartData);
        if (res.code === 200) {
          ElMessage.success('加入购物车成功');
        } else {
          ElMessage.error(res.message || '加入购物车失败');
        }
      } catch (error) {
        console.error('加入购物车失败', error);
        ElMessage.error('网络错误，请稍后重试');
      }
    };
    
    // 删除记录
    const handleDelete = (id) => {
      ElMessageBox.confirm('确定要删除此浏览记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await http.delete(`/history/delete/${id}`);
          if (res.code === 200) {
            ElMessage.success('删除成功');
            loadHistoryList();
          } else {
            ElMessage.error(res.message || '删除失败');
          }
        } catch (error) {
          console.error('删除浏览记录失败', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 清空历史
    const handleClear = () => {
      ElMessageBox.confirm('确定要清空所有浏览记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await http.delete('/history/clear');
          if (res.code === 200) {
            ElMessage.success('清空成功');
            historyList.value = [];
            total.value = 0;
          } else {
            ElMessage.error(res.message || '清空失败');
          }
        } catch (error) {
          console.error('清空浏览记录失败', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 分页变化
    const handleCurrentChange = (page) => {
      currentPage.value = page;
      loadHistoryList();
    };
    
    onMounted(() => {
      loadHistoryList();
    });
    
    return {
      historyList,
      sortedHistoryList,
      sortBy,
      currentPage,
      pageSize,
      total,
      formatDate,
      goToDetail,
      goToHome,
      addToCart,
      handleDelete,
      handleClear,
      handleCurrentChange,
      formatImageUrl
    };
  }
};
</script>

<style scoped>
.history-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header .actions {
  display: flex;
  gap: 10px;
}

.history-list {
  margin-top: 20px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  transition: all 0.3s;
}

.history-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.product-info {
  display: flex;
  align-items: center;
  flex: 1;
  cursor: pointer;
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
  transition: transform 0.3s;
}

.product-info:hover .product-img img {
  transform: scale(1.05);
}

.product-detail {
  flex: 1;
}

.product-name {
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
}

.product-price {
  color: #F56C6C;
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 5px;
}

.view-time {
  color: #909399;
  font-size: 12px;
}

.history-item .actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.sort-options {
  display: flex;
  flex-direction: column;
}

.sort-item {
  padding: 8px 10px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.sort-item:hover {
  background-color: #f5f7fa;
}

.sort-item.active {
  color: #409eff;
  font-weight: bold;
}
</style> 