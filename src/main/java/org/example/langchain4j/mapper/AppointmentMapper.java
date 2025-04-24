package org.example.langchain4j.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.langchain4j.entity.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 预约信息表 Mapper 接口
 * </p>
 *
 * @author liu bo
 * @since 2025-04-24
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

}
