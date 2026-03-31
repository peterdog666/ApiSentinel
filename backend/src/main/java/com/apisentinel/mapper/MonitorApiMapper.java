package com.apisentinel.mapper;

import com.apisentinel.entity.MonitorApi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 监控接口 Mapper
 *
 * @author ApiSentinel Team
 */
@Mapper
public interface MonitorApiMapper extends BaseMapper<MonitorApi> {

    /**
     * 查询所有启用的监控接口
     *
     * @return 启用的监控接口列表
     */
    @Select("SELECT * FROM monitor_api WHERE status = 1 AND deleted = 0")
    List<MonitorApi> selectEnabledApis();

    /**
     * 统计监控接口总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM monitor_api WHERE deleted = 0")
    Long countTotal();

    /**
     * 统计正常状态的监控接口数量
     *
     * @return 正常数量
     */
    @Select("SELECT COUNT(*) FROM monitor_api WHERE status = 1 AND deleted = 0")
    Long countNormal();

    /**
     * 统计异常状态的监控接口数量
     *
     * @return 异常数量
     */
    @Select("SELECT COUNT(*) FROM monitor_api WHERE status = 0 AND deleted = 0")
    Long countAbnormal();
}
