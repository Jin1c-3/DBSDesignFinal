package com.yutech.back.entity.po;

import java.math.BigDecimal;
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
@TableName("WageConfig")
public class WageConfig implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 职务
     */
    @TableId("Duty")
    private String duty;

    /**
     * 基本工资
     */
    @TableField("BaseWage")
    private BigDecimal baseWage;

    /**
     * 日薪
     */
    @TableField("DayWage")
    private BigDecimal dayWage;

    /**
     * 加班补贴比率
     */
    @TableField("OvertimeRatio")
    private Float overtimeRatio;

    /**
     * 标准出差补贴
     */
    @TableField("BusinessTripStd")
    private BigDecimal businessTripStd;

    /**
     * 标准旷工扣款
     */
    @TableField("AbsenceStd")
    private BigDecimal absenceStd;

    /**
     * 标准迟到扣款
     */
    @TableField("LateStd")
    private BigDecimal lateStd;

    /**
     * 标准奖励
     */
    @TableField("Bonus")
    private BigDecimal bonus;

    /**
     * 其他
     */
    @TableField("EtcStd")
    private BigDecimal etcStd;


}
