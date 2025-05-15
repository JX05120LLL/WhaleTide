<template>
  <div class="wscn-http404-container">
    <div class="wscn-http404">
      <div class="pic-404">
        <h1>404</h1>
      </div>
      <div class="bullshit">
        <div class="bullshit__headline">页面不存在</div>
        <div class="bullshit__info">请检查您输入的网址是否正确，或者点击下面的按钮返回首页</div>
        <a href="/" class="bullshit__return-home">返回首页</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Page404',
  computed: {
    message() {
      return '找不到页面'
    }
  }
}
</script>

<style lang="scss" scoped>
.wscn-http404-container {
  transform: translate(-50%, -50%);
  position: absolute;
  top: 40%;
  left: 50%;
}

.wscn-http404 {
  position: relative;
  width: 800px;
  padding: 0 50px;
  overflow: hidden;

  .pic-404 {
    position: relative;
    float: left;
    width: 300px;
    padding: 30px 0;
    overflow: hidden;
    
    h1 {
      font-size: 120px;
      color: #409EFF;
    }
  }

  .bullshit {
    position: relative;
    float: left;
    width: 300px;
    padding: 30px 0;
    overflow: hidden;

    &__headline {
      font-size: 32px;
      font-weight: bold;
      color: #1F2D3D;
    }

    &__info {
      margin: 20px 0;
      font-size: 16px;
      color: #606266;
    }

    &__return-home {
      display: block;
      margin: 20px 0;
      width: 150px;
      padding: 10px;
      font-size: 16px;
      text-align: center;
      color: white;
      background: #409EFF;
      border-radius: 5px;
      text-decoration: none;
    }
  }
}
</style> 