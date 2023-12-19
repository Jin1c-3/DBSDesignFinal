package com.yutech.back.service.persistence.impl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.yutech.back.entity.po.MonthlyWage;
import com.yutech.back.mapper.po.MonthlyWageMapper;
import com.yutech.back.service.persistence.MonthlyWageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
@Service
public class MonthlyWageServiceImpl extends MppServiceImpl<MonthlyWageMapper, MonthlyWage> implements MonthlyWageService {

	public boolean myUpdateByMultiId(MonthlyWage monthlyWage) {
		return baseMapper.myUpdateByMultiId(monthlyWage);
	}
}
