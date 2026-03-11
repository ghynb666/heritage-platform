package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.entity.Product;
import com.heritage.entity.ProductCategory;
import com.heritage.entity.SysUser;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.ProductMapper;
import com.heritage.mapper.ProductCategoryMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.vo.ProductVO;
import com.heritage.vo.ProductCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCategoryMapper categoryMapper;

    @Autowired
    private UserMapper userMapper;

    public Page<ProductVO> getAdminProductList(Integer page, Integer size, Integer status, Long categoryId, String keyword) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Product::getStatus, status);
        }
        if (categoryId != null) {
            wrapper.eq(Product::getProductCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        Page<Product> productPage = productMapper.selectPage(pageParam, wrapper);
        return convertToVOPage(productPage);
    }

    public ProductVO getAdminProductDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return null;
        }
        return convertToVO(product);
    }

    public void updateStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }

    public void updateRecommend(Long id, Integer isRecommend) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        product.setIsRecommend(isRecommend);
        productMapper.updateById(product);
    }

    public void deleteProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        productMapper.deleteById(id);
    }

    private Page<ProductVO> convertToVOPage(Page<Product> productPage) {
        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        List<ProductVO> voList = productPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        if (product.getProductCategoryId() != null) {
            ProductCategory category = categoryMapper.selectById(product.getProductCategoryId());
            if (category != null) {
                vo.setProductCategoryName(category.getName());
            }
        }
        if (product.getInheritorId() != null) {
            SysUser inheritor = userMapper.selectById(product.getInheritorId());
            if (inheritor != null) {
                vo.setInheritorName(inheritor.getNickname() != null ? inheritor.getNickname() : inheritor.getUsername());
                vo.setInheritorAvatar(inheritor.getAvatar());
            }
        }
        if (StringUtils.hasText(product.getImages())) {
            vo.setImages(List.of(product.getImages().split(",")));
        }
        return vo;
    }
}
