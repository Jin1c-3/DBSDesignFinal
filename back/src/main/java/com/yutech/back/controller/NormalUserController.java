package com.yutech.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yutech.back.common.utils.Result;
import com.yutech.back.entity.po.DailyCheckStat;
import com.yutech.back.entity.po.Emp;
import com.yutech.back.entity.po.MonthlyWage;
import com.yutech.back.entity.po.Users;
import com.yutech.back.service.persistence.DailyCheckStatService;
import com.yutech.back.service.persistence.EmpService;
import com.yutech.back.service.persistence.MonthlyWageService;
import com.yutech.back.service.persistence.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/normal-user")
@Slf4j
@CrossOrigin
public class NormalUserController {

	@Autowired
	private EmpService empService;

	@Autowired
	private DailyCheckStatService dailyCheckStatService;

	@Autowired
	private MonthlyWageService monthlyWageService;

	@Autowired
	private UsersService userService;

	@GetMapping("/get-one-emp")
	public Result<Emp> getOneEmp(Integer empId) {
		log.debug("获取员工信息，前端信息===={}", empId);
		Emp emp = empService.getById(empId);
		return Result.ok(emp).message("获取成功");
	}

	@PatchMapping("/update-emp")
	public Result updateEmp(@RequestBody Emp emp) {
		log.debug("更新员工信息，前端信息===={}", emp);
		empService.updateById(emp);
		return Result.ok().message("更新成功");
	}

	@GetMapping("/get-one-user")
	public Result<Users> getOneUser(String userName) {
		log.debug("获取用户信息，前端信息===={}", userName);
		Users user = userService.getById(userName);
		return Result.ok(user).message("获取成功");
	}

	@PatchMapping("/update-user")
	public Result updateUser(@RequestBody Users user) {
		log.debug("更新员工信息，前端信息===={}", user);
		userService.updateById(user);
		return Result.ok().message("更新成功");
	}

	@GetMapping("/get-one-check-stat")
	public Result<List<DailyCheckStat>> getOneCheckStat(Integer empId) {
		log.debug("获取员工考勤信息，前端信息===={}", empId);
		List<DailyCheckStat> dailyCheckStatList = dailyCheckStatService.list(new QueryWrapper<DailyCheckStat>()
				.eq("EmpID", empId));
		return Result.ok(dailyCheckStatList).message("考勤信息查询成功");
	}

	@GetMapping("/get-one-emp-wage")
	public Result<List<MonthlyWage>> getOneEmpWage(@RequestParam Integer empId) {
		log.debug("获取员工工资信息，前端信息===={}", empId);
		List<MonthlyWage> monthlyWageList = monthlyWageService.list(new QueryWrapper<MonthlyWage>()
				.eq("EmpID", empId));
		return Result.ok(monthlyWageList).message("工资查询成功");
	}
}
