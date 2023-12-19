package com.yutech.back.service.persistence.impl;

import com.yutech.back.entity.po.Users;
import com.yutech.back.mapper.po.UsersMapper;
import com.yutech.back.service.persistence.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
