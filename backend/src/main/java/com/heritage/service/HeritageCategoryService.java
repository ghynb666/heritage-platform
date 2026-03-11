package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.entity.HeritageCategory;
import com.heritage.mapper.HeritageCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeritageCategoryService {
    @Autowired
    private HeritageCategoryMapper categoryMapper;

    public List<HeritageCategory> getAllCategories() {
        LambdaQueryWrapper<HeritageCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HeritageCategory::getStatus, 1)
                .orderByAsc(HeritageCategory::getSort);
        return categoryMapper.selectList(wrapper);
    }

    public Page<HeritageCategory> getCategoryPage(Integer page, Integer size) {
        Page<HeritageCategory> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HeritageCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(HeritageCategory::getSort);
        return categoryMapper.selectPage(pageParam, wrapper);
    }

    public HeritageCategory getById(Long id) {
        return categoryMapper.selectById(id);
    }

    public void save(HeritageCategory category) {
        categoryMapper.insert(category);
    }

    public void update(HeritageCategory category) {
        categoryMapper.updateById(category);
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
