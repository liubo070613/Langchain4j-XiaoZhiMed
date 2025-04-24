package org.example.langchain4j.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 预约信息表
 * </p>
 *
 * @author liu bo
 * @since 2025-04-24
 */
@Data
@TableName("appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预约人姓名
     */
    @TableField("username")
    private String username;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 预约科室
     */
    @TableField("department")
    private String department;

    /**
     * 预约日期（格式：yyyy-MM-dd）
     */
    @TableField("date")
    private String date;

    /**
     * 预约时间（格式：HH:mm）
     */
    @TableField("time")
    private String time;

    /**
     * 医生姓名
     */
    @TableField("doctor_name")
    private String doctorName;
}
