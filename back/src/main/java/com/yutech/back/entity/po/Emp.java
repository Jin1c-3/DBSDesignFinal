package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@TableName("Emp")
public class Emp implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "EmpID", type = IdType.AUTO)
	private Integer empID;

	@TableField("EmpName")
	private String empName;

	@TableField("EmpSex")
	private String empSex;

	@TableField("EmpBirth")
	private String empBirth;

	/**
	 * 职务
	 */
	@TableField("Duty")
	private String duty;

	/**
	 * 琐事部门ID
	 */
	@TableField("DepartID")
	private Integer departID;

	/**
	 * 身份证号
	 */
	@TableField("IdentityID")
	private String identityID;

	@TableField("EmpTele")
	private String empTele;

	/**
	 * 员工图片
	 */
	@TableField("EmpPic")
	private byte[] empPic;

	/**
	 * 备注
	 */
	@TableField("EmpRemark")
	private String empRemark;

	/**
	 * 入职时间
	 */
	@TableField("HireDate")
	private String hireDate;

	/**
	 * 离职时间
	 */
	@TableField("LeaveDate")
	private String leaveDate;

	/**
	 * 逻辑删除标志
	 */
	@TableField("LogicalDelete")
	@TableLogic
	private Integer logicalDelete;


}
