package com.yutech.back.entity.po;

import java.math.BigDecimal;
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
@TableName("EmpMonthlyWage")
public class EmpMonthlyWage implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("EmpID")
    private Integer empID;

    @TableField("EmpName")
    private String empName;

    @TableField("DepartName")
    private String departName;

    @TableField("Duty")
    private String duty;

    @TableField("WageMonth")
    private String wageMonth;

    @TableField("TotalWage")
    private BigDecimal totalWage;

    @TableField("BaseWage")
    private BigDecimal baseWage;

    @TableField("DayWage")
    private BigDecimal dayWage;

    @TableField("OvertimeWage")
    private BigDecimal overtimeWage;

    @TableField("SeniorityWage")
    private BigDecimal seniorityWage;

    @TableField("BusinessTripWage")
    private BigDecimal businessTripWage;

    @TableField("AbsenceWage")
    private BigDecimal absenceWage;

    @TableField("LateWage")
    private BigDecimal lateWage;

    @TableField("BonusWage")
    private BigDecimal bonusWage;

    @TableField("EtcWage")
    private BigDecimal etcWage;


}
