package com.employee.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Setter
@Getter
public class EmployeeSession {

    private String sessionId;
    private Timestamp createdTime;
    private String employeeName;

}
