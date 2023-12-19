package com.yutech.back.service.persistence;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yutech.back.entity.po.Emp;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
public interface EmpService extends IService<Emp> {

	@Override
	boolean save(Emp emp);

	boolean mySaveBatch(Iterable<Emp> empList);
}
