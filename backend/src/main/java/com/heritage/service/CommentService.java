package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.entity.Comment;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.CommentMapper;
import com.heritage.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public Page<CommentVO> getPage(Integer page, Integer size) {
        Page<Comment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreateTime);
        Page<Comment> entityPage = commentMapper.selectPage(pageParam, wrapper);
        Page<CommentVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    public void delete(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        commentMapper.deleteById(id);
    }

    private CommentVO toVO(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        vo.setTargetTypeName(getTargetTypeName(comment.getTargetType()));
        return vo;
    }

    private String getTargetTypeName(Integer targetType) {
        if (targetType == null) {
            return "未知";
        }
        switch (targetType) {
            case 1:
                return "非遗项目";
            case 2:
                return "文创商品";
            case 3:
                return "资讯";
            case 4:
                return "论坛帖子";
            default:
                return "未知";
        }
    }
}
