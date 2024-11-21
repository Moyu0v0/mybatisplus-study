package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Address
 *
 * @author sundae
 * @date 2024/11/21
 * @description
 */
@Data
@TableName("address")
public class AddressPO {
    @TableId
    private Long id;

    private Long userId;

    private String province;

    private String city;

    private String town;

    private String mobile;

    private String street;

    private String contact;

    @TableField("is_default")
    private Boolean isDefault;

    private String notes;

    private Boolean deleted;
}

