package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.InheritorApplyDTO;
import com.heritage.entity.HeritageCategory;
import com.heritage.entity.InheritorApply;
import com.heritage.entity.SysRole;
import com.heritage.entity.SysUser;
import com.heritage.entity.SysUserRole;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.HeritageCategoryMapper;
import com.heritage.mapper.InheritorApplyMapper;
import com.heritage.mapper.RoleMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.mapper.UserRoleMapper;
import com.heritage.vo.InheritorApplyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InheritorApplyService {
    @Autowired
    private InheritorApplyMapper applyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private HeritageCategoryMapper heritageCategoryMapper;

    public void apply(Long userId, InheritorApplyDTO dto) {
        LambdaQueryWrapper<InheritorApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InheritorApply::getUserId, userId)
               .in(InheritorApply::getStatus, 0, 1);
        if (applyMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("您已提交过申请，请勿重复提交");
        }

        InheritorApply apply = new InheritorApply();
        apply.setUserId(userId);
        apply.setRealName(dto.getRealName());
        apply.setIdCard(dto.getIdCard());
        apply.setCertificateImages(dto.getCertificateImages());
        apply.setDescription(dto.getDescription());
        apply.setHeritageCategoryId(dto.getHeritageCategoryId());
        apply.setStatus(0);
        applyMapper.insert(apply);
    }

    public InheritorApplyVO getApplyStatus(Long userId) {
        LambdaQueryWrapper<InheritorApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InheritorApply::getUserId, userId)
               .orderByDesc(InheritorApply::getCreateTime)
               .last("LIMIT 1");
        InheritorApply apply = applyMapper.selectOne(wrapper);
        if (apply == null) {
            return null;
        }
        return convertToVO(apply);
    }

    public Page<InheritorApplyVO> getApplyList(Integer page, Integer size, Integer status) {
        Page<InheritorApply> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<InheritorApply> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(InheritorApply::getStatus, status);
        }
        wrapper.orderByDesc(InheritorApply::getCreateTime);

        Page<InheritorApply> result = applyMapper.selectPage(pageParam, wrapper);

        Page<InheritorApplyVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<InheritorApplyVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    public InheritorApplyVO getApplyDetail(Long id) {
        InheritorApply apply = applyMapper.selectById(id);
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        return convertToVO(apply);
    }

    @Transactional
    public void approve(Long id, Long reviewerId) {
        InheritorApply apply = applyMapper.selectById(id);
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        if (apply.getStatus() != 0) {
            throw new BusinessException("该申请已处理");
        }

        apply.setStatus(1);
        apply.setReviewerId(reviewerId);
        apply.setReviewTime(LocalDateTime.now());
        applyMapper.updateById(apply);

        SysRole inheritorRole = roleMapper.selectOne(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, "INHERITOR"));
        if (inheritorRole != null) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(apply.getUserId());
            userRole.setRoleId(inheritorRole.getId());
            userRoleMapper.insert(userRole);
        }

        SysUser user = userMapper.selectById(apply.getUserId());
        if (user != null) {
            user.setHeritageCategoryId(apply.getHeritageCategoryId());
            userMapper.updateById(user);
        }
    }

    @Transactional
    public void reject(Long id, Long reviewerId, String reason) {
        InheritorApply apply = applyMapper.selectById(id);
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        if (apply.getStatus() != 0) {
            throw new BusinessException("该申请已处理");
        }

        apply.setStatus(2);
        apply.setRejectReason(reason);
        apply.setReviewerId(reviewerId);
        apply.setReviewTime(LocalDateTime.now());
        applyMapper.updateById(apply);
    }

    private InheritorApplyVO convertToVO(InheritorApply apply) {
        InheritorApplyVO vo = new InheritorApplyVO();
        BeanUtils.copyProperties(apply, vo);

        SysUser user = userMapper.selectById(apply.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }

        HeritageCategory category = heritageCategoryMapper.selectById(apply.getHeritageCategoryId());
        if (category != null) {
            vo.setHeritageCategoryName(category.getName());
        }
        return vo;
    }
}
