package com.apisentinel.mapper;

import com.apisentinel.entity.CheckRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检查记录 Mapper
 *
 * @author ApiSentinel Team
 */
@Mapper
public interface CheckRecordMapper extends BaseMapper<CheckRecord> {

    /**
     * 根据API ID查询检查记录
     *
     * @param apiId API ID
     * @return 检查记录列表
     */
    @Select("SELECT * FROM check_record WHERE api_id = #{apiId} ORDER BY check_time DESC")
    List<CheckRecord> selectByApiId(@Param("apiId") Long apiId);

    /**
     * 统计今日检查次数
     *
     * @return 今日检查次数
     */
    @Select("SELECT COUNT(*) FROM check_record WHERE DATE(check_time) = CURDATE()")
    Long countTodayChecks();

    /**
     * 查询API最近N次的检查记录
     *
     * @param apiId API ID
     * @param limit 限制数量
     * @return 检查记录列表
     */
    @Select("SELECT * FROM check_record WHERE api_id = #{apiId} ORDER BY check_time DESC LIMIT #{limit}")
    List<CheckRecord> selectRecentByApiId(@Param("apiId") Long apiId, @Param("limit") Integer limit);

    /**
     * 查询API在指定时间范围内的检查记录
     *
     * @param apiId     API ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 检查记录列表
     */
    @Select("SELECT * FROM check_record WHERE api_id = #{apiId} AND check_time BETWEEN #{startTime} AND #{endTime} ORDER BY check_time DESC")
    List<CheckRecord> selectByTimeRange(@Param("apiId") Long apiId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 计算API的可用率
     *
     * @param apiId     API ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 可用率（0-100）
     */
    @Select("SELECT ROUND(SUM(CASE WHEN success = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) FROM check_record WHERE api_id = #{apiId} AND check_time BETWEEN #{startTime} AND #{endTime}")
    Double calculateAvailability(@Param("apiId") Long apiId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询API最近的连续失败次数
     *
     * @param apiId API ID
     * @return 连续失败次数
     */
    @Select("SELECT COUNT(*) FROM check_record cr1 WHERE cr1.api_id = #{apiId} AND cr1.success = 0 AND NOT EXISTS (SELECT 1 FROM check_record cr2 WHERE cr2.api_id = #{apiId} AND cr2.success = 1 AND cr2.check_time > cr1.check_time)")
    Integer countConsecutiveFailures(@Param("apiId") Long apiId);
}
