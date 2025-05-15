<template>
  <footer class="footer">
    <div class="footer-wave"></div>
    <div class="container">
      <div class="bubble-footer">
        <div v-for="n in 8" :key="n" class="bubble" 
            :style="{
              left: `${Math.random() * 100}%`,
              width: `${15 + Math.random() * 20}px`,
              height: `${15 + Math.random() * 20}px`,
              animationDuration: `${5 + Math.random() * 8}s`,
              animationDelay: `${Math.random() * 5}s`
            }">
        </div>
      </div>
      
      <div class="footer-content">
        <div class="footer-section">
          <h3>购物指南</h3>
          <ul>
            <li><a href="#">账户注册</a></li>
            <li><a href="#">购物流程</a></li>
            <li><a href="#">订单查询</a></li>
            <li><a href="#">常见问题</a></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>支付方式</h3>
          <ul>
            <li><a href="#">在线支付</a></li>
            <li><a href="#">货到付款</a></li>
            <li><a href="#">分期付款</a></li>
            <li><a href="#">公司转账</a></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>配送方式</h3>
          <ul>
            <li><a href="#">上门自提</a></li>
            <li><a href="#">快递配送</a></li>
            <li><a href="#">配送范围</a></li>
            <li><a href="#">运费说明</a></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>售后服务</h3>
          <ul>
            <li><a href="#">退换货政策</a></li>
            <li><a href="#">退换货流程</a></li>
            <li><a href="#">投诉与建议</a></li>
            <li><a href="#">质量保证</a></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>关注我们</h3>
          <div class="contact-info">
            <p>客服热线：400-888-8888</p>
            <p>工作时间：9:00-22:00</p>
            <div class="social-icons">
              <a href="#"><i class="el-icon-s-platform"></i></a>
              <a href="#"><i class="el-icon-s-custom"></i></a>
              <a href="#"><i class="el-icon-s-promotion"></i></a>
            </div>
          </div>
        </div>
      </div>
      
      <div class="footer-bottom">
        <div class="copyright">
          <p>© 2023 鲸浪商城 WhaleTide. 保留所有权利。</p>
        </div>
        <div class="footer-links">
          <a href="#">隐私政策</a>
          <a href="#">使用条款</a>
          <a href="#">关于我们</a>
          <a href="#">联系我们</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter'
}
</script>

<style scoped>
.footer {
  background: linear-gradient(135deg, var(--deep-blue), #0a1922);
  color: #fff;
  padding: 50px 0 20px;
  margin-top: 80px;
  position: relative;
}

.footer-wave {
  position: absolute;
  top: -20px;
  left: 0;
  width: 100%;
  height: 20px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z' fill='%23003a70'/%3E%3C/svg%3E");
  background-size: cover;
  background-repeat: no-repeat;
  transform: rotate(180deg);
  animation: waveMove 15s ease-in-out infinite alternate;
}

.footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='400' viewBox='0 0 800 800'%3E%3Cg fill='none' stroke='%230078d7' stroke-width='1'%3E%3Cpath d='M769 229L1037 260.9M927 880L731 737 520 660 309 538 40 599 295 764 126.5 879.5 40 599-197 493 102 382-31 229 126.5 79.5-69-63'/%3E%3Cpath d='M-31 229L237 261 390 382 603 493 308.5 537.5 101.5 381.5M370 905L295 764'/%3E%3Cpath d='M520 660L578 842 731 737 840 599 603 493 520 660 295 764 309 538 390 382 539 269 769 229 577.5 41.5 370 105 295 -36 126.5 79.5 237 261 102 382 40 599 -69 737 127 880'/%3E%3Cpath d='M520-140L578.5 42.5 731-63M603 493L539 269 237 261 370 105M902 382L539 269M390 382L102 382'/%3E%3Cpath d='M-222 42L126.5 79.5 370 105 539 269 577.5 41.5 927 80 769 229 902 382 603 493 731 737M295-36L577.5 41.5M578 842L295 764M40-201L127 80M102 382L-261 269'/%3E%3C/g%3E%3Cg fill='%235bebeb'%3E%3Ccircle cx='769' cy='229' r='5'/%3E%3Ccircle cx='539' cy='269' r='5'/%3E%3Ccircle cx='603' cy='493' r='5'/%3E%3Ccircle cx='731' cy='737' r='5'/%3E%3Ccircle cx='520' cy='660' r='5'/%3E%3Ccircle cx='309' cy='538' r='5'/%3E%3Ccircle cx='295' cy='764' r='5'/%3E%3Ccircle cx='40' cy='599' r='5'/%3E%3Ccircle cx='102' cy='382' r='5'/%3E%3Ccircle cx='127' cy='80' r='5'/%3E%3Ccircle cx='370' cy='105' r='5'/%3E%3Ccircle cx='578' cy='42' r='5'/%3E%3Ccircle cx='237' cy='261' r='5'/%3E%3Ccircle cx='390' cy='382' r='5'/%3E%3C/g%3E%3C/svg%3E");
  opacity: 0.03;
  pointer-events: none;
}

.container {
  position: relative;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 30px;
  position: relative;
  z-index: 1;
}

.footer-section {
  width: 20%;
  min-width: 180px;
  margin-bottom: 30px;
}

.footer-section h3 {
  font-size: 18px;
  margin-bottom: 20px;
  font-weight: 500;
  position: relative;
  padding-left: 15px;
  display: inline-block;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.footer-section h3::before {
  content: '';
  position: absolute;
  left: 0;
  top: 5px;
  height: 15px;
  width: 4px;
  background: var(--secondary-gradient);
  border-radius: 4px;
  animation: titlePulse 2s infinite alternate;
}

.footer-section h3::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -5px;
  width: 40px;
  height: 2px;
  background: var(--primary-gradient);
  border-radius: 2px;
  transition: width var(--transition-normal);
}

.footer-section:hover h3::after {
  width: 60px;
}

.footer-section ul {
  list-style: none;
  padding-left: 0;
}

.footer-section ul li {
  margin-bottom: 12px;
  position: relative;
  padding-left: 0;
  transition: transform var(--transition-normal);
}

.footer-section ul li a {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  transition: all var(--transition-normal);
  position: relative;
  padding-left: 0;
  display: inline-block;
}

.footer-section ul li a::before {
  content: '›';
  position: absolute;
  left: -15px;
  opacity: 0;
  color: var(--secondary-color);
  transition: all var(--transition-normal);
}

.footer-section ul li a:hover {
  color: white;
  padding-left: 15px;
}

.footer-section ul li:hover {
  transform: translateX(5px);
}

.footer-section ul li a:hover::before {
  left: 0;
  opacity: 1;
}

.contact-info p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  margin-bottom: 12px;
  position: relative;
  padding-left: 25px;
}

.contact-info p::before {
  content: '📞';
  position: absolute;
  left: 0;
  opacity: 0.8;
}

.contact-info p:nth-child(2)::before {
  content: '🕒';
}

.social-icons {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.social-icons a {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  color: #fff;
  transition: all var(--transition-normal);
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  overflow: hidden;
}

.social-icons a::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: var(--primary-gradient);
  transition: all var(--transition-normal);
  z-index: -1;
}

.social-icons a:hover {
  transform: translateY(-5px) rotate(360deg);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  border-color: transparent;
}

.social-icons a:hover::before {
  left: 0;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  position: relative;
  z-index: 1;
}

.copyright {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
}

.footer-links {
  display: flex;
  gap: 20px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  transition: all var(--transition-normal);
  position: relative;
  padding-bottom: 2px;
}

.footer-links a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 1px;
  background-color: var(--teal);
  transition: width var(--transition-normal);
}

.footer-links a:hover {
  color: var(--teal);
}

.footer-links a:hover::after {
  width: 100%;
}

/* 气泡效果 */
.bubble-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.bubble-footer .bubble {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle at 65% 15%, white 1px, aqua 3%, transparent 60%, transparent 100%);
  box-shadow: inset 0 0 6px rgba(255, 255, 255, 0.5);
  bottom: -120px;
  animation: float-up-footer var(--transition-slow) ease-in-out infinite;
  opacity: 0.2;
}

@keyframes float-up-footer {
  0% {
    transform: translateY(0) scale(1);
    opacity: 0.2;
  }
  50% {
    transform: translateY(-50px) scale(1.1);
    opacity: 0.3;
  }
  100% {
    transform: translateY(-100px) scale(0.8);
    opacity: 0;
  }
}

@media (max-width: 992px) {
  .footer-section {
    width: 30%;
  }
}

@media (max-width: 768px) {
  .footer-section {
    width: 100%;
  }
  
  .footer-bottom {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }
  
  .footer-links {
    justify-content: center;
    flex-wrap: wrap;
  }
}
</style> 