package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.AnnouncementDTO;
import com.heritage.entity.Announcement;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.AnnouncementMapper;
import com.heritage.vo.AnnouncementVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AnnouncementAdminService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    public Page<AnnouncementVO> getPage(Integer page, Integer size) {
        Page<Announcement> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Announcement::getSort)
                .orderByDesc(Announcement::getCreateTime);
        Page<Announcement> entityPage = announcementMapper.selectPage(pageParam, wrapper);
        Page<AnnouncementVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    public void add(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(dto, announcement);
        fillDefaults(announcement);
        announcementMapper.insert(announcement);
    }

    public void update(AnnouncementDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("公告ID不能为空");
        }
        Announcement announcement = requireById(dto.getId());
        BeanUtils.copyProperties(dto, announcement);
        fillDefaults(announcement);
        announcementMapper.updateById(announcement);
    }

    public void delete(Long id) {
        requireById(id);
        announcementMapper.deleteById(id);
    }

    public void updateTop(Long id, Integer isTop) {
        Announcement announcement = requireById(id);
        int currentSort = announcement.getSort() == null ? 0 : announcement.getSort();
        if (Integer.valueOf(1).equals(isTop)) {
            announcement.setSort(currentSort > 0 ? -currentSort : (currentSort == 0 ? -1 : currentSort));
        } else {
            announcement.setSort(currentSort < 0 ? Math.abs(currentSort) : currentSort);
        }
        announcementMapper.updateById(announcement);
    }

    private Announcement requireById(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        return announcement;
    }

    private void fillDefaults(Announcement announcement) {
        if (announcement.getType() == null) {
            announcement.setType(0);
        }
        if (announcement.getStatus() == null) {
            announcement.setStatus(1);
        }
        if (announcement.getSort() == null) {
            announcement.setSort(0);
        }
    }

    private AnnouncementVO toVO(Announcement announcement) {
        AnnouncementVO vo = new AnnouncementVO();
        BeanUtils.copyProperties(announcement, vo);
        vo.setIsTop(announcement.getSort() != null && announcement.getSort() < 0 ? 1 : 0);
        return vo;
    }
}
