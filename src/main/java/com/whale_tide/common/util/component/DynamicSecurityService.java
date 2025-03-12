package com.whale_tide.common.util.component;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 动态权限相关业务类
 * Created by macro on 2020/2/7.
 */
@Service
public class DynamicSecurityService {

    /**
     * 加载资源ANT通配符和资源对应MAP
     */
    public Map<String, ConfigAttribute> loadDataSource() {
        // 这里实现具体的逻辑，返回资源与权限的映射
        // 例如：
        // Map<String, ConfigAttribute> dataSource = new HashMap<>();
        // dataSource.put("/admin/**", new SecurityConfig("ADMIN"));
        // return dataSource;
        return null; // 暂时返回 null，你需要根据实际需求实现该方法
    }
}
