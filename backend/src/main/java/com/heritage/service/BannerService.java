package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heritage.dto.BannerDTO;
import com.heritage.dto.SortItemDTO;
import com.heritage.entity.Banner;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.BannerMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {
    @Autowired
    private BannerMapper bannerMapper;

    public List<Banner> getList() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Banner::getSort)
                .orderByDesc(Banner::getCreateTime);
        return bannerMapper.selectList(wrapper);
    }

    public void add(BannerDTO dto) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(dto, banner);
        fillDefaults(banner);
        bannerMapper.insert(banner);
    }

    public void update(BannerDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("轮播图ID不能为空");
        }
        Banner banner = requireById(dto.getId());
        BeanUtils.copyProperties(dto, banner);
        fillDefaults(banner);
        bannerMapper.updateById(banner);
    }

    public void delete(Long id) {
        requireById(id);
        bannerMapper.deleteById(id);
    }

    public void updateSort(List<SortItemDTO> items) {
        if (items == null) {
            return;
        }
        for (SortItemDTO item : items) {
            if (item.getId() == null) {
                continue;
            }
            Banner banner = requireById(item.getId());
            banner.setSort(item.getSort() == null ? 0 : item.getSort());
            bannerMapper.updateById(banner);
        }
    }

    private Banner requireById(Long id) {
        Banner banner = bannerMapper.selectById(id);
        if (banner == null) {
            throw new BusinessException("轮播图不存在");
        }
        return banner;
    }

    private void fillDefaults(Banner banner) {
        if (banner.getLinkType() == null) {
            banner.setLinkType(0);
        }
        if (banner.getSort() == null) {
            banner.setSort(0);
        }
        if (banner.getStatus() == null) {
            banner.setStatus(1);
        }
    }
}
