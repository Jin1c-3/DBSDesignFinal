package com.yutech.back.controller;

import com.yutech.back.common.utils.Result;
import com.yutech.back.entity.po.Users;
import com.yutech.back.service.persistence.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/super-user")
@Slf4j
@CrossOrigin
public class SuperUserController {

	@Autowired
	private UsersService userService;

	@PostMapping("/add-user")
	public Result addUser(@RequestBody Users user) {
		if (userService.getById(userService.getById(user)) != null) {
			return Result.error().message("该用户已存在");
		}
		log.debug("新增用户，前端信息==={}", user);
		userService.save(user);
		return Result.ok().message("添加成功");
	}

	@PostMapping("/add-userlist")
	public Result addUserList(@RequestBody List<Users> userList) {
		log.debug("批量添加员工，前端信息==={}", userList);
		try {
			userService.saveBatch(userList);
		} catch (Exception e) {
			log.info("员工信息添加失败==={}", userList);
			return Result.error().message("员工信息添加失败");
		}
		log.debug("添加成功==={}", userList);
		return Result.ok().message("添加成功");
	}

	@DeleteMapping("/delete-user")
	public Result deleteUser(String userName) {
		if (userService.getById(userName) == null) {
			log.info("该用户不存在==={}", userName);
			return Result.error().message("该用户不存在");
		}
		userService.removeById(userName);
		return Result.ok().message("删除成功");
	}

	@GetMapping("/get-user-list")
	public Result<List<Users>> getUserList() {
		List<Users> userList = userService.list();
		log.debug("用户信息===={}", userList);
		return Result.ok(userList).message("查询成功");
	}

	@PatchMapping("/update-user")
	public Result updateUser(Users user) {
		if (userService.getById(user.getUserName()) == null) {
			log.info("该用户不存在==={}", user.getUserName());
			return Result.error().message("该用户不存在");
		}
		userService.updateById(user);
		return Result.ok().message("更新成功");
	}
}
