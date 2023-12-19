package com.yutech.back.mapper.po;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yutech.back.entity.po.Emp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jin1c-3
 * @since 2023-04-16
 */
@Mapper
public interface EmpMapper extends BaseMapper<Emp> {

	@Insert("insert into Emp(EmpName,EmpSex,EmpBirth,Duty,DepartID,IdentityID,EmpTele,EmpPic) values(#{empName},#{empSex},#{empBirth},#{duty},#{departID},#{identityID},#{empTele},#{empPic})")
	boolean save(Emp emp);
}
