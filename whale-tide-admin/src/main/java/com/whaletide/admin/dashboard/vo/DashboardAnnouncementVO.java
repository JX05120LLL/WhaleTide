package com.whaletide.admin.dashboard.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardAnnouncementVO {
    String title;
    String content;
    LocalDateTime date;

    public DashboardAnnouncementVO(String title, String content, LocalDateTime date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
