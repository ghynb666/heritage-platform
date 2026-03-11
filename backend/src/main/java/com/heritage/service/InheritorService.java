package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.UserQueryDTO;
import com.heritage.entity.HeritageCategory;
import com.heritage.entity.SysUser;
import com.heritage.entity.SysUserRole;
import com.heritage.mapper.HeritageCategoryMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.mapper.UserRoleMapper;
import com.heritage.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InheritorService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private HeritageCategoryMapper heritageCategoryMapper;

    public Page<UserVO> getInheritorPage(UserQueryDTO query) {
        List<Long> inheritorUserIds = userRoleMapper.selectUserIdsByRoleKey("INHERITOR");
        if (inheritorUserIds.isEmpty()) {
            return new Page<>();
        }

        Page<SysUser> page = new Page<>(query.getPage(), query.getSize());
        
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getId, inheritorUserIds);
        
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w
                .like(SysUser::getUsername, query.getKeyword())
                .or()
                .like(SysUser::getNickname, query.getKeyword())
            );
        }
        
        if (query.getHeritageCategoryId() != null) {
            wrapper.eq(SysUser::getHeritageCategoryId, query.getHeritageCategoryId());
        }
        
        wrapper.orderByDesc(SysUser::getCreateTime);
        
        Page<SysUser> userPage = userMapper.selectPage(page, wrapper);
        
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    public UserVO getInheritorDetail(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Transactional
    public void updateCategory(Long id, Long categoryId) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setHeritageCategoryId(categoryId);
        userMapper.updateById(user);
    }

    @Transactional
    public void revokeInheritor(Long id) {
        Long inheritorRoleId = userRoleMapper.selectRoleIdByRoleKey("INHERITOR");
        if (inheritorRoleId != null) {
            LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUserRole::getUserId, id);
            wrapper.eq(SysUserRole::getRoleId, inheritorRoleId);
            userRoleMapper.delete(wrapper);
        }

        SysUser user = new SysUser();
        user.setId(id);
        user.setHeritageCategoryId(null);
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
            vo.setRoleNames(roles.stream()
                    .map(this::getRoleName)
                    .collect(Collectors.joining("、")));
        }
        
        return vo;
    }

    private String getRoleName(String roleKey) {
        switch (roleKey) {
            case "USER": return "普通用户";
            case "INHERITOR": return "传承人";
            case "ADMIN": return "管理员";
            default: return roleKey;
        }
    }
}
