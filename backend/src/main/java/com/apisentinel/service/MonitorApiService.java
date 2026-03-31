package com.apisentinel.service;

import com.apisentinel.entity.MonitorApi;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 监控接口 Service
 *
 * @author ApiSentinel Team
 */
public interface MonitorApiService extends IService<MonitorApi> {

    /**
     * 分页查询监控接口
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    IPage<MonitorApi> pageList(Integer page, Integer size);

    /**
     * 获取所有启用的监控接口
     *
     * @return 启用的监控接口列表
     */
    List<MonitorApi> listEnabledApis();

    /**
     * 添加监控接口
     *
     * @param monitorApi 监控接口
     * @return 是否成功
     */
    boolean addApi(MonitorApi monitorApi);

    /**
     * 更新监控接口
     *
     * @param monitorApi 监控接口
     * @return 是否成功
     */
    boolean updateApi(MonitorApi monitorApi);

    /**
     * 删除监控接口
     *
     * @param id 接口ID
     * @return 是否成功
     */
    boolean deleteApi(Long id);

    /**
     * 启用/禁用监控接口
     *
     * @param id     接口ID
     * @param status 状态：0-禁用，1-启用
     * @return 是否成功
     */
    boolean toggleStatus(Long id, Integer status);

    /**
     * 获取Dashboard统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getDashboardStats();
}
