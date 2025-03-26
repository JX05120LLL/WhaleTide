<template> 
  <div>
    <el-upload
      :action="minioUploadUrl"
      list-type="picture-card"
      :multiple="false" 
      :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0] && fileList[0].url" alt="">
    </el-dialog>
    <div class="el-upload__tip" v-if="!showFileList" style="color: #ff4949; margin-top: 5px;">请点击上方框内加号上传商品主图</div>
  </div>
</template>
<script>
  import {policy} from '@/api/oss'

  export default {
    name: 'singleUpload',
    props: {
      value: String
    },
    computed: {
      imageUrl() {
        return this.value;
      },
      imageName() {
        if (this.value != null && this.value !== '') {
          return this.value.substr(this.value.lastIndexOf("/") + 1);
        } else {
          return null;
        }
      },
      fileList() {
        return [{
          name: this.imageName,
          url: this.imageUrl
        }]
      },
      showFileList: {
        get: function () {
          return this.value !== null && this.value !== ''&& this.value!==undefined;
        },
        set: function (newValue) {
        }
      }
    },
    data() {
      return {
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: '',
          host: '',
          // callback:'',
        },
        dialogVisible: false,
        useOss:false, //使用oss->true;使用MinIO->false
        ossUploadUrl:'http://macro-oss.oss-cn-shenzhen.aliyuncs.com',
        minioUploadUrl:'http://localhost:8080/minio/upload',
      };
    },
    methods: {
      emitInput(val) {
        this.$emit('input', val)
      },
      handleRemove(file, fileList) {
        this.emitInput('');
      },
      handlePreview(file) {
        this.dialogVisible = true;
      },
      beforeUpload(file) {
        // 模拟上传成功的情况
        const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
        const isLt10M = file.size / 1024 / 1024 < 10;

        if (!isJPG) {
          this.$message.error('只能上传JPG/PNG格式的图片!');
          return false;
        }
        if (!isLt10M) {
          this.$message.error('图片大小不能超过10MB!');
          return false;
        }

        // 创建临时URL供预览使用
        return new Promise(resolve => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = () => {
            // 延迟一下，模拟网络请求
            setTimeout(() => {
              resolve(true);
            }, 300);
          };
        });
      },
      handleUploadSuccess(res, file) {
        // 模拟上传成功后的处理
        this.showFileList = true;
        this.fileList.pop();
        
        // 使用本地文件URL (使用FileReader读取的base64内容)
        const url = URL.createObjectURL(file.raw);
        
        this.fileList.push({name: file.name, url: url});
        this.emitInput(url);
        
        this.$message.success('上传成功!');
      }
    }
  }
</script>
<style>

</style>


