package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.entity.ProductCategory;
import com.heritage.mapper.ProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryMapper categoryMapper;

    public List<ProductCategory> getAllCategories() {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductCategory::getStatus, 1);
        wrapper.orderByAsc(ProductCategory::getSort);
        return categoryMapper.selectList(wrapper);
    }

    public Page<ProductCategory> getCategoryPage(Integer page, Integer size) {
        Page<ProductCategory> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSort);
        return categoryMapper.selectPage(pageParam, wrapper);
    }

    public ProductCategory getById(Long id) {
        return categoryMapper.selectById(id);
    }

    public void save(ProductCategory category) {
        categoryMapper.insert(category);
    }

    public void update(ProductCategory category) {
        categoryMapper.updateById(category);
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    public void updateSort(List<ProductCategory> categories) {
        for (ProductCategory category : categories) {
            ProductCategory entity = new ProductCategory();
            entity.setId(category.getId());
            entity.setSort(category.getSort());
            categoryMapper.updateById(entity);
        }
    }
}
