package com.whale_tide.dto.client.message;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private String title;
    private String content;
    private Integer readStatus;
    private LocalDateTime createTime;
}
