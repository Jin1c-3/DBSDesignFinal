package com.yutech.back.service.persistence.impl;

import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.yutech.back.entity.po.DailyCheckStat;
import com.yutech.back.mapper.po.DailyCheckStatMapper;
import com.yutech.back.service.persistence.DailyCheckStatService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
@Service
public class DailyCheckStatServiceImpl extends MppServiceImpl<DailyCheckStatMapper, DailyCheckStat> implements DailyCheckStatService {

}
