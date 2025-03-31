package com.whale_tide.controller.client;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.message.MessagePageResponse;
import com.whale_tide.dto.client.message.MessageResponse;
import com.whale_tide.entity.ums.UmsNotifications;
import com.whale_tide.service.client.IMessageService;
import com.whale_tide.service.client.IUserService;
import com.whale_tide.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "系统消息接口")
@RestController("clientMessageController")
@RequestMapping("/member/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUitl;

    /**
     * 获取消息列表
     *
     * @param status
     * @param page
     * @param size
     * @param token
     * @return
     */
    @RequestMapping("/list")
    public CommonResult<MessagePageResponse> list(@RequestParam("readStatus") int status,
                                                  @RequestParam("page") Long page,
                                                  @RequestParam("size") Long size,
                                                  @RequestHeader("Authorization") String token) {

        Page<UmsNotifications> messages = messageService.getMessagesWithStatus(userService.getUserInfo(jwtUitl.getUsernameFromToken(token)).getId()
                , status, page, size);

        List<MessageResponse> messageResponses = new ArrayList<>();
        for (UmsNotifications message : messages.getRecords()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setId(message.getId());
            messageResponse.setTitle(message.getTitle());
            messageResponse.setContent(message.getContent());
            messageResponse.setReadStatus(message.getReadStatus());
            messageResponse.setCreateTime(message.getCreateTime());

            messageResponses.add(messageResponse);
        }
        MessagePageResponse messagePageResponse = new MessagePageResponse();
        messagePageResponse.setList(messageResponses);
        messagePageResponse.setPageNum(messages.getCurrent());
        messagePageResponse.setPageSize(messages.getSize());
        messagePageResponse.setTotal(messages.getTotal());
        messagePageResponse.setTotalPage(messages.getPages());

        return CommonResult.success(messagePageResponse);
    }

    /**
     * 获取消息详情
     *
     * @param messageId
     * @return
     */
    @RequestMapping("/{id}")
    public CommonResult<MessageResponse> detail(@PathVariable("id") Long messageId) {
        UmsNotifications message = messageService.getMessageDetail(messageId);
        if (message == null) {
            return CommonResult.failed();
        }
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(message.getId());
        messageResponse.setTitle(message.getTitle());
        messageResponse.setContent(message.getContent());
        messageResponse.setReadStatus(message.getReadStatus());
        messageResponse.setCreateTime(message.getCreateTime());

        return CommonResult.success(messageResponse);
    }

    /**
     * 标记消息为已读
     *
     * @param messageId
     * @return
     */
    @RequestMapping("/read/{id}")
    public CommonResult<Void> markMessageRead(@PathVariable("id") Long messageId) {
        int result = messageService.markMessageRead(messageId);
        if (result == 1) {
            return CommonResult.success(null);
        } else if (result == 0) {
            return CommonResult.failed("已经标记过为已读");
        } else if (result == -1) {
            return CommonResult.failed("消息不存在");
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 删除消息
     *
     * @param messageId
     * @return
     */
    @RequestMapping("/delete/{id}")
    public CommonResult<Void> deleteMessage(@PathVariable("id") Long messageId) {
        int result = messageService.deleteMessage(messageId);
        if (result == 1) {
            return CommonResult.success(null);
        } else if (result == -1) {
            return CommonResult.failed("消息不存在");
        } else {
            return CommonResult.failed("服务器内部错误");
        }
    }

    /**
     * 获取未读消息数量
     *
     * @param token
     * @return
     */
    @RequestMapping("/unreadCount")
    public CommonResult<Long> getUnreadMessageCount(@RequestHeader("Authorization") String token) {
        Long userId = userService.getUserInfo(jwtUitl.getUsernameFromToken(token)).getId();
        Long count = messageService.getUnreadMessageCount(userId);
        if (count == -1L)
            return CommonResult.failed("用户信息错误");
        else if (count >= 0L)
            return CommonResult.success(count);
        else
            return CommonResult.failed("服务器内部错误");
    }

}
