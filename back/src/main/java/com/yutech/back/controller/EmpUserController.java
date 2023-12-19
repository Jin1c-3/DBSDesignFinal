package com.yutech.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yutech.back.common.utils.Result;
import com.yutech.back.entity.po.DailyCheckStat;
import com.yutech.back.entity.po.DepartmentManager;
import com.yutech.back.entity.po.Emp;
import com.yutech.back.service.persistence.DailyCheckStatService;
import com.yutech.back.service.persistence.DepartmentManagerService;
import com.yutech.back.service.persistence.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/emp-user")
@Slf4j
public class EmpUserController {

	@Autowired
	private EmpService empService;

	@Autowired
	private DailyCheckStatService dailyCheckStatService;

	@Autowired
	private DepartmentManagerService departmentManagerService;

	@PostMapping("/add-emp")
	public Result addEmp(Emp emp, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		emp.setEmpPic(file.getBytes());
//		log.debug("添加员工==={}", emp);
		try {
			empService.save(emp);
		} catch (Exception e) {
//			log.info("员工信息添加失败==={}", emp);
			return Result.error().message("员工信息添加失败");
		}
//		log.debug("添加成功==={}", emp);
		return Result.ok().message("添加成功");
	}

	@PostMapping("/add-empList")
	public Result addEmpList(@RequestBody List<Emp> empList) {
		log.debug("批量添加员工，前端信息==={}", empList);
		try {
			empService.mySaveBatch(empList);
		} catch (Exception e) {
			log.info("员工信息添加失败==={}", empList);
			return Result.error().message("员工信息添加失败");
		}
		log.debug("添加成功==={}", empList);
		return Result.ok().message("批量添加成功");
	}

	@GetMapping("/get-emp-pic")
	public byte[] getEmpPic(Integer empId) {
		log.debug("获取员工头像，前端信息==={}", empId);
		Emp emp = empService.getOne(new QueryWrapper<Emp>().eq("EmpID", empId));
		if (emp == null) {
			log.info("该员工不存在==={}", empId);
			return new byte[]{};
		}
		log.debug("获取成功==={}", empId);
		return emp.getEmpPic();
	}

	@DeleteMapping("/delete-emp")
	public Result deleteEmp(Integer empId) {
		log.debug("删除员工，前端信息==={}", empId);
		if (empService.getOne(new QueryWrapper<Emp>().eq("EmpID", empId)) == null) {
			log.info("该员工不存在==={}", empId);
			return Result.error().message("该员工不存在");
		}
		try {
			empService.removeById(empId);
		} catch (Exception e) {
			log.info("员工信息删除失败==={}", empId);
			return Result.error().message("员工信息删除失败");
		}
		log.debug("删除成功==={}", empId);
		return Result.ok().message("删除成功");
	}

	@GetMapping("/get-emp")
	public Result<List<Emp>> getEmpList() {
		log.debug("获取员工列表");
		List<Emp> empList = empService.list();
		log.debug("获取成功==={}", empList);
		return Result.ok(empList).message("获取成功");
	}

	@PatchMapping("/update-emp")
	public Result updateEmp(@RequestBody Emp emp) {
		log.debug("更新员工信息，前端信息===={}", emp);
		if (empService.getOne(new QueryWrapper<Emp>().eq("EmpID", emp.getEmpID())) == null) {
			log.info("该员工不存在==={}", emp.getEmpID());
			return Result.error().message(" 该员工不存在");
		}
		try {
			empService.updateById(emp);
		} catch (Exception e) {
			log.info("员工信息更新失败==={}", emp);
			return Result.error().message("员工信息更新失败");
		}
		return Result.ok().message("更新成功");
	}

	@PostMapping("/add-daily-check")
	public Result addDailyCheckStat(@RequestBody DailyCheckStat dailyCheckStat) {
		log.debug("添加员工考勤信息，前端信息==={}", dailyCheckStat);
		if (dailyCheckStatService.selectByMultiId(dailyCheckStat) != null) {
			log.info("该员工考勤信息已存在");
			return Result.error().message("该员工考勤信息已存在");
		}
		try {
			dailyCheckStatService.save(dailyCheckStat);
		} catch (Exception e) {
			log.info("员工考勤信息添加失败==={}", dailyCheckStat);
			return Result.error().message("员工考勤信息添加失败");
		}
		log.debug("添加成功==={}", dailyCheckStat);
		return Result.ok().message("添加成功");
	}

	@GetMapping("/get-daily-check")
	public Result<List<DailyCheckStat>> getDailyCheckStatList() {
		log.debug("获取员工考勤信息列表");
		List<DailyCheckStat> dailyCheckStatList = dailyCheckStatService.list();
		log.debug("获取成功==={}", dailyCheckStatList);
		return Result.ok(dailyCheckStatList).message("获取成功");
	}

	@GetMapping("/get-depart-manager")
	public Result<List<DepartmentManager>> getDepartManagerList() {
		List<DepartmentManager> departmentManagerList = departmentManagerService.list();
		log.debug("获取部门经理列表成功");
		return Result.ok(departmentManagerList).message("获取成功");
	}
}
