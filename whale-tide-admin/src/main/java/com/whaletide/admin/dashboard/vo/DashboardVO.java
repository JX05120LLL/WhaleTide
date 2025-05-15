package com.whaletide.admin.dashboard.vo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardVO {
    DashboardStatsVO stats;
    List<DashboardAnnouncementVO> announcements;
    List<DashboardToduVO> todos;
}
