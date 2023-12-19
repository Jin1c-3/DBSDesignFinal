package com.yutech.back.service.persistence;

import com.github.jeffreyning.mybatisplus.service.IMppService;
import com.yutech.back.entity.po.MonthlyWage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
public interface MonthlyWageService extends IMppService<MonthlyWage> {

	boolean myUpdateByMultiId(MonthlyWage monthlyWage);

}
