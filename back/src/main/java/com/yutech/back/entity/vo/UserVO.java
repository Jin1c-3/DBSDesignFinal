package com.yutech.back.entity.vo;

import com.yutech.back.entity.po.Users;
import lombok.Data;

@Data
public class UserVO {
	private String token;
	private String name;

	private String pwd;

	/**
	 * 用户权限
	 */
	private Integer userRightLevel;

	/**
	 * 用户权限等级
	 */
	private String[] roles;

	public UserVO() {
	}

	public UserVO(Users user, String token) {
		this.token = token;
		this.name = user.getUserName();
		this.pwd = user.getUserPassword();
		this.userRightLevel = user.getUserRightLevel();
		this.roles = new String[]{String.valueOf(user.getUserRightLevel())};
	}
}
