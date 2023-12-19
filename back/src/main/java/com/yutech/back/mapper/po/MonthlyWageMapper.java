package com.yutech.back.mapper.po;

import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import com.yutech.back.entity.po.MonthlyWage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
@Mapper
public interface MonthlyWageMapper extends MppBaseMapper<MonthlyWage> {

	@Update("update MonthlyWage set BaseWage=#{baseWage},DayWage=#{dayWage},OvertimeWage=#{overtimeWage},SeniorityWage=#{seniorityWage} where EmpID=#{empID} and WageDate=#{wageDate}")
	boolean myUpdateByMultiId(MonthlyWage monthlyWage);

}
