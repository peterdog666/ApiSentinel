package com.apisentinel.controller;

import com.apisentinel.common.R;
import com.apisentinel.entity.CheckRecord;
import com.apisentinel.service.CheckRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 检查记录 Controller
 *
 * @author ApiSentinel Team
 */
@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
@CrossOrigin
public class CheckRecordController {

    private final CheckRecordService checkRecordService;

    /**
     * 分页查询检查记录
     */
    @GetMapping("/list")
    public R<IPage<CheckRecord>> list(
            @RequestParam(required = false) Long apiId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(checkRecordService.pageList(apiId, page, size));
    }

    /**
     * 根据API ID查询检查记录
     */
    @GetMapping("/api/{apiId}")
    public R<List<CheckRecord>> listByApiId(@PathVariable Long apiId) {
        return R.ok(checkRecordService.listByApiId(apiId));
    }

    /**
     * 查询API最近N次的检查记录
     */
    @GetMapping("/api/{apiId}/recent")
    public R<List<CheckRecord>> listRecentByApiId(
            @PathVariable Long apiId,
            @RequestParam(defaultValue = "50") Integer limit) {
        return R.ok(checkRecordService.listRecentByApiId(apiId, limit));
    }

    /**
     * 获取API的可用率统计
     */
    @GetMapping("/api/{apiId}/availability")
    public R<Double> getAvailability(
            @PathVariable Long apiId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return R.ok(checkRecordService.getAvailability(apiId, startTime, endTime));
    }

    /**
     * 获取API的响应时间趋势数据
     */
    @GetMapping("/api/{apiId}/trend")
    public R<List<Map<String, Object>>> getResponseTimeTrend(
            @PathVariable Long apiId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return R.ok(checkRecordService.getResponseTimeTrend(apiId, startTime, endTime));
    }

    /**
     * 查询API最近的连续失败次数
     */
    @GetMapping("/api/{apiId}/consecutive-failures")
    public R<Integer> countConsecutiveFailures(@PathVariable Long apiId) {
        return R.ok(checkRecordService.countConsecutiveFailures(apiId));
    }
}
