package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsNotifications;
import com.whale_tide.mapper.ums.UmsNotificationsMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class MessageService implements IMessageService {


    @Autowired
    UmsNotificationsMapper notificationsMapper;

    @Autowired
    UmsUsersMapper usersMapper;


    @Override
    public Page<UmsNotifications> getMessagesWithStatus(Long userId, int status, Long page, Long size) {
        if (userId == null || page == null || size == null) {
            return null;
        }


        Page<UmsNotifications> pageInfo = new Page<>(page, size);
        return notificationsMapper.selectPage(pageInfo, new QueryWrapper<UmsNotifications>()
                .eq("receiver_id", userId)
                .eq("read_status", status));
    }


    @Override
    public UmsNotifications getMessageDetail(Long messageId) {
        if (messageId == null) {
            return null;
        }

        return notificationsMapper.selectById(messageId);
    }

    @Override
    public int markMessageRead(Long messageId) {
        if (messageId == null)
            return -1;

        UmsNotifications umsNotifications = notificationsMapper.selectById(messageId);

        if (umsNotifications != null) {
            if (umsNotifications.getReadStatus() == 1)
                return 0;
            umsNotifications.setReadStatus(1);
            umsNotifications.setReadTime(LocalDateTime.now());
            int result = notificationsMapper.updateById(umsNotifications);
            if (result == 1)
                return 1;
            else
                return -2;
        } else
            return -1;
    }

    @Override
    public int deleteMessage(Long messageId) {
        if (messageId == null)
            return -1;

        UmsNotifications umsNotifications = notificationsMapper.selectById(messageId);
        if (umsNotifications == null)
            return -1;

        int result = notificationsMapper.deleteById(messageId);
        if (result == 1)
            return result;
        else
            return -2;
    }

    @Override
    public Long getUnreadMessageCount(Long userId) {
        if (userId == null)
            return -1L;

        if (usersMapper.selectById(userId) == null)
            return -1L;

        return notificationsMapper.selectCount(new QueryWrapper<UmsNotifications>()
                .eq("receiver_id", userId)
                .eq("read_status", 0));
    }
}
