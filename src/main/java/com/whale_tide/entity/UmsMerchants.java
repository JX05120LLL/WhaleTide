package com.whale_tide.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商家表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_merchants")
public class UmsMerchants implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺logo
     */
    private String shopLogo;

    /**
     * 店铺横幅
     */
    private String shopBanner;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 身份证正面
     */
    private String identityCardFront;

    /**
     * 身份证背面
     */
    private String identityCardBack;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 经营类目，多个用逗号分隔
     */
    private String businessCategories;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 店铺地址
     */
    private String shopAddress;

    /**
     * 省份
     */
    private String shopProvince;

    /**
     * 城市
     */
    private String shopCity;

    /**
     * 区县
     */
    private String shopDistrict;

    /**
     * 邮政编码
     */
    private String shopPostalCode;

    /**
     * 开户名
     */
    private String bankAccountName;

    /**
     * 银行账号
     */
    private String bankAccountNumber;

    /**
     * 开户银行
     */
    private String bankName;

    /**
     * 支行名称
     */
    private String bankBranch;

    /**
     * 状态：0-待审核，1-审核通过，2-审核拒绝，3-冻结
     */
    private Integer status;

    /**
     * 状态原因
     */
    private String statusReason;

    /**
     * 店铺评分
     */
    private BigDecimal rating;

    /**
     * 服务评分
     */
    private BigDecimal serviceRating;

    /**
     * 物流评分
     */
    private BigDecimal deliveryRating;

    /**
     * 描述相符评分
     */
    private BigDecimal descriptionRating;

    /**
     * 总销量
     */
    private Integer totalSales;

    /**
     * 商品总数
     */
    private Integer totalProductCount;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否删除：0-否，1-是
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
