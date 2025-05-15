package com.whaletide.admin.dashboard.controller;


import com.whaletide.admin.dashboard.service.IDashboardService;
import com.whaletide.admin.dashboard.vo.DashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@Tag(name = "面板相关接口")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @Operation(summary = "获取面板数据")
    @RequestMapping("/stats")
    public DashboardVO stats() {
        DashboardVO dashboardVO = dashboardService.stats();
        return dashboardVO;
    }

}
