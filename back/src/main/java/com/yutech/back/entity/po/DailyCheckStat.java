package com.yutech.back.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
@TableName("DailyCheckStat")
public class DailyCheckStat implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableField("EmpID")
	@MppMultiId
	private Integer empID;

	/**
	 * 每日考勤日期
	 */
	@MppMultiId
	@TableField("CheckDate")
	private String checkDate;

	/**
	 * 加班小时数
	 */
	@TableField("OvertimeHour")
	private Float overtimeHour;

	/**
	 * 是否上班
	 */
	@TableField("WorkDay")
	private Integer workDay;

	/**
	 * 是否出差
	 */
	@TableField("BusinessTripDay")
	private Integer businessTripDay;

	/**
	 * 是否旷工
	 */
	@TableField("AbsenceDay")
	private Integer absenceDay;

	/**
	 * 迟到次数
	 */
	@TableField("LateDay")
	private Integer lateDay;


}
