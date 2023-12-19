package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Users")
public class Users implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("UserName")
    private String userName;

    @TableField("UserPassword")
    private String userPassword;

    /**
     * 用户权限
     */
    @TableField("UserRight")
    private String userRight;

    /**
     * 用户权限等级
     */
    @TableField("UserRightLevel")
    private Integer userRightLevel;


}
