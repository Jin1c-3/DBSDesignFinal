package com.yutech.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yutech.back.common.utils.JwtUtil;
import com.yutech.back.common.utils.Result;
import com.yutech.back.entity.dto.LoginDTO;
import com.yutech.back.entity.po.Users;
import com.yutech.back.entity.vo.UserVO;
import com.yutech.back.service.persistence.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@CrossOrigin
public class LoginController {

	@Autowired
	private UsersService usersService;

	/**
	 * 用户登录
	 *
	 * @param loginDTO
	 * @return token
	 */
	@PostMapping("/login")
	public Result<UserVO> userLogin(@RequestBody LoginDTO loginDTO) {
		log.debug("用户登录，前端信息===={}", loginDTO);
		Users userInDB = usersService.getOne(new QueryWrapper<Users>().eq("username", loginDTO.getUsername()));
		if (userInDB == null) {
			return Result.error(new UserVO()).message("用户不存在");
		}
		if (!userInDB.getUserPassword().equals(loginDTO.getPassword())) {
			return Result.error(new UserVO()).message("密码错误");
		}
		log.debug("用户登录成功，用户信息===={}", userInDB);
		return Result.ok(new UserVO(userInDB, JwtUtil.sign(userInDB.getUserName(), userInDB.getUserPassword() + userInDB.getUserRightLevel()))).message("登录成功");
	}

	@GetMapping("/info")
	public Result<UserVO> info(HttpServletRequest request) {
		log.debug("获取用户信息==={}", request);
		String name = JwtUtil.getIdByToken(request);
		Users userInDB = usersService.getOne(new QueryWrapper<Users>().eq("username", name));
		if (userInDB == null) {
			return Result.error(new UserVO()).message("用户不存在");
		}
		log.debug("用户登录成功，用户信息===={}", userInDB);
		return Result.ok(new UserVO(userInDB, JwtUtil.sign(userInDB.getUserName(), userInDB.getUserPassword() + userInDB.getUserRightLevel()))).message("用户信息获取成功");
	}

	@GetMapping("/logout")
	public Result logout() {
		return Result.ok();
	}
}
