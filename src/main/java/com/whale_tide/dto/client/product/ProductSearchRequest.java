package com.whale_tide.dto.client.product;

import com.whale_tide.common.api.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品搜索请求参数
 */
@Data
@ApiModel(description = "产品搜索请求")
public class ProductSearchRequest extends PageRequest {

    @ApiModelProperty("关键词")
    private String keyword;
    @ApiModelProperty("品牌ID")
    private Long brandId;
    @ApiModelProperty("产品分类ID")
    private Long productCategoryId;
    @ApiModelProperty("排序字段")
    private String sortField;
    @ApiModelProperty("排序顺序,asc/desc")
    private String order;

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
