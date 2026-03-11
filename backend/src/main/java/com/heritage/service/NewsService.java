package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.NewsDTO;
import com.heritage.entity.News;
import com.heritage.entity.SysUser;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.NewsMapper;
import com.heritage.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    public Page<News> getPage(Integer page, Integer size, Integer status, String keyword) {
        Page<News> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(News::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(News::getTitle, keyword).or().like(News::getSummary, keyword));
        }
        wrapper.orderByDesc(News::getIsTop)
                .orderByDesc(News::getCreateTime);
        return newsMapper.selectPage(pageParam, wrapper);
    }

    public News getDetail(Long id) {
        return requireById(id);
    }

    public void add(NewsDTO dto, Long userId) {
        News news = new News();
        BeanUtils.copyProperties(dto, news);
        fillAuthor(news, userId);
        fillDefaults(news);
        newsMapper.insert(news);
    }

    public void update(Long id, NewsDTO dto) {
        News news = requireById(id);
        BeanUtils.copyProperties(dto, news);
        fillDefaults(news);
        newsMapper.updateById(news);
    }

    public void delete(Long id) {
        requireById(id);
        newsMapper.deleteById(id);
    }

    public void audit(Long id, Integer status, String auditReason) {
        if (status == null) {
            throw new BusinessException("审核状态不能为空");
        }
        if (status != 1 && status != 2) {
            throw new BusinessException("审核状态不合法");
        }
        if (status == 2 && !StringUtils.hasText(auditReason)) {
            throw new BusinessException("拒绝时请填写原因");
        }
        News news = requireById(id);
        news.setStatus(status);
        newsMapper.updateById(news);
    }

    public void updateTop(Long id, Integer isTop) {
        News news = requireById(id);
        news.setIsTop(Integer.valueOf(1).equals(isTop) ? 1 : 0);
        newsMapper.updateById(news);
    }

    private News requireById(Long id) {
        News news = newsMapper.selectById(id);
        if (news == null) {
            throw new BusinessException("资讯不存在");
        }
        return news;
    }

    private void fillAuthor(News news, Long userId) {
        SysUser user = userMapper.selectById(userId);
        news.setAuthorId(userId);
        news.setAuthorType(0);
        news.setAuthorName(user == null ? "管理员" : (StringUtils.hasText(user.getNickname()) ? user.getNickname() : user.getUsername()));
    }

    private void fillDefaults(News news) {
        if (news.getStatus() == null) {
            news.setStatus(1);
        }
        if (news.getIsTop() == null) {
            news.setIsTop(0);
        }
        if (news.getViewCount() == null) {
            news.setViewCount(0);
        }
        if (news.getLikeCount() == null) {
            news.setLikeCount(0);
        }
        if (news.getCommentCount() == null) {
            news.setCommentCount(0);
        }
    }
}
