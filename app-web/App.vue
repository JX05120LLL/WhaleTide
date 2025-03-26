<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
/**
 * vuex管理登陆状态，具体可以参考官方登陆模板示例
 */
import {
  mapMutations
} from 'vuex';
export default {
  methods: {
    ...mapMutations(['login'])
  },
  created: function() {
    let userInfo = localStorage.getItem('userInfo') || '';
    if(userInfo) {
      try {
        userInfo = JSON.parse(userInfo);
        if(userInfo.id){
          //更新登陆状态
          this.login(userInfo);
        }
      } catch(e) {
        console.error(e);
      }
    }
  }
}
</script>

<style lang='scss'>
/*
  全局公共样式和字体图标
*/
@font-face {
  font-family: yticon;
  font-weight: normal;
  font-style: normal;
  src: url('https://at.alicdn.com/t/font_1078604_w4kpxh0rafi.ttf') format('truetype');
}

.yticon {
  font-family: "yticon" !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-yiguoqi1:before {
  content: "\e700";
}

.icon-iconfontshanchu1:before {
  content: "\e619";
}

.icon-iconfontweixin:before {
  content: "\e611";
}

.icon-alipay:before {
  content: "\e636";
}

.icon-shang:before {
  content: "\e624";
}

.icon-shouye:before {
  content: "\e626";
}

.icon-shanchu4:before {
  content: "\e622";
}

.icon-xiaoxi:before {
  content: "\e618";
}

.icon-jiantour-copy:before {
  content: "\e600";
}

.icon-fenxiang2:before {
  content: "\e61e";
}

.icon-pingjia:before {
  content: "\e67b";
}

.icon-daifukuan:before {
  content: "\e68f";
}

.icon-pinglun-copy:before {
  content: "\e612";
}

.icon-dianhua-copy:before {
  content: "\e621";
}

.icon-shoucang:before {
  content: "\e645";
}

.icon-xuanzhong2:before {
  content: "\e62f";
}

.icon-gouwuche_:before {
  content: "\e630";
}

.icon-icon-test:before {
  content: "\e60c";
}

.icon-icon-test1:before {
  content: "\e632";
}

.icon-bianji:before {
  content: "\e646";
}

.icon-jiazailoading-A:before {
  content: "\e8fc";
}

.icon-zuoshang:before {
  content: "\e613";
}

.icon-jia2:before {
  content: "\e60a";
}

.icon-huifu:before {
  content: "\e68b";
}

.icon-sousuo:before {
  content: "\e7ce";
}

.icon-arrow-fine-up:before {
  content: "\e601";
}

.icon-hot:before {
  content: "\e60e";
}

.icon-lishijilu:before {
  content: "\e6b9";
}

.icon-zhengxinchaxun-zhifubaoceping-:before {
  content: "\e616";
}

.icon-naozhong:before {
  content: "\e64a";
}

.icon-xiatubiao--copy:before {
  content: "\e608";
}

.icon-shoucang_xuanzhongzhuangtai:before {
  content: "\e6a9";
}

.icon-jia1:before {
  content: "\e61c";
}

.icon-bangzhu1:before {
  content: "\e63d";
}

.icon-arrow-left-bottom:before {
  content: "\e602";
}

.icon-arrow-right-bottom:before {
  content: "\e603";
}

.icon-arrow-left-top:before {
  content: "\e604";
}

/* 通用样式 */
* {
  box-sizing: border-box;
}

body {
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
  color: #333;
  font-size: 14px;
  background-color: #f8f8f8;
}

a {
  text-decoration: none;
  color: inherit;
}

.container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.flex {
  display: flex;
}

.flex-col {
  flex-direction: column;
}

.flex-row {
  flex-direction: row;
}

.justify-between {
  justify-content: space-between;
}

.justify-center {
  justify-content: center;
}

.items-center {
  align-items: center;
}

.text-center {
  text-align: center;
}

.text-primary {
  color: #0081ff;
}

.text-warning {
  color: #ff9900;
}

.text-danger {
  color: #ff3300;
}

.text-success {
  color: #19be6b;
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background-color: #0081ff;
  color: #fff;
}

.btn-primary:hover {
  background-color: #0069d9;
}

.card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}
</style> 