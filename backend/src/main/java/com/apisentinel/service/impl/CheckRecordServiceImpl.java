package com.apisentinel.service.impl;

import com.apisentinel.entity.CheckRecord;
import com.apisentinel.mapper.CheckRecordMapper;
import com.apisentinel.service.CheckRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查记录 Service 实现类
 *
 * @author ApiSentinel Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckRecordServiceImpl extends ServiceImpl<CheckRecordMapper, CheckRecord> implements CheckRecordService {

    private final CheckRecordMapper checkRecordMapper;

    @Override
    public IPage<CheckRecord> pageList(Long apiId, Integer page, Integer size) {
        Page<CheckRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<CheckRecord> wrapper = new LambdaQueryWrapper<>();
        if (apiId != null) {
            wrapper.eq(CheckRecord::getApiId, apiId);
        }
        wrapper.orderByDesc(CheckRecord::getCheckTime);
        return this.page(pageParam, wrapper);
    }

    @Override
    public List<CheckRecord> listByApiId(Long apiId) {
        return checkRecordMapper.selectByApiId(apiId);
    }

    @Override
    public List<CheckRecord> listRecentByApiId(Long apiId, Integer limit) {
        return checkRecordMapper.selectRecentByApiId(apiId, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRecord(CheckRecord record) {
        return this.save(record);
    }

    @Override
    public Double getAvailability(Long apiId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(7);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        Double availability = checkRecordMapper.calculateAvailability(apiId, startTime, endTime);
        return availability != null ? availability : 100.0;
    }

    @Override
    public List<Map<String, Object>> getResponseTimeTrend(Long apiId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(7);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        
        LambdaQueryWrapper<CheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckRecord::getApiId, apiId)
               .between(CheckRecord::getCheckTime, startTime, endTime)
               .orderByAsc(CheckRecord::getCheckTime);
        
        List<CheckRecord> records = this.list(wrapper);
        List<Map<String, Object>> trend = new ArrayList<>();
        
        for (CheckRecord record : records) {
            Map<String, Object> point = new HashMap<>();
            point.put("time", record.getCheckTime().toString());
            point.put("responseTime", record.getResponseTime());
            point.put("success", record.getSuccess());
            point.put("statusCode", record.getStatusCode());
            trend.add(point);
        }
        
        return trend;
    }

    @Override
    public Integer countConsecutiveFailures(Long apiId) {
        return checkRecordMapper.countConsecutiveFailures(apiId);
    }
}
