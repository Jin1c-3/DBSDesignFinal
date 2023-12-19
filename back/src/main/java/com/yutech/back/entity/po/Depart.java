package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("Depart")
public class Depart implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "DepartID", type = IdType.AUTO)
    private Integer departID;

    /**
     * 部门名称
     */
    @TableField("DepartName")
    private String departName;

    /**
     * 部门主管ID
     */
    @TableField("DepartManagerID")
    private Integer departManagerID;


}
