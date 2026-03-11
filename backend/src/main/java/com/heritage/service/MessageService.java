package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.dto.MessageReplyDTO;
import com.heritage.entity.Message;
import com.heritage.entity.SysUser;
import com.heritage.exception.BusinessException;
import com.heritage.mapper.MessageMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    public Page<MessageVO> getPage(Integer page, Integer size) {
        Page<Message> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Message::getCreateTime);
        Page<Message> entityPage = messageMapper.selectPage(pageParam, wrapper);
        Page<MessageVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    public void delete(Long id) {
        requireById(id);
        messageMapper.deleteById(id);
    }

    public void reply(Long id, MessageReplyDTO dto, Long adminUserId) {
        Message source = requireById(id);
        SysUser admin = userMapper.selectById(adminUserId);
        Message reply = new Message();
        reply.setContent(dto.getContent());
        reply.setUserId(adminUserId);
        reply.setUserName(admin == null ? "管理员" : (StringUtils.hasText(admin.getNickname()) ? admin.getNickname() : admin.getUsername()));
        reply.setUserAvatar(admin == null ? null : admin.getAvatar());
        reply.setParentId(source.getId());
        reply.setReplyUserId(source.getUserId());
        reply.setReplyUserName(source.getUserName());
        reply.setStatus(1);
        messageMapper.insert(reply);
    }

    private Message requireById(Long id) {
        Message message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }
        return message;
    }

    private MessageVO toVO(Message message) {
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);
        return vo;
    }
}
