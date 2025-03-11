package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("brand")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;//品牌名称
    private String logo;//品牌logo
    private String description;//品牌描述


}
