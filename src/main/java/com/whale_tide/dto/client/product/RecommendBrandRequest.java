package com.whale_tide.dto.client.product;


import com.whale_tide.common.api.PageRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel("推荐品牌列表请求")
public class RecommendBrandRequest extends PageRequest {

    // 手动添加getter和setter方法
    public Long getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }


}
