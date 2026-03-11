package com.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heritage.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<SysUserRole> {
    @Select("SELECT r.role_key FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0")
    List<String> selectRoleKeysByUserId(@Param("userId") Long userId);

    @Select("SELECT ur.user_id FROM sys_user_role ur " +
            "INNER JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE r.role_key = #{roleKey} AND r.deleted = 0")
    List<Long> selectUserIdsByRoleKey(@Param("roleKey") String roleKey);

    @Select("SELECT ur.role_id FROM sys_user_role ur WHERE ur.user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    @Select("SELECT r.id FROM sys_role r WHERE r.role_key = #{roleKey} AND r.deleted = 0 LIMIT 1")
    Long selectRoleIdByRoleKey(@Param("roleKey") String roleKey);
}
