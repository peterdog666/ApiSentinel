package com.apisentinel.controller;

import com.apisentinel.common.R;
import com.apisentinel.entity.MonitorApi;
import com.apisentinel.service.MonitorApiService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 监控接口 Controller
 *
 * @author ApiSentinel Team
 */
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
@CrossOrigin
public class MonitorApiController {

    private final MonitorApiService monitorApiService;

    /**
     * 分页查询监控接口列表
     */
    @GetMapping("/list")
    public R<IPage<MonitorApi>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(monitorApiService.pageList(page, size));
    }

    /**
     * 获取所有启用的监控接口
     */
    @GetMapping("/enabled")
    public R<List<MonitorApi>> listEnabled() {
        return R.ok(monitorApiService.listEnabledApis());
    }

    /**
     * 根据ID获取监控接口详情
     */
    @GetMapping("/{id}")
    public R<MonitorApi> getById(@PathVariable Long id) {
        MonitorApi api = monitorApiService.getById(id);
        if (api == null) {
            return R.notFound("接口不存在");
        }
        return R.ok(api);
    }

    /**
     * 添加监控接口
     */
    @PostMapping
    public R<Void> add(@Valid @RequestBody MonitorApi monitorApi) {
        boolean success = monitorApiService.addApi(monitorApi);
        return success ? R.ok("添加成功") : R.error("添加失败");
    }

    /**
     * 更新监控接口
     */
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody MonitorApi monitorApi) {
        monitorApi.setId(id);
        boolean success = monitorApiService.updateApi(monitorApi);
        return success ? R.ok("更新成功") : R.error("更新失败");
    }

    /**
     * 删除监控接口
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        boolean success = monitorApiService.deleteApi(id);
        return success ? R.ok("删除成功") : R.error("删除失败");
    }

    /**
     * 启用监控接口
     */
    @PutMapping("/{id}/enable")
    public R<Void> enable(@PathVariable Long id) {
        boolean success = monitorApiService.toggleStatus(id, 1);
        return success ? R.ok("启用成功") : R.error("启用失败");
    }

    /**
     * 禁用监控接口
     */
    @PutMapping("/{id}/disable")
    public R<Void> disable(@PathVariable Long id) {
        boolean success = monitorApiService.toggleStatus(id, 0);
        return success ? R.ok("禁用成功") : R.error("禁用失败");
    }

    /**
     * 获取Dashboard统计数据
     */
    @GetMapping("/dashboard/stats")
    public R<Map<String, Object>> getDashboardStats() {
        return R.ok(monitorApiService.getDashboardStats());
    }
}
