package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.entity.ForumPost;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.ForumPostMapper;
import com.heritage.vo.ForumPostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class ForumService {
    @Autowired
    private ForumPostMapper forumPostMapper;

    public Page<ForumPostVO> getPage(Integer page, Integer size, Integer status, String keyword) {
        Page<ForumPost> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(ForumPost::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(ForumPost::getTitle, keyword).or().like(ForumPost::getAuthorName, keyword));
        }
        wrapper.orderByDesc(ForumPost::getIsTop)
                .orderByDesc(ForumPost::getCreateTime);
        Page<ForumPost> entityPage = forumPostMapper.selectPage(pageParam, wrapper);
        Page<ForumPostVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    public ForumPostVO getDetail(Long id) {
        return toVO(requireById(id));
    }

    public void delete(Long id) {
        requireById(id);
        forumPostMapper.deleteById(id);
    }

    public void updateTop(Long id, Integer isTop) {
        ForumPost post = requireById(id);
        post.setIsTop(Integer.valueOf(1).equals(isTop) ? 1 : 0);
        forumPostMapper.updateById(post);
    }

    private ForumPost requireById(Long id) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        return post;
    }

    private ForumPostVO toVO(ForumPost post) {
        ForumPostVO vo = new ForumPostVO();
        BeanUtils.copyProperties(post, vo);
        if (StringUtils.hasText(post.getImages())) {
            vo.setImages(Arrays.asList(post.getImages().split(",")));
        } else {
            vo.setImages(Collections.emptyList());
        }
        return vo;
    }
}
