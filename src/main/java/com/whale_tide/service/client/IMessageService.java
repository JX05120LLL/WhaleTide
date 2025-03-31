package com.whale_tide.service.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsNotifications;

public interface IMessageService {
    //获取消息列表（分页）
    Page<UmsNotifications> getMessagesWithStatus(Long userId, int status, Long page, Long size);

    //获取系统消息详情
    UmsNotifications getMessageDetail(Long messageId);

    //标记消息为已读
    int markMessageRead(Long messageId);

    //删除系统消息
    int deleteMessage(Long messageId);

    //获取未读消息数量
    Long getUnreadMessageCount(Long userId);
}
