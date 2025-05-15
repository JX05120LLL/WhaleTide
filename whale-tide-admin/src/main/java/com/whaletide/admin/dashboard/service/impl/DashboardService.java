package com.whaletide.admin.dashboard.service.impl;

import com.whaletide.admin.dashboard.service.IDashboardService;
import com.whaletide.admin.dashboard.vo.DashboardAnnouncementVO;
import com.whaletide.admin.dashboard.vo.DashboardStatsVO;
import com.whaletide.admin.dashboard.vo.DashboardToduVO;
import com.whaletide.admin.dashboard.vo.DashboardVO;
import com.whaletide.common.mapper.pms.PmsProductCommentsMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import com.whaletide.common.mapper.ums.UmsUsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DashboardService implements IDashboardService {

    @Autowired
    private UmsUsersMapper usersMapper;

    @Autowired
    private PmsProductCommentsMapper ProductCommentsMapper;

    @Autowired
    private PmsProductsMapper productsMapper;

    @Override
    public DashboardVO stats() {
        DashboardVO dashboardVO = new DashboardVO();

        DashboardStatsVO statsVO = new DashboardStatsVO();
        statsVO.setUserCount(usersMapper.selectCount(null));
        statsVO.setMessageCount(ProductCommentsMapper.selectCount(null));
        statsVO.setProductCount(productsMapper.selectCount(null));
        dashboardVO.setStats(statsVO);

        List<DashboardAnnouncementVO> announcements = new ArrayList<>();
        announcements.add(new DashboardAnnouncementVO("title1", "content1", LocalDateTime.of(2021, 1, 1, 0, 0, 0)));
        announcements.add(new DashboardAnnouncementVO("title2", "content2", LocalDateTime.of(2021, 1, 2, 0, 0, 0)));
        announcements.add(new DashboardAnnouncementVO("title3", "content3", LocalDateTime.of(2021, 1, 3, 0, 0, 0)));
        dashboardVO.setAnnouncements(announcements);

        List<DashboardToduVO> todos = new ArrayList<>();
        todos.add(new DashboardToduVO("content1", LocalDateTime.of(2021, 1, 1, 0, 0, 0), "status1"));
        todos.add(new DashboardToduVO("content2", LocalDateTime.of(2021, 1, 2, 0, 0, 0), "status2"));
        todos.add(new DashboardToduVO("content3", LocalDateTime.of(2021, 1, 3, 0, 0, 0), "status3"));
        dashboardVO.setTodos(todos);

        return dashboardVO;
    }
}
