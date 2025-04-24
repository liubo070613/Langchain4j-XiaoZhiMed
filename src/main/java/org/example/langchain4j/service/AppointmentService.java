package org.example.langchain4j.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.langchain4j.entity.Appointment;

public interface AppointmentService extends IService<Appointment> {

    public Appointment getOne(Appointment appointment);
}
