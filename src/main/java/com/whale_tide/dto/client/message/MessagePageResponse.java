package com.whale_tide.dto.client.message;

import lombok.Data;

import java.util.List;

@Data
public class MessagePageResponse {
    List<MessageResponse> list;
    Long pageNum;
    Long pageSize;
    Long total;
    Long totalPage;
}
