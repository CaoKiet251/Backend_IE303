package com.example.Backend_IE303.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String shiftType; // e.g., "MORNING", "AFTERNOON", "NIGHT"

    private LocalDateTime time_in;

    private LocalDateTime time_out;

}
