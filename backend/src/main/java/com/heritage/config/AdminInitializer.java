package com.heritage.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heritage.entity.SysRole;
import com.heritage.entity.SysUser;
import com.heritage.entity.SysUserRole;
import com.heritage.mapper.RoleMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void run(String... args) {
        initAdmin();
    }

    private void initAdmin() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, "admin");
        if (userMapper.selectCount(wrapper) == 0) {
            SysUser admin = new SysUser();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNickname("管理员");
            admin.setStatus(1);
            userMapper.insert(admin);

            SysRole adminRole = roleMapper.selectOne(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, "ADMIN")
            );
            if (adminRole != null) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(admin.getId());
                userRole.setRoleId(adminRole.getId());
                userRoleMapper.insert(userRole);
            }
        }
    }
}
