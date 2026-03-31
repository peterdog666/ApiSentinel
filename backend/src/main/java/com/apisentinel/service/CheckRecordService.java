package com.apisentinel.service;

import com.apisentinel.entity.CheckRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 检查记录 Service
 *
 * @author ApiSentinel Team
 */
public interface CheckRecordService extends IService<CheckRecord> {

    /**
     * 分页查询检查记录
     *
     * @param apiId API ID
     * @param page  页码
     * @param size  每页大小
     * @return 分页结果
     */
    IPage<CheckRecord> pageList(Long apiId, Integer page, Integer size);

    /**
     * 根据API ID查询检查记录
     *
     * @param apiId API ID
     * @return 检查记录列表
     */
    List<CheckRecord> listByApiId(Long apiId);

    /**
     * 查询API最近N次的检查记录
     *
     * @param apiId API ID
     * @param limit 限制数量
     * @return 检查记录列表
     */
    List<CheckRecord> listRecentByApiId(Long apiId, Integer limit);

    /**
     * 保存检查记录
     *
     * @param record 检查记录
     * @return 是否成功
     */
    boolean saveRecord(CheckRecord record);

    /**
     * 获取API的可用率统计
     *
     * @param apiId     API ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 可用率
     */
    Double getAvailability(Long apiId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取API的响应时间趋势数据
     *
     * @param apiId     API ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 趋势数据
     */
    List<Map<String, Object>> getResponseTimeTrend(Long apiId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询API最近的连续失败次数
     *
     * @param apiId API ID
     * @return 连续失败次数
     */
    Integer countConsecutiveFailures(Long apiId);
}
