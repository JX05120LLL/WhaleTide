package com.whaletide.admin.dashboard.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardToduVO {
    String content;
    LocalDateTime date;
    String status;

    public DashboardToduVO(String content, LocalDateTime date, String status) {
        this.content = content;
        this.date = date;
        this.status = status;
    }
}
