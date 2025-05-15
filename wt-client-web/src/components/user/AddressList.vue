/* eslint-disable */
<template>
  <div class="address-list-container">
    <div v-if="loading" class="loading">
      <el-skeleton :rows="3" animated />
    </div>
    
    <div v-else-if="addressList.length > 0" class="address-list">
      <div 
        v-for="(address, index) in addressList" 
        :key="address.id" 
        class="address-item" 
        :class="{ 'is-default': address.isDefault }"
        @click="selectAddress(address)"
      >
        <div class="address-info">
          <div class="name-phone">
            <span class="name">{{ address.receiverName }}</span>
            <span class="phone">{{ address.receiverPhone }}</span>
            <el-tag v-if="address.isDefault" size="small" type="success">默认</el-tag>
          </div>
          <div class="detail">
            {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detailAddress }}
          </div>
        </div>
      </div>
    </div>
    
    <el-empty v-else description="暂无收货地址" :image-size="200">
      <el-button type="primary" @click="handleAddAddress">添加新地址</el-button>
    </el-empty>
    
    <div class="actions">
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="handleAddAddress">添加新地址</el-button>
    </div>
    
    <!-- 地址编辑对话框 -->
    <el-dialog 
      :title="dialogType === 'add' ? '添加收货地址' : '编辑收货地址'" 
      v-model="dialogVisible" 
      width="500px"
    >
      <el-form :model="addressForm" :rules="rules" ref="addressFormRef" label-width="100px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="receiverPhone">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号码"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="regionCodes">
          <el-cascader 
            v-model="addressForm.regionCodes"
            :options="regionOptions"
            @change="handleRegionChange"
            placeholder="请选择所在地区"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress">
          <el-input 
            v-model="addressForm.detailAddress" 
            type="textarea" 
            placeholder="请输入详细地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮政编码" prop="postCode">
          <el-input v-model="addressForm.postCode" placeholder="请输入邮政编码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="addressForm.isDefault">设为默认收货地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAddress">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { http } from '@/utils/request';
import { regionData } from '@/utils/region-data';

export default {
  name: 'AddressList',
  emits: ['select', 'close'],
  setup(props, { emit }) {
    // 状态
    const loading = ref(true);
    const addressList = ref([]);
    
    // 表单ref
    const addressFormRef = ref(null);
    
    // 对话框类型和可见性
    const dialogType = ref('add');
    const dialogVisible = ref(false);
    
    // 地址表单数据
    const addressForm = reactive({
      id: null,
      receiverName: '',
      receiverPhone: '',
      province: '',
      city: '',
      district: '',
      detailAddress: '',
      postCode: '',
      isDefault: false,
      regionCodes: []
    });
    
    // 表单校验规则
    const rules = {
      receiverName: [
        { required: true, message: '请输入收货人姓名', trigger: 'blur' },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
      ],
      receiverPhone: [
        { required: true, message: '请输入手机号码', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      regionCodes: [
        { required: true, message: '请选择所在地区', trigger: 'change' }
      ],
      detailAddress: [
        { required: true, message: '请输入详细地址', trigger: 'blur' },
        { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
      ],
      postCode: [
        { pattern: /^\d{6}$/, message: '请输入正确的邮政编码', trigger: 'blur' }
      ]
    };
    
    // 地区数据
    const regionOptions = ref(regionData);
    
    // 处理地区选择变化
    const handleRegionChange = (values) => {
      if (values && values.length === 3) {
        const selectedOptions = [];
        let options = regionOptions.value;
        
        for (let i = 0; i < values.length; i++) {
          const option = options.find(opt => opt.value === values[i]);
          if (option) {
            selectedOptions.push(option);
            options = option.children || [];
          }
        }
        
        if (selectedOptions.length === 3) {
          addressForm.province = selectedOptions[0].label;
          addressForm.city = selectedOptions[1].label;
          addressForm.district = selectedOptions[2].label;
        }
      }
    };
    
    // 加载地址列表
    const loadAddressList = async () => {
      loading.value = true;
      try {
        const res = await http.get('/address/list');
        if (res.code === 200) {
          addressList.value = res.data;
        } else {
          ElMessage.error(res.message || '获取地址列表失败');
        }
      } catch (error) {
        console.error('获取地址列表失败', error);
        ElMessage.error('网络错误，请稍后重试');
      } finally {
        loading.value = false;
      }
    };
    
    // 选择地址
    const selectAddress = (address) => {
      emit('select', address);
    };
    
    // 添加新地址
    const handleAddAddress = () => {
      // 重置表单
      if (addressFormRef.value) {
        addressFormRef.value.resetFields();
      }
      
      dialogType.value = 'add';
      addressForm.id = null;
      addressForm.receiverName = '';
      addressForm.receiverPhone = '';
      addressForm.province = '';
      addressForm.city = '';
      addressForm.district = '';
      addressForm.detailAddress = '';
      addressForm.postCode = '';
      addressForm.isDefault = false;
      addressForm.regionCodes = [];
      
      dialogVisible.value = true;
    };
    
    // 保存地址
    const saveAddress = () => {
      addressFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            let res;
            
            // 构建提交的数据
            const addressData = {
              id: addressForm.id,
              receiverName: addressForm.receiverName,
              receiverPhone: addressForm.receiverPhone,
              province: addressForm.province,
              city: addressForm.city,
              district: addressForm.district,
              detailAddress: addressForm.detailAddress,
              postCode: addressForm.postCode,
              isDefault: addressForm.isDefault
            };
            
            if (dialogType.value === 'add') {
              res = await http.post('/address/add', addressData);
            } else {
              res = await http.put('/address/update', addressData);
            }
            
            if (res.code === 200) {
              ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功');
              dialogVisible.value = false;
              loadAddressList();
            } else {
              ElMessage.error(res.message || (dialogType.value === 'add' ? '添加失败' : '更新失败'));
            }
          } catch (error) {
            console.error('保存地址失败', error);
            ElMessage.error('网络错误，请稍后重试');
          }
        }
      });
    };
    
    onMounted(() => {
      loadAddressList();
    });
    
    return {
      loading,
      addressList,
      addressFormRef,
      dialogType,
      dialogVisible,
      addressForm,
      rules,
      regionOptions,
      handleRegionChange,
      selectAddress,
      handleAddAddress,
      saveAddress
    };
  }
};
</script>

<style scoped>
.address-list-container {
  padding: 15px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.address-item {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.address-item:hover {
  border-color: #409eff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.address-item.is-default {
  border-color: #67c23a;
  background-color: #f0f9eb;
}

.name-phone {
  margin-bottom: 10px;
}

.name {
  font-weight: bold;
  margin-right: 16px;
}

.phone {
  color: #606266;
  margin-right: 16px;
}

.detail {
  color: #606266;
  line-height: 1.5;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  gap: 15px;
}

.loading {
  padding: 20px;
}
</style> 