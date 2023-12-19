package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 1
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("DepartmentManager")
public class DepartmentManager implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("DepartID")
    private Integer departID;

    @TableField("DepartName")
    private String departName;

    @TableField("EmpID")
    private Integer empID;

    @TableField("EmpName")
    private String empName;


}
