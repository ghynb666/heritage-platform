package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heritage.entity.SysRegion;
import com.heritage.mapper.RegionMapper;
import com.heritage.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegionService {
    @Autowired
    private RegionMapper regionMapper;

    public List<RegionVO> getRegionTree() {
        List<SysRegion> allRegions = regionMapper.selectList(
                new LambdaQueryWrapper<SysRegion>().orderByAsc(SysRegion::getSort)
        );

        List<RegionVO> voList = allRegions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Map<Long, List<RegionVO>> parentMap = voList.stream()
                .collect(Collectors.groupingBy(RegionVO::getParentId));

        voList.forEach(vo -> {
            List<RegionVO> children = parentMap.get(vo.getId());
            if (children != null) {
                vo.setChildren(children);
            }
        });

        return voList.stream()
                .filter(vo -> vo.getParentId() == null || vo.getParentId() == 0)
                .collect(Collectors.toList());
    }

    public void addRegion(SysRegion region) {
        regionMapper.insert(region);
    }

    public void updateRegion(SysRegion region) {
        regionMapper.updateById(region);
    }

    public void deleteRegion(Long id) {
        regionMapper.deleteById(id);
    }

    private RegionVO convertToVO(SysRegion region) {
        RegionVO vo = new RegionVO();
        BeanUtils.copyProperties(region, vo);
        return vo;
    }
}
