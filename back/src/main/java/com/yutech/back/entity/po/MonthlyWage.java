package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("MonthlyWage")
public class MonthlyWage implements Serializable {

	private static final long serialVersionUID = 1L;

	@MppMultiId
	@TableField("EmpID")
	private Integer empID;

	@MppMultiId
	@TableField("WageDate")
	private String wageDate;

	/**
	 * 基本工资
	 */
	@TableField("BaseWage")
	private BigDecimal baseWage;

	/**
	 * 日薪合计
	 */
	@TableField("DayWage")
	private BigDecimal dayWage;

	/**
	 * 加班工资
	 */
	@TableField("OvertimeWage")
	private BigDecimal overtimeWage;

	/**
	 * 工龄工资
	 */
	@TableField("SeniorityWage")
	private BigDecimal seniorityWage;

	/**
	 * 出差补贴
	 */
	@TableField("BusinessTripWage")
	private BigDecimal businessTripWage;

	/**
	 * 旷工扣款
	 */
	@TableField("AbsenceWage")
	private BigDecimal absenceWage;

	/**
	 * 迟到扣款
	 */
	@TableField("LateWage")
	private BigDecimal lateWage;

	/**
	 * 奖励工资
	 */
	@TableField("BonusWage")
	private BigDecimal bonusWage;

	/**
	 * 其他工资
	 */
	@TableField("EtcWage")
	private BigDecimal etcWage;

	/**
	 * 工资总和
	 */
	@TableField(value = "TotalWage")
	private BigDecimal totalWage;


}
