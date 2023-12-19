package com.yutech.back.controller;

import com.yutech.back.common.utils.Result;
import com.yutech.back.entity.po.MonthlyWage;
import com.yutech.back.entity.po.WageConfig;
import com.yutech.back.service.persistence.MonthlyWageService;
import com.yutech.back.service.persistence.WageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wage-user")
@Slf4j
@CrossOrigin
public class WageUserController {

	@Autowired
	private WageConfigService wageConfigService;

	@Autowired
	private MonthlyWageService monthlyWageService;

	@GetMapping("/get-emp-monthly-wage-list")
	public Result<List<MonthlyWage>> getEmpMonthlyWageList() {
		List<MonthlyWage> monthlyWageList = monthlyWageService.list();
		log.debug("获取员工工资信息，前端信息===={}", monthlyWageService);
		return Result.ok(monthlyWageList).message("工资查询成功");
	}

	@PatchMapping("/update-emp-monthly-wage")
	public Result updateEmpMonthlyWage(@RequestBody MonthlyWage monthlyWage) {
		log.debug("更新员工工资信息，前端信息===={}", monthlyWage);
		monthlyWageService.myUpdateByMultiId(monthlyWage);
		return Result.ok().message("更新成功");
	}

	@PostMapping("/add-wage-config")
	public Result addWageConfig(@RequestBody WageConfig wageConfig) {
		log.debug("添加工资配置信息，前端信息===={}", wageConfig);
		if (wageConfigService.getById(wageConfigService.getById(wageConfig)) != null) {
			log.info("该工资配置已存在");
			return Result.error().message("该工资配置已存在");
		}
		try {
			wageConfigService.save(wageConfig);
		} catch (Exception e) {
			log.error("添加工资配置信息失败==={}", e.getMessage());
			return Result.error().message("添加失败");
		}
		log.debug("添加工资配置信息成功==={}", wageConfig);
		return Result.ok().message("添加成功");
	}

	@PostMapping("/add-wage-config-list")
	public Result addWageConfigList(@RequestBody List<WageConfig> wageConfigList) {
		log.debug("添加工资配置信息，前端信息===={}", wageConfigList);
		for (WageConfig wageConfig : wageConfigList) {
			if (wageConfigService.getById(wageConfigService.getById(wageConfig)) != null) {
				log.info("该工资配置已存在");
				return Result.error().message("该工资配置已存在");
			}
		}
		try {
			wageConfigService.saveBatch(wageConfigList);
		} catch (Exception e) {
			log.error("添加工资配置信息失败==={}", e.getMessage());
			return Result.error().message("请检查表格数据，添加失败");
		}
		log.debug("添加工资配置信息成功==={}", wageConfigList);
		return Result.ok().message("添加成功");
	}

	@DeleteMapping("/delete-wage-config")
	public Result deleteWageConfig(@RequestParam String duty) {
		log.debug("删除工资配置信息，前端信息===={}", duty);
		if (wageConfigService.getById(duty) == null) {
			log.info("该工资配置不存在");
			return Result.error().message("该工资配置不存在");
		}
		try {
			wageConfigService.removeById(duty);
		} catch (Exception e) {
			log.info("删除工资配置信息失败==={}", e.getMessage());
			return Result.error().message("删除失败");
		}
		log.debug("删除工资配置信息成功==={}", duty);
		return Result.ok().message("删除成功");
	}

	@GetMapping("/get-wage-config-list")
	public Result<List<WageConfig>> getWageConfigList() {
		List<WageConfig> wageConfigList = wageConfigService.list();
		log.debug("获取工资配置信息，前端信息===={}", wageConfigList);
		return Result.ok(wageConfigList).message("工资配置查询成功");
	}

	@PatchMapping("/update-wage-config")
	public Result updateWageConfig(@RequestBody WageConfig wageConfig) {
		log.debug("更新工资配置信息，前端信息===={}", wageConfig);
		if (wageConfigService.getById(wageConfig.getDuty()) == null) {
			log.info("该工资配置不存在");
			return Result.error().message("该工资配置不存在");
		}
		wageConfigService.updateById(wageConfig);
		return Result.ok().message("更新成功");
	}
}
