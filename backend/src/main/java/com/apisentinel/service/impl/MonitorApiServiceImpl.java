package com.apisentinel.service.impl;

import com.apisentinel.entity.MonitorApi;
import com.apisentinel.mapper.MonitorApiMapper;
import com.apisentinel.service.MonitorApiService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控接口 Service 实现类
 *
 * @author ApiSentinel Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorApiServiceImpl extends ServiceImpl<MonitorApiMapper, MonitorApi> implements MonitorApiService {

    private final MonitorApiMapper monitorApiMapper;

    @Override
    public IPage<MonitorApi> pageList(Integer page, Integer size) {
        Page<MonitorApi> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MonitorApi> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MonitorApi::getCreateTime);
        return this.page(pageParam, wrapper);
    }

    @Override
    public List<MonitorApi> listEnabledApis() {
        return monitorApiMapper.selectEnabledApis();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addApi(MonitorApi monitorApi) {
        // 设置默认值
        if (monitorApi.getStatus() == null) {
            monitorApi.setStatus(1);
        }
        if (monitorApi.getCheckInterval() == null) {
            monitorApi.setCheckInterval(5);
        }
        if (monitorApi.getTimeout() == null) {
            monitorApi.setTimeout(10);
        }
        if (monitorApi.getFailThreshold() == null) {
            monitorApi.setFailThreshold(3);
        }
        return this.save(monitorApi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApi(MonitorApi monitorApi) {
        if (monitorApi.getId() == null) {
            return false;
        }
        return this.updateById(monitorApi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteApi(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleStatus(Long id, Integer status) {
        MonitorApi api = new MonitorApi();
        api.setId(id);
        api.setStatus(status);
        return this.updateById(api);
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总监控数
        Long total = monitorApiMapper.countTotal();
        // 正常数量
        Long normal = monitorApiMapper.countNormal();
        // 异常数量
        Long abnormal = monitorApiMapper.countAbnormal();
        // 今日检查次数
        Long todayChecks = baseMapper.countTodayChecks();
        
        stats.put("total", total);
        stats.put("normal", normal);
        stats.put("abnormal", abnormal);
        stats.put("todayChecks", todayChecks);
        
        return stats;
    }
}
