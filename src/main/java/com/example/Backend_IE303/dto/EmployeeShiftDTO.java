package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShiftDTO {
    private Integer id;
    private Integer employeeId;
    private LocalDateTime date;
    private String shiftType;
    private LocalDateTime time_in;
    private LocalDateTime time_out;
}