package com.yutech.back.service.persistence.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yutech.back.entity.po.Emp;
import com.yutech.back.mapper.po.EmpMapper;
import com.yutech.back.service.persistence.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

	@Autowired
	private EmpMapper EmpMapper;

	@Override
	public boolean save(Emp emp) {
		return EmpMapper.save(emp);
	}

	public boolean mySaveBatch(Iterable<Emp> empList) {
		try {
			for (Emp emp : empList) {
				EmpMapper.save(emp);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
