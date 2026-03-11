package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heritage.entity.HeritageCategory;
import com.heritage.entity.SysUser;
import com.heritage.mapper.HeritageCategoryMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.mapper.UserRoleMapper;
import com.heritage.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private HeritageCategoryMapper heritageCategoryMapper;

    public UserVO getUserById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        if (user.getHeritageCategoryId() != null) {
            HeritageCategory category = heritageCategoryMapper.selectById(user.getHeritageCategoryId());
            if (category != null) {
                vo.setHeritageCategoryName(category.getName());
            }
        }
        return vo;
    }

    public List<String> getUserRoles(Long userId) {
        return userRoleMapper.selectRoleKeysByUserId(userId);
    }
}
