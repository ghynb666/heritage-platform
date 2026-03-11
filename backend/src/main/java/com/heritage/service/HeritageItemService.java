package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.HeritageImageDTO;
import com.heritage.dto.HeritageItemDTO;
import com.heritage.dto.HeritageVideoDTO;
import com.heritage.entity.HeritageCategory;
import com.heritage.entity.HeritageImage;
import com.heritage.entity.HeritageItem;
import com.heritage.entity.HeritageVideo;
import com.heritage.entity.SysUser;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.HeritageCategoryMapper;
import com.heritage.mapper.HeritageImageMapper;
import com.heritage.mapper.HeritageItemMapper;
import com.heritage.mapper.HeritageVideoMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.vo.HeritageImageVO;
import com.heritage.vo.HeritageItemVO;
import com.heritage.vo.HeritageVideoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeritageItemService {
    @Autowired
    private HeritageItemMapper itemMapper;

    @Autowired
    private HeritageImageMapper imageMapper;

    @Autowired
    private HeritageVideoMapper videoMapper;

    @Autowired
    private HeritageCategoryMapper categoryMapper;

    @Autowired
    private UserMapper userMapper;

    public Page<HeritageItemVO> getItemList(Integer page, Integer size, Long categoryId, String keyword, String sortBy) {
        Page<HeritageItem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HeritageItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HeritageItem::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(HeritageItem::getHeritageCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(HeritageItem::getTitle, keyword);
        }
        if ("viewCount".equals(sortBy)) {
            wrapper.orderByDesc(HeritageItem::getViewCount);
        } else if ("likeCount".equals(sortBy)) {
            wrapper.orderByDesc(HeritageItem::getLikeCount);
        } else if ("favoriteCount".equals(sortBy)) {
            wrapper.orderByDesc(HeritageItem::getFavoriteCount);
        } else {
            wrapper.orderByDesc(HeritageItem::getCreateTime);
        }
        Page<HeritageItem> itemPage = itemMapper.selectPage(pageParam, wrapper);
        return convertToVOPage(itemPage);
    }

    public HeritageItemVO getItemDetail(Long id) {
        HeritageItem item = itemMapper.selectById(id);
        if (item == null || item.getStatus() != 1) {
            return null;
        }
        item.setViewCount(item.getViewCount() + 1);
        itemMapper.updateById(item);
        return convertToVO(item);
    }

    public Page<HeritageItemVO> searchItems(Integer page, Integer size, String keyword) {
        Page<HeritageItem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HeritageItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HeritageItem::getStatus, 1);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(HeritageItem::getTitle, keyword)
                    .or()
                    .like(HeritageItem::getHistoryOrigin, keyword)
                    .or()
                    .like(HeritageItem::getCraftFeature, keyword);
        }
        wrapper.orderByDesc(HeritageItem::getCreateTime);
        Page<HeritageItem> itemPage = itemMapper.selectPage(pageParam, wrapper);
        return convertToVOPage(itemPage);
    }

    @Transactional
    public void publishItem(HeritageItemDTO dto, Long userId, Integer publisherType) {
        if (publisherType == 1) {
            SysUser user = userMapper.selectById(userId);
            if (user == null || user.getHeritageCategoryId() == null) {
                throw new BusinessException("传承人未关联非遗分类");
            }
            if (!user.getHeritageCategoryId().equals(dto.getHeritageCategoryId())) {
                throw new BusinessException("传承人只能发布自己所属类型的非遗项目");
            }
        }
        HeritageItem item = new HeritageItem();
        BeanUtils.copyProperties(dto, item);
        item.setPublisherId(userId);
        item.setPublisherType(publisherType);
        item.setViewCount(0);
        item.setLikeCount(0);
        item.setFavoriteCount(0);
        item.setCommentCount(0);
        item.setStatus(1);
        item.setIsRecommend(0);
        itemMapper.insert(item);
        saveImages(item.getId(), dto.getImages());
        saveVideos(item.getId(), dto.getVideos());
    }

    @Transactional
    public void updateItem(HeritageItemDTO dto, Long userId) {
        HeritageItem item = itemMapper.selectById(dto.getId());
        if (item == null) {
            throw new BusinessException("项目不存在");
        }
        if (!item.getPublisherId().equals(userId)) {
            throw new BusinessException("无权修改此项目");
        }
        BeanUtils.copyProperties(dto, item);
        itemMapper.updateById(item);
        LambdaQueryWrapper<HeritageImage> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(HeritageImage::getHeritageItemId, item.getId());
        imageMapper.delete(imageWrapper);
        LambdaQueryWrapper<HeritageVideo> videoWrapper = new LambdaQueryWrapper<>();
        videoWrapper.eq(HeritageVideo::getHeritageItemId, item.getId());
        videoMapper.delete(videoWrapper);
        saveImages(item.getId(), dto.getImages());
        saveVideos(item.getId(), dto.getVideos());
    }

    @Transactional
    public void deleteItem(Long id, Long userId) {
        HeritageItem item = itemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("项目不存在");
        }
        if (!item.getPublisherId().equals(userId)) {
            throw new BusinessException("无权删除此项目");
        }
        itemMapper.deleteById(id);
    }

    public Page<HeritageItemVO> getMyItems(Integer page, Integer size, Long userId) {
        Page<HeritageItem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HeritageItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HeritageItem::getPublisherId, userId)
                .orderByDesc(HeritageItem::getCreateTime);
        Page<HeritageItem> itemPage = itemMapper.selectPage(pageParam, wrapper);
        return convertToVOPage(itemPage);
    }

    public Page<HeritageItemVO> getAdminItemList(Integer page, Integer size, Integer status) {
        Page<HeritageItem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HeritageItem> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(HeritageItem::getStatus, status);
        }
        wrapper.orderByDesc(HeritageItem::getCreateTime);
        Page<HeritageItem> itemPage = itemMapper.selectPage(pageParam, wrapper);
        return convertToVOPage(itemPage);
    }

    public void updateStatus(Long id, Integer status) {
        HeritageItem item = itemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("项目不存在");
        }
        item.setStatus(status);
        itemMapper.updateById(item);
    }

    private void saveImages(Long itemId, List<HeritageImageDTO> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        for (HeritageImageDTO dto : images) {
            HeritageImage image = new HeritageImage();
            image.setHeritageItemId(itemId);
            image.setImageUrl(dto.getImageUrl());
            image.setDescription(dto.getDescription());
            image.setSort(dto.getSort() != null ? dto.getSort() : 0);
            imageMapper.insert(image);
        }
    }

    private void saveVideos(Long itemId, List<HeritageVideoDTO> videos) {
        if (videos == null || videos.isEmpty()) {
            return;
        }
        for (HeritageVideoDTO dto : videos) {
            HeritageVideo video = new HeritageVideo();
            video.setHeritageItemId(itemId);
            video.setVideoUrl(dto.getVideoUrl());
            video.setCoverUrl(dto.getCoverUrl());
            video.setTitle(dto.getTitle());
            video.setDescription(dto.getDescription());
            video.setDuration(dto.getDuration());
            video.setSort(dto.getSort() != null ? dto.getSort() : 0);
            videoMapper.insert(video);
        }
    }

    private Page<HeritageItemVO> convertToVOPage(Page<HeritageItem> itemPage) {
        Page<HeritageItemVO> voPage = new Page<>(itemPage.getCurrent(), itemPage.getSize(), itemPage.getTotal());
        List<HeritageItemVO> voList = itemPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    private HeritageItemVO convertToVO(HeritageItem item) {
        HeritageItemVO vo = new HeritageItemVO();
        BeanUtils.copyProperties(item, vo);
        HeritageCategory category = categoryMapper.selectById(item.getHeritageCategoryId());
        if (category != null) {
            vo.setHeritageCategoryName(category.getName());
        }
        if (item.getPublisherId() != null) {
            SysUser publisher = userMapper.selectById(item.getPublisherId());
            if (publisher != null) {
                vo.setPublisherName(publisher.getNickname() != null ? publisher.getNickname() : publisher.getUsername());
                vo.setPublisherAvatar(publisher.getAvatar());
            }
        }
        LambdaQueryWrapper<HeritageImage> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(HeritageImage::getHeritageItemId, item.getId())
                .orderByAsc(HeritageImage::getSort);
        List<HeritageImage> images = imageMapper.selectList(imageWrapper);
        vo.setImages(images.stream().map(img -> {
            HeritageImageVO imgVO = new HeritageImageVO();
            BeanUtils.copyProperties(img, imgVO);
            return imgVO;
        }).collect(Collectors.toList()));
        LambdaQueryWrapper<HeritageVideo> videoWrapper = new LambdaQueryWrapper<>();
        videoWrapper.eq(HeritageVideo::getHeritageItemId, item.getId())
                .orderByAsc(HeritageVideo::getSort);
        List<HeritageVideo> videos = videoMapper.selectList(videoWrapper);
        vo.setVideos(videos.stream().map(vid -> {
            HeritageVideoVO vidVO = new HeritageVideoVO();
            BeanUtils.copyProperties(vid, vidVO);
            return vidVO;
        }).collect(Collectors.toList()));
        return vo;
    }
}
