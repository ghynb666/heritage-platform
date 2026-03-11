package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.UserQueryDTO;
import com.heritage.dto.UserUpdateDTO;
import com.heritage.entity.HeritageCategory;
import com.heritage.entity.SysUser;
import com.heritage.entity.SysUserRole;
import com.heritage.mapper.HeritageCategoryMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.mapper.UserRoleMapper;
import com.heritage.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private HeritageCategoryMapper heritageCategoryMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserVO getUserById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    public UserVO getUserDetail(Long id) {
        return getUserById(id);
    }

    public List<String> getUserRoles(Long userId) {
        return userRoleMapper.selectRoleKeysByUserId(userId);
    }

    public Page<UserVO> getUserPage(UserQueryDTO query) {
        Page<SysUser> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(SysUser::getUsername, query.getKeyword())
                    .or()
                    .like(SysUser::getNickname, query.getKeyword())
                    .or()
                    .like(SysUser::getPhone, query.getKeyword()));
        }

        if (query.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, query.getStatus());
        }

        if (StringUtils.hasText(query.getRole())) {
            List<Long> userIds = userRoleMapper.selectUserIdsByRoleKey(query.getRole());
            if (userIds.isEmpty()) {
                return new Page<>();
            }
            wrapper.in(SysUser::getId, userIds);
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = userMapper.selectPage(page, wrapper);
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    public void updateUser(Long id, UserUpdateDTO dto) {
        SysUser existing = userMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }

        SysUser user = new SysUser();
        user.setId(id);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setProvince(dto.getProvince());
        user.setCity(dto.getCity());
        user.setArea(dto.getArea());
        user.setAddress(dto.getAddress());
        user.setSignature(dto.getSignature());
        user.setHeritageCategoryId(dto.getHeritageCategoryId());
        user.setBirthday(dto.getBirthday() == null ? (LocalDateTime) null : dto.getBirthday().atStartOfDay());
        userMapper.updateById(user);
    }

    public void updateUserStatus(Long id, Integer status) {
        List<String> roles = userRoleMapper.selectRoleKeysByUserId(id);
        if (roles.contains("ADMIN")) {
            throw new RuntimeException("不能禁用管理员账号");
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setStatus(status);
        userMapper.updateById(user);
    }

    public void deleteUser(Long id) {
        List<String> roles = userRoleMapper.selectRoleKeysByUserId(id);
        if (roles.contains("ADMIN")) {
            throw new RuntimeException("不能删除管理员账号");
        }
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
        userMapper.deleteById(id);
    }

    public void resetPassword(Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(user);
    }

    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        if (user.getHeritageCategoryId() != null) {
            HeritageCategory category = heritageCategoryMapper.selectById(user.getHeritageCategoryId());
            if (category != null) {
                vo.setHeritageCategoryName(category.getName());
            }
        }

        List<String> roles = userRoleMapper.selectRoleKeysByUserId(user.getId());
        vo.setRoles(roles);
        if (!roles.isEmpty()) {
            vo.setRoleNames(roles.stream().map(this::getRoleName).collect(Collectors.joining("、")));
        }
        return vo;
    }

    private String getRoleName(String roleKey) {
        switch (roleKey) {
            case "USER":
                return "普通用户";
            case "INHERITOR":
                return "传承人";
            case "ADMIN":
                return "管理员";
            default:
                return roleKey;
        }
    }
}
