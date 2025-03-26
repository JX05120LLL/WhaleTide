package com.whale_tide.dto.client.product;


import com.whale_tide.common.api.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * 获取品牌列表请求,需要分页
 */

@Data
@ApiModel("获取品牌列表")
public class BrandProductRequest extends PageRequest {

    @ApiModelProperty("品牌ID")
    private Long brandId;

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
