/* eslint-disable */
<template>
  <div class="favorites-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的收藏</span>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selectedItems.length === 0">
            删除选中
          </el-button>
        </div>
      </template>
      
      <div v-if="favoriteList.length > 0" class="favorite-list">
        <el-table
          ref="tableRef"
          :data="favoriteList"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="商品信息" min-width="400">
            <template #default="scope">
              <div class="product-info">
                <div class="product-img">
                  <img :src="formatImageUrl(scope.row.productPic)" alt="商品图片" @click="goToDetail(scope.row.productId)">
                </div>
                <div class="product-detail">
                  <div class="product-name" @click="goToDetail(scope.row.productId)">
                    {{ scope.row.productName }}
                  </div>
                  <div class="product-price">
                    ¥{{ scope.row.productPrice.toFixed(2) }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="收藏时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220">
            <template #default="scope">
              <div class="button-group">
                <el-button type="primary" size="small" @click="addToCart(scope.row)">加入购物车</el-button>
                <el-button type="danger" size="small" @click="handleDelete(scope.row)">取消收藏</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
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
      <el-empty v-else description="暂无收藏商品" :image-size="200">
        <el-button type="primary" @click="goToHome">去逛逛</el-button>
      </el-empty>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { http } from '@/utils/request';
import { staticBaseURL } from '@/utils/request';
import { useRouter } from 'vue-router';

export default {
  name: 'UserFavorites',
  setup() {
    const router = useRouter();
    
    // 收藏列表
    const favoriteList = ref([]);
    const tableRef = ref(null);
    const selectedItems = ref([]);
    
    // 分页
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);
    
    // 处理图片URL，确保使用正确的服务器地址
    const formatImageUrl = (path) => {
      if (!path) return '/images/placeholder.png';
      
      // 如果路径已经是完整URL，直接返回
      if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
      }
      
      // 处理相对路径，确保以/开头
      const normalizedPath = path.startsWith('/') ? path : `/${path}`;
      
      // 使用后端服务器地址
      return `${staticBaseURL}${normalizedPath}`;
    };
    
    // 加载收藏列表
    const loadFavoriteList = async () => {
      try {
        const res = await http.get('/favorite/list', {
          pageNum: currentPage.value,
          pageSize: pageSize.value
        });
        if (res.code === 200) {
          favoriteList.value = res.data.list;
          total.value = res.data.total;
        } else {
          ElMessage.error(res.message || '获取收藏列表失败');
        }
      } catch (error) {
        console.error('获取收藏列表失败', error);
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
    
    // 选择变化
    const handleSelectionChange = (selection) => {
      selectedItems.value = selection;
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
        // 1. 确保商品ID是数字类型
        const productId = parseInt(item.productId, 10);
        
        // 2. 构建请求体，确保格式与CartAddDTO一致
        const cartData = {
          productId: productId, // 使用数字类型
          quantity: 1
        };
        
        console.log('添加购物车，请求数据:', cartData);
        
        // 3. 使用cart API
        const res = await http.post('/cart/add', cartData);
        
        if (res.code === 200) {
          ElMessage.success('加入购物车成功');
        } else {
          ElMessage.error(res.message || '加入购物车失败');
        }
      } catch (error) {
        console.error('加入购物车失败', error);
        if (error.response && error.response.status === 401) {
          ElMessage.warning('请先登录');
          router.push('/login');
        } else {
          ElMessage.error('网络错误，请稍后重试');
        }
      }
    };
    
    // 取消收藏
    const handleDelete = (item) => {
      ElMessageBox.confirm('确定要取消收藏该商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await http.delete(`/favorite/delete/${item.productId}`);
          if (res.code === 200) {
            ElMessage.success('取消收藏成功');
            loadFavoriteList();
          } else {
            ElMessage.error(res.message || '取消收藏失败');
          }
        } catch (error) {
          console.error('取消收藏失败', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 批量删除
    const handleBatchDelete = () => {
      if (selectedItems.value.length === 0) {
        ElMessage.warning('请选择要取消收藏的商品');
        return;
      }
      
      ElMessageBox.confirm(`确定要取消收藏选中的 ${selectedItems.value.length} 个商品吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const productIds = selectedItems.value.map(item => item.productId);
          const res = await http.delete(`/favorite/delete/${productIds.join(',')}`);
          if (res.code === 200) {
            ElMessage.success('批量取消收藏成功');
            loadFavoriteList();
          } else {
            ElMessage.error(res.message || '批量取消收藏失败');
          }
        } catch (error) {
          console.error('批量取消收藏失败', error);
          ElMessage.error('网络错误，请稍后重试');
        }
      }).catch(() => {
        // 用户取消操作
      });
    };
    
    // 分页变化
    const handleCurrentChange = (page) => {
      currentPage.value = page;
      loadFavoriteList();
    };
    
    onMounted(() => {
      loadFavoriteList();
    });
    
    return {
      favoriteList,
      tableRef,
      selectedItems,
      currentPage,
      pageSize,
      total,
      formatDate,
      formatImageUrl,
      handleSelectionChange,
      goToDetail,
      goToHome,
      addToCart,
      handleDelete,
      handleBatchDelete,
      handleCurrentChange
    };
  }
};
</script>

<style scoped>
.favorites-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.favorite-list {
  margin-top: 20px;
}

.product-info {
  display: flex;
  align-items: center;
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
  margin-bottom: 8px;
  cursor: pointer;
  color: #303133;
}

.product-name:hover {
  color: #409EFF;
}

.product-price {
  color: #F56C6C;
  font-weight: bold;
  font-size: 16px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.button-group {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.button-group .el-button {
  min-width: 90px;
  margin: 0;
}
</style> 